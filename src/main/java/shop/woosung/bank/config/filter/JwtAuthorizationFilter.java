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
import shop.woosung.bank.config.jwt.JwtTokenProvider;
import shop.woosung.bank.config.jwt.JwtProcess;
import shop.woosung.bank.config.jwt.JwtVO;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        try {
            if(isHeaderVerify(request)) {
                String token = request.getHeader(JwtVO.HEADER).replace(JwtVO.TOKEN_PREFIX, "");
                LoginUser loginUser = JwtProcess.verify(jwtTokenProvider, token);
                Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            chain.doFilter(request, response);
        } catch (TokenExpiredException exception) {
            log.error("request.getRequestURI() = {}, ", request.getRequestURI());
            log.error("token expired = {}", exception.getMessage());
            CommonResponseHandler.handleException(response, "토큰이 만료 되었습니다.", HttpStatus.UNAUTHORIZED);
        } catch (JWTVerificationException exception) {
            log.error("request.getRequestURI() = {}, ", request.getRequestURI());
            log.error("token fail = {}", exception.getMessage());
            CommonResponseHandler.handleException(response, "토큰 검증에 실패 했습니다.", HttpStatus.UNAUTHORIZED);
        } catch (IOException | ServletException exception) {
            log.error("request.getRequestURI() = {}, ", request.getRequestURI());
            log.error("IOException | ServletException = {}", exception.getMessage());
            CommonResponseHandler.handleException(response, "서버 오류가 발생 했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isHeaderVerify(HttpServletRequest request) {
        String header = request.getHeader(JwtVO.HEADER);
        return header != null;
    }
}
