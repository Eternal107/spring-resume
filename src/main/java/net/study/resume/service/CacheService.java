package net.study.resume.service;

import net.study.resume.entity.Profile;
import net.study.resume.repository.storage.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;


@Component
public class CacheService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheService.class);

    @Autowired
    private ProfileRepository profileRepository;


    @Cacheable({"profiles"})
    public Profile findByUid(String uid){
        return profileRepository.findByUid(uid);
    }

    @CacheEvict({"profiles"})
    public String evictByUid(String uid){
        LOGGER.debug("Profile removed from cache by uid={}", uid);
        return uid;
    }
}
