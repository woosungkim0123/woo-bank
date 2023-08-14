package shop.woosung.bank.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import shop.woosung.bank.config.filter.JwtAuthenticationFilter;
import shop.woosung.bank.config.filter.JwtAuthorizationFilter;
import shop.woosung.bank.config.jwt.JwtTokenProvider;
import shop.woosung.bank.user.domain.UserRole;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final Environment environment;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        configureDevSettings(http);

        return http
                    .cors(config -> config.configurationSource(configureCors()))
                    .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .formLogin(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .authorizeRequests(config -> config
                            .antMatchers("/api/s/**").authenticated()
                            .antMatchers("/api/admin/**").hasRole(UserRole.ADMIN.name())
                            .anyRequest().permitAll())
                // todo ->handler로못잡음->시큐리티에서잡자
//                    .exceptionHandling(config -> {
//
//                        config
//                                .authenticationEntryPoint((req, res, e) -> {
//                                    throw new AException("11");
//                                });
//                            }
//
//                    )
                    .apply(new CustomSecurityFilterManager()).and()
                    .build();
    }

    private class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = getBuilder().getSharedObject(AuthenticationManager.class);
            getBuilder().addFilter(new JwtAuthenticationFilter(jwtTokenProvider, authenticationManager));
            getBuilder().addFilter(new JwtAuthorizationFilter(jwtTokenProvider, authenticationManager));
            super.configure(http);
        }
    }

    private CorsConfigurationSource configureCors() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    private void configureDevSettings(HttpSecurity http) throws Exception {
        if (isDevProfileActive()) {
            http
                .headers(config -> config.frameOptions().disable())
                .csrf(AbstractHttpConfigurer::disable);
        }
    }

    private boolean isDevProfileActive() {
        return Arrays.asList(environment.getActiveProfiles()).contains("dev");
    }
}
