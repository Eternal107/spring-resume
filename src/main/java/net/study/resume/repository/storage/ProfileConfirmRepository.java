package net.study.resume.repository.storage;

import net.study.resume.entity.ProfileConfirm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

public interface ProfileConfirmRepository extends JpaRepository<ProfileConfirm,Long> {

    ProfileConfirm findByToken(String token);

    ProfileConfirm findByProfileId(Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from ProfileConfirm token where token.created < ?1")
    int deleteNotCompleted(Timestamp expired);
}
