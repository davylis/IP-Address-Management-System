import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.ip_management_system.controllers.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                                        .requestMatchers( "/css/**", "/login").permitAll()
                                        .anyRequest().authenticated()) //require authentication
                                        .formLogin(formlogin -> formlogin
                                        .loginPage("/login")
                                        .defaultSuccessUrl("/ippools", true)
                                        .permitAll())//allow all to see login page
                                    .logout(logout -> logout
                                                .permitAll()); //allow everyone to see logout page
                                            return http.build();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
 
        auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder());
     
    }
    
}
