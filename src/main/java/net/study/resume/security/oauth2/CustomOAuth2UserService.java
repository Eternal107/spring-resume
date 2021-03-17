package net.study.resume.security.oauth2;

import lombok.SneakyThrows;
import net.study.resume.entity.Profile;
import net.study.resume.exception.OAuth2AuthenticationProcessingException;
import net.study.resume.security.UserPrincipal;
import net.study.resume.security.oauth2.user.OAuth2UserInfo;
import net.study.resume.security.oauth2.user.OAuth2UserInfoFactory;
import net.study.resume.service.EditProfileService;
import net.study.resume.service.ProfileService;
import net.study.resume.util.DataUtil;
import net.study.resume.util.MultipartImage;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.hibernate.result.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private EditProfileService editProfileService;

    @Value("${generate.password.alphabet}")
    private String generatePasswordAlphabet;

    @Value("${generate.password.length}")
    private int generatePasswordLength;



    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from "+oAuth2UserRequest.getClientRegistration().getRegistrationId());
        }

        Profile profile = profileService.findByEmail(oAuth2UserInfo.getEmail());
        if(profile==null) {
            profile = registerProfile(oAuth2UserInfo);
        }else
        {
            updateProfile(profile,oAuth2UserInfo);
        }

        return UserPrincipal.create(profile, oAuth2User.getAttributes());
    }

    protected Profile updateProfile(Profile profile,OAuth2UserInfo oAuth2UserInfo) {
        uploadPhotoFromProvider(profile,oAuth2UserInfo);
        return profile;
    }


    @SneakyThrows
    private void uploadPhotoFromProvider(Profile profile, OAuth2UserInfo oAuth2UserInfo){
        if(profile.getProfilePhoto() == null){
            URL imageUrl = new URL(oAuth2UserInfo.getImageUrl());

            BufferedImage image = ImageIO.read(imageUrl);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image,"jpg",byteArrayOutputStream);
            byteArrayOutputStream.flush();

            MultipartFile multipartFile = new MultipartImage(byteArrayOutputStream.toByteArray(),"image/jpg");
            editProfileService.updateProfilePhoto(profile,multipartFile);

            byteArrayOutputStream.close(); // Close once it is done saving
        }
    }

    private Profile registerProfile(OAuth2UserInfo oAuth2UserInfo) {
        Profile profile = new Profile();
        profile.setFirstName(oAuth2UserInfo.getFirstName());
        profile.setLastName(oAuth2UserInfo.getLastName());
        profile.setEmail(oAuth2UserInfo.getEmail());
        profile.setPassword(DataUtil.generateRandomString(generatePasswordAlphabet,generatePasswordLength));
        profileService.register(profile);
        return profile;
    }

}
