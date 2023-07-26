package com.example.demo.config;

import com.example.demo.security.jwt.JwtEntryPoint;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.security.jwt.JwtTokenFilter;
import com.example.demo.security.userprincal.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailService userDetailService;
    @Autowired
    private JwtEntryPoint jwtEntryPoint;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().
                and().csrf().disable().
                authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/signIn").permitAll()
                .antMatchers("/auth/admin**").hasAnyAuthority("ADMIN")
                .antMatchers("/doctors**").hasAnyAuthority("DOCTOR","ADMIN","USER")
                .antMatchers("/bookings**").hasAnyAuthority("DOCTOR","ADMIN","USER")
                .antMatchers("/specialty**").hasAnyAuthority("DOCTOR","ADMIN")
                .antMatchers("/api**").hasAnyAuthority("DOCTOR","ADMIN","USER")
//                .anyRequest().authenticated()
                .and().exceptionHandling()
                .authenticationEntryPoint(jwtEntryPoint)
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        httpSecurity.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
