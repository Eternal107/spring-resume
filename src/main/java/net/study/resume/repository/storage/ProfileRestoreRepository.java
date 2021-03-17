package net.study.resume.repository.storage;

import net.study.resume.entity.ProfileRestore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

public interface ProfileRestoreRepository extends JpaRepository<ProfileRestore,Long> {

    ProfileRestore findByToken(String token);

    ProfileRestore findByProfileId(Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from ProfileRestore token where token.created < ?1")
    int deleteNotCompleted(Timestamp expired);
}
