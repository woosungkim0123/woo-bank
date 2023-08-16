package shop.woosung.bank.config.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import shop.woosung.bank.common.handler.CommonResponseHandler;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.config.auth.dto.LoginRequestDto;
import shop.woosung.bank.config.auth.dto.LoginResponseDto;
import shop.woosung.bank.config.jwt.JwtTokenProvider;
import shop.woosung.bank.config.jwt.JwtProcess;
import shop.woosung.bank.config.jwt.JwtVO;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login");
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            ObjectMapper om = new ObjectMapper();
            LoginRequestDto loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException exception) {
            log.error("request.getRequestURI = {}", request.getRequestURI());
            log.error("IOException = {}", exception.getMessage());
            CommonResponseHandler.handleException(response, "로그인에 실패 하였습니다.", HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("AuthenticationException = {}", exception.getMessage());
        CommonResponseHandler.handleException(response, "계정 정보를 확인해주세요.", HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        LoginUser loginUser = (LoginUser) authResult.getPrincipal();
        String jwtToken = JwtProcess.create(jwtTokenProvider, loginUser);
        response.addHeader(JwtVO.HEADER, jwtToken);
        LoginResponseDto loginResponseDto = new LoginResponseDto(loginUser.getUser());
        CommonResponseHandler.handleSuccess(response, "로그인 완료", HttpStatus.OK, loginResponseDto);
    }
}
