package net.study.resume.component;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Scope(scopeName = "session")
public class SessionRecaptchaLoginFailuresCounter {
    protected final ConcurrentMap<String, Integer> loginFailures = new ConcurrentHashMap();

    public void addLoginFailure(String username) {
        loginFailures.compute(username, (name, count) -> count == null ? 1 : count + 1);
    }

    public int getLoginFailuresCount(String username) {
        return this.loginFailures.getOrDefault(username, 0);
    }

    public void clearLoginFailures(String username) {
        loginFailures.remove(username);
    }

    public int getMapMaxEntry() {
        if(loginFailures.size()==0){
            return 0;
        }
        Map.Entry<String, Integer> maxEntry = Collections.max(loginFailures.entrySet(), Map.Entry.comparingByValue());
        return maxEntry.getValue();
    }

    public ConcurrentMap<String, Integer> getLoginFailuresMap(){
        return loginFailures;
    }
}
