package net.study.resume.service;

import net.study.resume.repository.storage.PracticRepository;
import net.study.resume.repository.storage.ProfileConfirmRepository;
import net.study.resume.repository.storage.ProfileRepository;
import net.study.resume.repository.storage.ProfileRestoreRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ScheduledTaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTaskService.class);

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileRestoreRepository profileRestoreRepository;

    @Autowired
    private ProfileConfirmRepository profileConfirmRepository;

    @Value("${remove.not.completed.profiles.daysInterval}")
    private int removeNotCompletedProfileDaysInterval;

    @Value("${remove.expired.restoreToken.hoursInterval}")
    private int removeExpiredRestoreTokenHoursInterval;

    @Value("${remove.expired.confirmationToken.hoursInterval}")
    private int removeExpiredConfirmationTokenHoursInterval;

    @Scheduled(cron = "0 59 23 * * *")
    @Transactional
    public void removeNotComletedProfile(){
        DateTime current = DateTime.now().minusDays(removeNotCompletedProfileDaysInterval);
        int removed = profileRepository.deleteNotCompleted(new Timestamp(current.getMillis()));
        LOGGER.info("Removed {} profiles", removed);
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void removeExpiredRestoreToken(){
        DateTime current = DateTime.now().minusHours(removeExpiredRestoreTokenHoursInterval);
        int removed = profileRestoreRepository.deleteNotCompleted(new Timestamp(current.getMillis()));
        LOGGER.info("Removed {} restore tokens", removed);
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void removeExpiredConfirmationToken(){
        DateTime current = DateTime.now().minusHours(removeExpiredConfirmationTokenHoursInterval);
        int removed = profileConfirmRepository.deleteNotCompleted(new Timestamp(current.getMillis()));
        LOGGER.info("Removed {} confirmation tokens", removed);
    }
}
