package net.study.resume.config;

import com.github.mkopylec.recaptcha.security.login.FormLoginConfigurerEnhancer;
import net.study.resume.security.oauth2.CustomOAuth2UserService;
import net.study.resume.security.oauth2.handlers.FailureAuthenticationHandler;
import net.study.resume.security.oauth2.handlers.OAuth2SuccesHandler;
import net.study.resume.service.AuthenticationService;
import net.study.resume.service.RememberMeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    @Autowired
    private OAuth2SuccesHandler oAuth2SuccesHandler;
    @Autowired
    private FailureAuthenticationHandler failureAuthenticationHandler;
    @Autowired
    private RememberMeService persistentTokenRememberMeService;
    @Autowired
    private FormLoginConfigurerEnhancer enhancer;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers( "/edit", "/edit/**", "/remove").hasAuthority("USER")
                .anyRequest()
                .permitAll();

        enhancer.addRecaptchaSupport(http.formLogin())
                .loginPage("/sign-in")
                .usernameParameter("uid")
                .loginProcessingUrl("/sign-in-handler")
                .failureHandler(failureAuthenticationHandler)
                .and()
                      .logout()
                      .logoutSuccessUrl("/home")
                      .invalidateHttpSession(true)
                      .deleteCookies("JSESSIONID")
                .and()
                      .oauth2Login()
                      .loginPage("/sign-in")
                      .authorizationEndpoint()
                      .baseUri("/oauth2/authorize")
                .and()
                      .redirectionEndpoint()
                      .baseUri("/oauth2/callback/*")
                .and()
                      .userInfoEndpoint()
                      .userService(customOAuth2UserService)
                .and()
                      .successHandler(oAuth2SuccesHandler)
                      .failureUrl("/sign-in?error")
                .and()
                      .rememberMe()
                      .rememberMeParameter("remember-me")
                      .rememberMeServices(persistentTokenRememberMeService);
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(authenticationService); //set the custom user details service
        auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
        return auth;
    }


}
