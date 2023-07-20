package shop.woosung.bank.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import shop.woosung.bank.config.jwt.JwtAuthenticationFilter;
import shop.woosung.bank.config.jwt.JwtAuthorizationFilter;
import shop.woosung.bank.config.jwt.JwtHolder;
import shop.woosung.bank.domain.user.UserEnum;

import shop.woosung.bank.util.CustomResponseUtil;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtHolder jwtHolder;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("디버그 : BCryptPasswordEncoder 빈 등록됨");
        return new BCryptPasswordEncoder();
    }

    // TODO2 : 권한 에러처리 필요
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.csrf().disable();
        http.cors().configurationSource(configurationSource());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.formLogin().disable();
        http.httpBasic().disable();

        http.apply(new CustomSecurityFilterManager());


        http.exceptionHandling()
                .authenticationEntryPoint((req, res, e) -> CustomResponseUtil.fail(res, "로그인을 진행 해주세요", HttpStatus.UNAUTHORIZED))
                .accessDeniedHandler((req, res, e) -> CustomResponseUtil.fail(res, "권한이 없습니다", HttpStatus.FORBIDDEN));

        http.authorizeHttpRequests()
                .antMatchers("/api/s/**").authenticated()
                .antMatchers("/api/admin/**").hasRole("" + UserEnum.ADMIN)
                .anyRequest().permitAll();

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = getBuilder().getSharedObject(AuthenticationManager.class);
            getBuilder().addFilter(new JwtAuthenticationFilter(jwtHolder, authenticationManager));
            getBuilder().addFilter(new JwtAuthorizationFilter(jwtHolder, authenticationManager));
            super.configure(http);
        }
    }
}
