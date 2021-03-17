package net.study.resume;

import lombok.SneakyThrows;
import net.study.resume.repository.storage.ProfileRepository;
import net.study.resume.validator.AdulthoodConstraintValidator;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.rmi.server.UID;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest()
@Configuration
@ComponentScan(lazyInit = true)
class ResumeApplicationTests {

   @Autowired
   AdulthoodConstraintValidator validator;

    @Autowired
    ProfileRepository profileRepository;

	@Test
	void contextLoads() {
      Date date =new GregorianCalendar(2000, Calendar.FEBRUARY, 11).getTime();

        System.out.println("kek");
      assertTrue (validator.isValid(date,null));

	}



}
