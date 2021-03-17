package net.study.resume.service;

import net.study.resume.entity.Contacts;
import net.study.resume.entity.Profile;
import net.study.resume.entity.ProfileConfirm;
import net.study.resume.entity.ProfileRestore;
import net.study.resume.exception.CantCompleteClientRequestException;
import net.study.resume.exception.EmailNotConfirmedException;
import net.study.resume.exception.FormValidationException;
import net.study.resume.form.SignUpForm;
import net.study.resume.model.UploadResult;
import net.study.resume.repository.search.ProfileSearchRepository;
import net.study.resume.repository.storage.ProfileConfirmRepository;
import net.study.resume.repository.storage.ProfileRepository;
import net.study.resume.repository.storage.ProfileRestoreRepository;
import net.study.resume.util.DataUtil;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    ProfileRestoreRepository profileRestoreRepository;

    @Autowired
    ProfileConfirmRepository profileConfirmRepository;

    @Autowired
    CacheService cacheService;

    @Autowired
    ProfileSearchRepository profileSearchRepository;

    @Autowired
    NotificationManagerService notificationManagerService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public Profile save(Profile profile){
        return profileRepository.save(profile);
    }

    public Profile findById(Long id){
        return profileRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException("User with id = " + id + "not found"));
    }

    public Page<Profile> findAllByCompleted(boolean completed,Pageable pageable){
        return profileRepository.findAllByCompleted(completed,pageable);
    }

    public Profile findByUid(String Uid){
        return cacheService.findByUid(Uid);
    }

    public void register(Profile profile){
        profile.setUid(DataUtil.generateNewToken());
        profile.setPassword(encoder.encode(profile.getPassword()));
        profileRepository.save(profile);
    }

    public Profile register(SignUpForm signUpForm){
        Profile profile = profileRepository.findByEmail(signUpForm.getEmail());
        if(profile!=null){
            if(!profile.isEnabled()){
                sentConfirmEmail(profile);
                throw new EmailNotConfirmedException("email-"+profile.getEmail());
            }else{
                throw new FormValidationException("email",signUpForm.getEmail(),"this email is already registered");
            }
        }
        profile = new Profile();
        profile.setUid(DataUtil.generateNewToken());
        profile.setEmail(signUpForm.getEmail());
        profile.setPassword(encoder.encode(signUpForm.getPassword()));
        profileRepository.save(profile);
        return profile;
    }

    public void restoreAccess(String anyUid){
        Profile profile = profileRepository.findByUidOrEmailOrPhone(anyUid,anyUid,anyUid)
                .orElseThrow(()->new UsernameNotFoundException("User with id = " + anyUid + "not found"));

        ProfileRestore restore = profileRestoreRepository.findByProfileId(profile.getId());
        if (restore == null) {
            restore = new ProfileRestore();
            restore.setId(profile.getId());
        String restoreToken = DataUtil.generateNewToken();
        restore.setToken(restoreToken);
        profileRestoreRepository.save(restore);
        }
        notificationManagerService.sendRestoreLink(restore.getToken(),profile.getEmail());
    }

    public void sentConfirmEmail(Profile profile){
        ProfileConfirm restore = profileConfirmRepository.findByProfileId(profile.getId());
        if (restore == null) {
            restore = new ProfileConfirm();
            restore.setId(profile.getId());
            String restoreToken = DataUtil.generateNewToken();
            restore.setToken(restoreToken);
            profileConfirmRepository.save(restore);
        }
        notificationManagerService.sendMailConfirmation(restore.getToken(),profile.getEmail());

    }

    public Page<Profile> findBySearchQuery(String query, Pageable pageable) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(query)
                        .field("firstName")
                        .field("lastName")
                        .field("objective")
                        .field("summary")
                        .field("info")
                        .field("certificates.name")
                        .field("languages.name")
                        .field("practics.company")
                        .field("practics.position")
                        .field("practics.responsibilities")
                        .field("skills.value")
                        .field("courses.name")
                        .field("courses.school")
                        .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
                        .fuzziness(Fuzziness.ONE)
                        .operator(Operator.AND))
                .withSort(SortBuilders.fieldSort("uid").order(SortOrder.DESC))
                .withPageable(pageable)
                .build();

        return profileSearchRepository.search(searchQuery);

    }

    public Profile findByRestoreToken(String token){
        ProfileRestore restore = profileRestoreRepository.findByToken(token);
        if (restore == null) {
            throw new CantCompleteClientRequestException("Invalid token = "+token);
        }
        profileRestoreRepository.delete(restore);
        return restore.getProfile();
    }

    public Profile findByConfirmationToken(String token){
        ProfileConfirm restore = profileConfirmRepository.findByToken(token);
        if (restore == null) {
            throw new CantCompleteClientRequestException("Invalid token = "+token);
        }
        profileConfirmRepository.delete(restore);
        return restore.getProfile();
    }

    public Page<Profile> findAll(Pageable pageable){
        return profileRepository.findAll(pageable);
    }

    public List<Profile> findAll(){
        return profileRepository.findAll();
    }

    public Profile findByEmail(String email){
        return profileRepository.findByEmail(email);
    }

}
