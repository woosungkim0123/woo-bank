package shop.woosung.bank.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import shop.woosung.bank.domain.user.UserEnum;

// @Slf4j junit 테스트할 때 문제가 생김
@Configuration
public class SecurityConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("디버그 : BCryptPasswordEncoder 빈 등록됨");
        return new BCryptPasswordEncoder();
    }

    // JWT 필터 등록이 필요함


    // 공식문서에 나온거 고대로 따라 치는 것
    // JWT 서버, 세션 사용 안함
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("디버그 : SecurityFilterChain 빈 등록됨");
        http.headers().frameOptions().disable(); // iframe 허용 안함
        http.csrf().disable(); // csrf가 걸려있으면 post맨이 작동을 안함 (메타코딩 유튜브에 시큐리티 강의)
        http.cors().configurationSource(configurationSource()); // js요청을 거부하는 것 cross orgin resource sharing 다른 서버에 있는 프로그램 중에 js로 컨트롤러 쪽으로 요청하는건 다막겠다
        // 이걸 조금 허용ㅎ줘야함 일단 null로 주고 나중에 설정 1
        // configurationSource로 설정 변경 2
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // jSessionId를 서버쪽에서 관리 안하겠다는 뜻
        // 폼태그로 로그인하는건 안할거임. react나 앱이나 이런쪽에서 요청하는것을 받을것이기때문에 그림을 우리가 안그림
        http.formLogin().disable();

        // httpBasic은 브라우저가 팝업창을 이용해서 사용자 인증을 진행한다.
        http.httpBasic().disable();
        // 주소에 s가 들어오면 인증이 필요하고
        // admin에 들어올 수 있는 값이 String
        // 예전에는 "ROLE_" + UserEnum.Admin 이렇게했는데 ROLE이 디폴트
        http.authorizeHttpRequests()
                .antMatchers("/api/s/**").authenticated()
                .antMatchers("/api/admin/**").hasRole("" + UserEnum.ADMIN)
                .anyRequest().permitAll(); // 나머지 요청은 다 허용

        // jwt, filter, 오류제어도 해야함
        // 이게 강사가 생각하는 시큐리티의 기본 값

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        log.debug("디버그 : configurationSource cors 설정이 SecurityFilterChain에 등록됨");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*"); // 모든 헤더를 다받음
        configuration.addAllowedMethod("*"); // 모든 메소드를 다받음 (GET, POST, PUT, DELETE (Javascript 요청 허용))
        configuration.addAllowedOriginPattern("*"); // 모든 주소 허용 -> 모든 IP주소 허용 (프론트 엔드 쪽 ip만 허용) 핸드폰앱은 모든 사용자가 다른 ip를 사용해서 허용하고 안하고가 의미가없고 휴대폰앱은 javascript로 요청하는 것이 아니라
        // cors에 걸리지 않음
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 주소에 대해서 위에 설정한 configuration을 적용하겠다
        return source;



    }
}
