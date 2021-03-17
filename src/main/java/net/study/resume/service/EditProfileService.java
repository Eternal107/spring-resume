package net.study.resume.service;

import net.study.resume.component.UploadCertificateLinkManager;
import net.study.resume.entity.*;
import net.study.resume.exception.FormValidationException;
import net.study.resume.form.PasswordForm;
import net.study.resume.model.UploadResult;
import net.study.resume.repository.search.ProfileSearchRepository;
import net.study.resume.repository.storage.*;
import net.study.resume.security.UserPrincipal;
import net.study.resume.service.image.FileImageStorageService;
import net.study.resume.service.image.ImageProcessorService;
import net.study.resume.util.DataUtil;
import net.study.resume.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.beans.Transient;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EditProfileService {

    private static Logger LOGGER = LoggerFactory.getLogger(EditProfileService.class);

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    CacheService cacheService;

    @Autowired
    ProfileSearchRepository profileSearchRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    SkillCategoryRepository skillCategoryRepository;

    @Autowired
    PracticRepository practicRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CertificateRepository certificateRepository;

    @Autowired
    EducationRepository educationRepository;

    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    HobbyRepository hobbyRepository;

    @Autowired
    StaticDataService staticDataService;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    private ImageProcessorService imageProcessorService;

    @Autowired
    private FileImageStorageService fileImageStorageService;

    @Autowired
    private UploadCertificateLinkManager uploadCertificateLinkManager;

    private Map<Class<? extends ProfileEntity>,AbstractProfileRepository<? extends ProfileEntity>> profileEntityRepositoryMap;

    @PostConstruct
    private void postConstruct() {
        Map<Class<? extends ProfileEntity>,AbstractProfileRepository<? extends ProfileEntity>> map = new HashMap<>();
        map.put(Certificate.class, certificateRepository);
        map.put(Course.class, courseRepository);
        map.put(Education.class, educationRepository);
        map.put(Hobby.class, hobbyRepository);
        map.put(Language.class, languageRepository);
        map.put(Practic.class, practicRepository);
        map.put(Skill.class,skillRepository);
        profileEntityRepositoryMap = Collections.unmodifiableMap(map);
    }

    @Transactional
    public void updateProfileData(Long id, Profile profileForm, MultipartFile uploadPhoto) {
        Profile loadedProfile = profileRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException("this id doesn't exist"));
        profileForm.setId(id);
        updateProfilePhoto(loadedProfile,uploadPhoto);
        checkForDuplicatesInForm(profileForm);
        editProfileData(profileForm,loadedProfile);
        updatePrincipalIfDataChanged(profileForm,loadedProfile);
        loadedProfile.setCompleted(true);
        updateIndexProfileDataIfTransactionSuccess(loadedProfile);
        evictProfileCacheIfTransactionSuccess(loadedProfile);
        LOGGER.info("Profile updated");
    }

    private void updatePrincipalIfDataChanged(Profile profileForm, Profile loadedProfile) {
        boolean update = false;
        if(!profileForm.getUid().equals(loadedProfile.getUid())){
            loadedProfile.setUid(profileForm.getUid());
            update=true;
        }
        if(!profileForm.getFirstName().equals(loadedProfile.getFullName())){
            loadedProfile.setFirstName(profileForm.getFirstName());
            loadedProfile.setLastName(profileForm.getLastName());
            update=true;
        }
        if(update){
            securityUtil.authentificate(loadedProfile);
        }
    }

    public void updateProfilePhoto(Profile profile,MultipartFile uploadPhoto) {
        if (!uploadPhoto.isEmpty()) {
            UploadResult uploadResult = imageProcessorService.processNewProfilePhoto(uploadPhoto);
            deleteUploadedPhotosIfTransactionFailed(uploadResult);
            removeOldPhotosIfTransactionSucces(Arrays.asList(profile.getLargePhoto(),profile.getSmallPhoto()));

            profile.setLargePhoto(uploadResult.getLargeUrl());
            profile.setSmallPhoto(uploadResult.getSmallUrl());

        }
    }

    protected void deleteUploadedPhotosIfTransactionFailed(final UploadResult uploadResult) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    fileImageStorageService.remove(Arrays.asList(uploadResult.getLargeUrl(), uploadResult.getSmallUrl()));
                }
            }
        });
    }

    protected void removeOldPhotosIfTransactionSucces(final List<String> oldPhotos) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {

                fileImageStorageService.remove(oldPhotos);
            }
        });
    }

    protected void updateIndexProfileDataIfTransactionSuccess(final Profile profile) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                profileSearchRepository.save(profile);
                LOGGER.info("Profile index updated: {}", profile.getUid());
            }
        });
    }


    private void editProfileData(Profile profileForm , Profile loadedProfile){
            loadedProfile.setFirstName(profileForm.getFirstName());
            loadedProfile.setLastName(profileForm.getLastName());
            loadedProfile.setCity(profileForm.getCity());
            loadedProfile.setBirthDay(profileForm.getBirthDay());
            loadedProfile.setObjective(profileForm.getObjective());
            loadedProfile.setCountry(profileForm.getCountry());
            loadedProfile.setEmail(profileForm.getEmail());
            loadedProfile.setPhone(profileForm.getPhone());
            loadedProfile.setSummary(profileForm.getSummary());
    }

    protected void checkForDuplicatesInForm(Profile profileForm) {
        Profile profileForEmail = profileRepository.findByEmail(profileForm.getEmail());
        if (profileForEmail != null && !profileForEmail.getId().equals(profileForm.getId())) {
            throw new FormValidationException("email",profileForm.getEmail(), "this email is already taken");
        }
        Profile profileForPhone = profileRepository.findByPhone(profileForm.getPhone());
        if (profileForPhone != null && !profileForPhone.getId().equals(profileForm.getId())) {
            throw new FormValidationException("phone",profileForm.getPhone() ,"this phone is already taken");
        }
        Profile profileForUid = profileRepository.findByUid(profileForm.getUid());
        if (profileForUid != null && !profileForUid.getId().equals(profileForm.getId())) {
            throw new FormValidationException("uid",profileForm.getUid(), "this uid is already taken");
        }

    }

    @Transactional
    public void updateContacts(UserPrincipal principal, Contacts contactsForm) {
        Profile loadedProfile = profileRepository.findById(principal.getId())
                .orElseThrow(()->new UsernameNotFoundException("user not found"));
        loadedProfile.setContacts(contactsForm);
        evictProfileCacheIfTransactionSuccess(loadedProfile);
        LOGGER.debug("Profile contacts updated");
    }

    @Transactional
    public void editSkills(Long id,List<Skill> skills){
        ArrayList<Skill> data = skills.stream()
                .collect(Collectors.collectingAndThen
                          (Collectors.toCollection
                                (() -> new TreeSet<>(Comparator.comparingInt(val -> val.getSkillCategory().getId()))), ArrayList::new));

        updateProfileEntity(data,Skill.class,id);
        LOGGER.debug("Profile skills updated");
    }

    @Transactional
    public void editPractic(Long id,List<Practic> practics){
          updateProfileEntity(practics,Practic.class,id);
    }

    @Transactional
    public void editCertificates(Long id,List<Certificate> certificates){
        List<Certificate> oldCertificates = getCertificate(id);
        List<String> oldCertificatesLinks = DataUtil.getCertificateImageUrls(oldCertificates);
        updateProfileEntity(certificates,Certificate.class,id);
        for (Certificate certificate : certificates) {
            oldCertificatesLinks.remove(certificate.getLargeUrl());
            oldCertificatesLinks.remove(certificate.getSmallUrl());
        }
        clearResourcesIfTransactionSucces(oldCertificatesLinks);
    }

    protected void clearResourcesIfTransactionSucces(List<String> links){
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                uploadCertificateLinkManager.clearImageLinks();
                fileImageStorageService.remove(links);
                LOGGER.info("Temp images {} removed", links);
            }
        });

    }

    protected void evictProfileCacheIfTransactionSuccess(final Profile profile){
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                cacheService.evictByUid(profile.getUid());
            }
        });
    }


    @Transactional
    public void editCourse(Long id,List<Course> courses){
        updateProfileEntity(courses,Course.class,id);
    }

    @Transactional
    public void editEducation(Long id,List<Education> educations){
        updateProfileEntity(educations,Education.class,id);
    }

    @Transactional
    public void editLanguage(Long id,List<Language> languages){
        updateProfileEntity(languages,Language.class,id);
    }

    @Transactional
    public void editHobby(Long id,List<String> hobbyNames){

        List<Hobby> hobbies = hobbyNames.stream().map(Hobby::new).collect(Collectors.toList());
        updateProfileEntity(hobbies,Hobby.class,id);
    }

    @Transactional
    public void editInfo(Long id,String info){
        Profile profile = profileRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException("wrong id"+id));
        profile.setInfo(info);
        evictProfileCacheIfTransactionSuccess(profile);
        LOGGER.debug("Profile info updated");
    }

    @Transactional
    public Profile updateProfilePassword(Long id, PasswordForm passwordForm) {
        Profile profile = profileRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("wrong id"+id));
        profile.setPassword(passwordEncoder.encode(passwordForm.getPassword()));
        return profile;
    }

    protected <T extends ProfileEntity> void updateProfileEntity(List<T> data,Class<T> entityClass,Long id){
        AbstractProfileRepository<T> entityRepository = getEntityRepository(entityClass);
        executeUpdate(entityRepository,data,id);
    }

    protected <T extends ProfileEntity> AbstractProfileRepository<T> getEntityRepository(Class<T> entityClass){
        AbstractProfileRepository<T> repository = (AbstractProfileRepository<T>)profileEntityRepositoryMap.get(entityClass);
        if(repository == null) {
            throw new IllegalArgumentException("ProfileEntityRepository not found for entityClass="+entityClass);
        }
        return repository;
    }


    protected <T extends ProfileEntity> void executeUpdate(AbstractProfileRepository<T> repository,List<T> data,Long id){
        repository.deleteByProfileId(id);

        Profile profile = profileRepository.getOne(id);
        for (T entity : data) {
            entity.setId(null);
            entity.setProfile(profile);
        }
        repository.saveAll(data);
        updateIndexProfileDataIfTransactionSuccess(profile);
        evictProfileCacheIfTransactionSuccess(profile);
    }

    @Transactional
    public void removeProfile(Long id){
        Profile profile = profileRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException("user not found"));
        List<String> imagesLink = getImagesLinks(profile);
        profileRepository.delete(profile);
        removeProfileDataIfTransactionSucces(profile,imagesLink);
        evictProfileCacheIfTransactionSuccess(profile);
        LOGGER.info("Profile {} removed", profile.getUid());
    }

    protected void removeProfileDataIfTransactionSucces(final Profile profile,List<String> links){
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                LOGGER.info("Profile by id=" + profile.getId() + " removed from storage");
                profileSearchRepository.delete(profile);
                fileImageStorageService.remove(links);
                LOGGER.info("Profile by id=" + profile.getId() + " removed from search index");
            }
        });
    }

    private List<String> getImagesLinks(Profile profile) {
        List<String> links = new ArrayList<>();
        links.add(profile.getSmallPhoto());
        links.add(profile.getLargePhoto());
       if (profile.getCertificates()!=null){
           for (Certificate certificate : profile.getCertificates()) {
               links.add(certificate.getSmallUrl());
               links.add(certificate.getLargeUrl());
           }
       }
        return links;
    }

    public List<Skill> getSkill(Long id){
        return skillRepository.findByProfileId(id);
    }

    public List<Practic> getPractic(Long id){
        return practicRepository.findByProfileId(id);
    }

    public List<Course> getCourse(Long id){
        return courseRepository.findByProfileId(id);
    }

    public List<Certificate> getCertificate(Long id){
        return certificateRepository.findByProfileId(id);
    }

    public List<Education> getEducation(Long id){
        return educationRepository.findByProfileId(id);
    }


    public List<Language> getLanguage(Long id){
        return languageRepository.findByProfileId(id);
    }

    public Contacts getContacts(Long id){
        Profile profile = profileRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException("User with id = " + id + "not found"));
        return profile.getContacts();
    }

    public List<Hobby> listHobbiesWithProfileSelected(Long id) {
        List<Hobby> profileHobbies = hobbyRepository.findByProfileId(id);
        List<Hobby> hobbies = new ArrayList<>();
        for (Hobby h : staticDataService.listAllHobbies()) {
            boolean selected = profileHobbies.contains(h);
            hobbies.add(new Hobby(h.getName(), selected));
        }
        return hobbies;
    }
}
