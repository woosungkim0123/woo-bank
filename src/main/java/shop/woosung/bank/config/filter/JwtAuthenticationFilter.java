package shop.woosung.bank.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import shop.woosung.bank.common.handler.CommonResponseHandler;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.config.auth.dto.LoginRequestDto;
import shop.woosung.bank.config.auth.dto.LoginResponseDto;
import shop.woosung.bank.config.auth.jwt.JwtTokenProvider;
import shop.woosung.bank.config.auth.jwt.JwtProcess;
import shop.woosung.bank.config.auth.jwt.JwtVO;
import shop.woosung.bank.config.auth.jwt.exception.LoginValidationException;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CommonResponseHandler commonResponseHandler;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, CommonResponseHandler commonResponseHandler) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login");
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.commonResponseHandler = commonResponseHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginRequestDto loginRequestDto = convertLoginRequestDto(request);
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (LoginValidationException exception) {
            log.error("request.getRequestURI = {}", request.getRequestURI());
            log.error("LoginValidationException = {}", exception.getMessage());
            commonResponseHandler.handleException(response, "유효하지 않은 요청 입니다.", HttpStatus.BAD_REQUEST);
        } catch (IOException exception) {
            log.error("request.getRequestURI = {}", request.getRequestURI());
            log.error("IOException = {}", exception.getMessage());
            commonResponseHandler.handleException(response, "로그인에 실패 하였습니다.", HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("AuthenticationException = {}", exception.getMessage());
        commonResponseHandler.handleException(response, "계정 정보를 확인 해주세요.", HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        LoginUser loginUser = (LoginUser) authResult.getPrincipal();
        String jwtToken = JwtProcess.create(jwtTokenProvider, loginUser);
        response.addHeader(JwtVO.HEADER, jwtToken);
        LoginResponseDto loginResponseDto = LoginResponseDto.builder().user(loginUser.getUser()).build();
        commonResponseHandler.handleSuccess(response, "로그인 완료", HttpStatus.OK, loginResponseDto);
    }

    private LoginRequestDto convertLoginRequestDto(HttpServletRequest request) throws IOException {
        ObjectMapper om = new ObjectMapper();
        LoginRequestDto loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
        validateLoginRequestDto(loginRequestDto);
        return loginRequestDto;
    }

    private void validateLoginRequestDto(LoginRequestDto loginRequestDto) {
        validateNonEmpty(loginRequestDto.getEmail(), "이메일을 입력해주세요.");
        validateWhitespace(loginRequestDto.getEmail(), "인증실패 - request email : " + loginRequestDto.getEmail());
        validateNonEmpty(loginRequestDto.getPassword(), "비밀번호를 입력해주세요.");
    }

    private void validateNonEmpty(String value, String errorMessage) {
        if (value == null || value.isEmpty()) {
            throw new LoginValidationException(errorMessage);
        }
    }

    /**
     * Mysql에서는 공백을 제거하지 않고 저장하기 때문에, 공백이 있는 경우에는 인증에 실패하도록 한다.
     * h2 mysql 차이 때문에 추가한 로직
     */
    private void validateWhitespace(String value, String errorMessage) {
        String trimValue = value.trim();
        if (value.length() != trimValue.length()) {
            throw new InternalAuthenticationServiceException(errorMessage);
        }
    }
}
