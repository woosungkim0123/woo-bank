package shop.woosung.bank.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import shop.woosung.bank.common.handler.CommonResponseHandler;
import shop.woosung.bank.config.filter.JwtAuthenticationFilter;
import shop.woosung.bank.config.filter.JwtAuthorizationFilter;
import shop.woosung.bank.config.auth.jwt.JwtTokenProvider;
import shop.woosung.bank.user.domain.UserRole;
import shop.woosung.bank.user.service.port.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final Environment environment;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final CommonResponseHandler commonResponseHandler;

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
                    .exceptionHandling(config -> {
                        config.authenticationEntryPoint(this::authenticationEntryPointResponseHandler);
                        config.accessDeniedHandler(this::accessDeniedResponseHandler);
                    })
                    .apply(new CustomSecurityFilterManager()).and()
                    .build();
    }

    private class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = getBuilder().getSharedObject(AuthenticationManager.class);
            getBuilder().addFilter(new JwtAuthenticationFilter(jwtTokenProvider, authenticationManager, commonResponseHandler));
            getBuilder().addFilter(new JwtAuthorizationFilter(jwtTokenProvider, authenticationManager, userRepository, commonResponseHandler));
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

    private void authenticationEntryPointResponseHandler(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("AuthenticationException = {}", exception.getMessage());
        commonResponseHandler.handleException(response, "로그인 필요", HttpStatus.UNAUTHORIZED);
    }

    private void accessDeniedResponseHandler(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("AccessDeniedException = {}", exception.getMessage());
        commonResponseHandler.handleException(response, "권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    private void configureDevSettings(HttpSecurity http) throws Exception {
        if (isDevProfileActive() || isTestProfileActive()) {
            http
                .headers(config -> config.frameOptions().disable())
                .csrf(AbstractHttpConfigurer::disable);
        }
    }

    private boolean isDevProfileActive() {
        return Arrays.asList(environment.getActiveProfiles()).contains("dev");
    }
    private boolean isTestProfileActive() {
        return Arrays.asList(environment.getActiveProfiles()).contains("test");
    }
}
