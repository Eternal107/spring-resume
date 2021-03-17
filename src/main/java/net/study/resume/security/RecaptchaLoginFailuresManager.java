package net.study.resume.security;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresManager;
import net.study.resume.component.SessionRecaptchaLoginFailuresCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class RecaptchaLoginFailuresManager extends LoginFailuresManager {
    @Autowired
    ApplicationContext applicationContext;

    public RecaptchaLoginFailuresManager(RecaptchaProperties recaptcha) {
        super(recaptcha);
    }

    public void addLoginFailure(HttpServletRequest request) {
        String username = this.getUsername(request);
        getFailuresCounter().addLoginFailure(username);
    }

    public int getLoginFailuresCount(HttpServletRequest request) {
        String username = this.getUsername(request);
        return getFailuresCounter().getLoginFailuresCount(username);
    }

    public void clearLoginFailures(HttpServletRequest request) {
        String username = this.getUsername(request);
        getFailuresCounter().clearLoginFailures(username);
    }

    @Override
    public boolean isRecaptchaRequired(HttpServletRequest request) {
        int mapMaxEntry = getFailuresCounter().getMapMaxEntry();
        return mapMaxEntry >= this.security.getLoginFailuresThreshold();
    }



    public SessionRecaptchaLoginFailuresCounter getFailuresCounter(){
        return applicationContext.getBean("sessionRecaptchaLoginFailuresCounter", SessionRecaptchaLoginFailuresCounter.class);
    }
}
