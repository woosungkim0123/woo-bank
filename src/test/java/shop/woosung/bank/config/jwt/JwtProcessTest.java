package shop.woosung.bank.config.jwt;

import org.junit.jupiter.api.Test;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.config.auth.jwt.JwtProcess;
import shop.woosung.bank.config.auth.jwt.JwtVO;
import shop.woosung.bank.mock.util.FakeJwtTokenProvider;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.domain.UserRole;


import static org.assertj.core.api.Assertions.*;

class JwtProcessTest {
    
    @Test
    public void 토큰_생성_절차_테스트() {
        // given
        FakeJwtTokenProvider jwtTokenProvider = new FakeJwtTokenProvider();
        jwtTokenProvider.token = "abcdef";
        User user = User.builder()
                .id(1L).role(UserRole.ADMIN).build();
        LoginUser loginUser = new LoginUser(user);

        // when
        String jwtToken = JwtProcess.create(jwtTokenProvider, loginUser);
        
        // then
        assertThat(jwtToken).startsWith(JwtVO.TOKEN_PREFIX);
        assertThat(jwtTokenProvider.token).isEqualTo(jwtToken.replace(JwtVO.TOKEN_PREFIX, ""));
    }

    @Test
    public void 토큰_검증_절차_테스트() {
        // given
        FakeJwtTokenProvider jwtTokenProvider = new FakeJwtTokenProvider();
        jwtTokenProvider.token = "abcdef";
        jwtTokenProvider.userId = 1L;

        // when
        Long userId = JwtProcess.verify(jwtTokenProvider, jwtTokenProvider.token);

        // then
        assertThat(userId).isEqualTo(1L);
    }
}