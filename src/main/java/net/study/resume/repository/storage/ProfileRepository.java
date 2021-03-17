package net.study.resume.repository.storage;

import com.nimbusds.jose.crypto.impl.PRFParams;
import net.study.resume.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Optional;


public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByUidOrEmailOrPhone(String uid, String email, String phone);

    Profile findByEmail(String email);

    Profile findByUid(String Uid);

    Page<Profile> findAllByCompleted(boolean completed,Pageable pageable);

    Profile findByPhone(String phone);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Profile p where p.completed=false and p.created < ?1")
    int deleteNotCompleted(Timestamp oldDate);

    Page<Profile> findByObjectiveLikeOrSummaryLikeOrInfoLikeOrCertificatesNameLikeOrLanguagesNameLikeOrPracticsCompanyLikeOrPracticsPositionLikeOrPracticsResponsibilitiesLikeOrSkillsValueLike(
            String objective, String info, String summary, String certificateName, String languageName, String practicCompany,
            String practicPosition, String practicResponsibility, String skillValue, Pageable pageable);
}
