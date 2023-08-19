package shop.woosung.bank.config.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import shop.woosung.bank.common.handler.CommonResponseHandler;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.config.auth.jwt.JwtTokenProvider;
import shop.woosung.bank.config.auth.jwt.JwtProcess;
import shop.woosung.bank.config.auth.jwt.JwtVO;
import shop.woosung.bank.config.auth.jwt.exception.JwtIdConversionException;
import shop.woosung.bank.config.auth.jwt.exception.JwtNotFoundUser;
import shop.woosung.bank.config.auth.jwt.exception.JwtNotHaveIdException;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.service.port.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        try {
            if(isHeaderVerify(request)) {
                String token = request.getHeader(JwtVO.HEADER).replace(JwtVO.TOKEN_PREFIX, "");
                Long userId = JwtProcess.verify(jwtTokenProvider, token);
                LoginUser loginUser = getLoginUser(userId);
                Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            chain.doFilter(request, response);

        } catch (TokenExpiredException exception) {
            log.error("token expired = {}", exception.getMessage());
            CommonResponseHandler.handleException(response, "토큰이 만료 되었습니다.", HttpStatus.UNAUTHORIZED);
        } catch (JWTVerificationException exception) {
            log.error("JWTVerificationException = {}", exception.getMessage());
            CommonResponseHandler.handleException(response, "토큰 검증에 실패 했습니다.", HttpStatus.UNAUTHORIZED);
        } catch (JwtNotHaveIdException exception) {
            log.error("JwtNotHaveIdException = {}", exception.getMessage());
            CommonResponseHandler.handleException(response, "토큰 검증에 실패 했습니다.", HttpStatus.UNAUTHORIZED);
        } catch (JwtIdConversionException exception) {
            log.error("JwtIdConversionException = {}", exception.getMessage());
            CommonResponseHandler.handleException(response, "토큰 검증에 실패 했습니다.", HttpStatus.UNAUTHORIZED);
        } catch (JwtNotFoundUser exception) {
            log.error("JwtNotFoundUser = {}", exception.getMessage());
            CommonResponseHandler.handleException(response, "토큰 검증에 실패 했습니다.", HttpStatus.UNAUTHORIZED);
        } catch (IOException | ServletException exception) {
            log.error("IOException | ServletException = {}", exception.getMessage());
            CommonResponseHandler.handleException(response, "서버 오류가 발생 했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        }
    }

    private LoginUser getLoginUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(JwtNotFoundUser::new);
        return new LoginUser(user);
    }

    private boolean isHeaderVerify(HttpServletRequest request) {
        String header = request.getHeader(JwtVO.HEADER);
        return header != null && !header.isEmpty();
    }
}