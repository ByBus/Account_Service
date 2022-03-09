package account.auth;

import account.security.UserAuthenticationEntryPoint;
import account.buiseness.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@EnableWebSecurity
public class WebSecurityConfigurator extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsService userinfoService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userinfoService) // user store 1
                .passwordEncoder(getEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/api/auth/changepass").hasAnyRole(
                        Role.ADMINISTRATOR.name(),
                        Role.ACCOUNTANT.name(),
                        Role.USER.name()
                )
                .mvcMatchers("/api/empl/payment").hasAnyRole(Role.ACCOUNTANT.name(), Role.USER.name())
                .mvcMatchers("/api/acct/payments").hasRole(Role.ACCOUNTANT.name())
                .mvcMatchers("/api/admin/**").hasRole(Role.ADMINISTRATOR.name())
                .mvcMatchers("/api/security/**").hasRole(Role.AUDITOR.name())
                .mvcMatchers("/api/auth/signup", "/actuator/shutdown").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                .csrf().disable().headers().frameOptions().disable() // disabling CSRF will allow sending POST request using Postman
                .and()
                .httpBasic()
                .authenticationEntryPoint(getEntryPoint()); // entry for Locked Users to get 401 with body!
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/h2-console/**");
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(13);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint getEntryPoint() {
        return new UserAuthenticationEntryPoint();
    }
}