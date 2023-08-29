package shop.woosung.bank.config.filter;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.woosung.bank.config.auth.jwt.JwtVO;
import shop.woosung.bank.mock.util.FakeJwtTokenProvider;
import shop.woosung.bank.mock.repository.FakeUserRepository;
import shop.woosung.bank.mock.config.FakeJwtConfiguration;
import shop.woosung.bank.mock.config.FakeRepositoryConfiguration;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.domain.UserRole;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Import({ FakeJwtConfiguration.class, FakeRepositoryConfiguration.class })
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthorizationFilterTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private FakeJwtTokenProvider jwtTokenProvider;

    @Autowired
    private FakeUserRepository userRepository;

    private static final String correctToken = "abcdef";
    private static final String wrongToken = "abc";

    @BeforeEach
    public void init() {
        jwtTokenProvider.init();
        userRepository.deleteAll();
    }

    @ParameterizedTest()
    @MethodSource("allowedUsers")
    public void 올바른_토큰을_가진_유저가_권한에_맞는_요청시_성공한다(UserRole userRole, String allowUrl) throws Exception {
        // given
        User user = userRepository.save(User.builder().email("test@test.com").role(userRole).build());
        jwtTokenProvider.userId = user.getId();
        jwtTokenProvider.token = correctToken;
        String requestToken = JwtVO.TOKEN_PREFIX + correctToken;

        // when
        ResultActions resultActions = mvc.perform(get(allowUrl).header(JwtVO.HEADER, requestToken));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void 올바른_토큰을_가진_유저가_권한에_맞지_않는_요청시_예외를_던진다() throws Exception {
        // given
        User user = userRepository.save(User.builder().email("test@test.com").role(UserRole.CUSTOMER).build());
        jwtTokenProvider.userId = user.getId();
        jwtTokenProvider.token = correctToken;
        String requestToken = JwtVO.TOKEN_PREFIX + correctToken;

        // when
        ResultActions resultActions = mvc.perform(get("/api/admin/test").header(JwtVO.HEADER, requestToken));

        // then
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    public void 토큰이_올바르지_않으면_예외가_던져진다() throws Exception {
        // given
        jwtTokenProvider.token = correctToken;
        String requestToken = JwtVO.TOKEN_PREFIX + wrongToken;

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/test").header(JwtVO.HEADER, requestToken));

        // then
        resultActions.andExpect(status().isUnauthorized());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("토큰 검증에 실패 했습니다."));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    public void 토큰이_만료일자를_지나지않으면_정상적으로_응답한다() throws Exception {
        // given
        User user = userRepository.save(User.builder().email("test@test.com").role(UserRole.CUSTOMER).build());
        jwtTokenProvider.token = correctToken;
        jwtTokenProvider.userId = user.getId();
        jwtTokenProvider.currentAt = LocalDateTime.of(2023, 8, 20, 14, 30, 0);
        jwtTokenProvider.expiredAt = LocalDateTime.of(2023, 8, 20, 14, 30, 1);
        String requestToken = JwtVO.TOKEN_PREFIX + correctToken;

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/test").header(JwtVO.HEADER, requestToken));

        // then
        resultActions.andExpect(status().isNotFound());
    }
    
    @Test
    public void 토큰이_만료일자를_지나면_예외를_던진다() throws Exception {
        // given
        jwtTokenProvider.token = correctToken;
        jwtTokenProvider.currentAt = LocalDateTime.of(2023, 8, 20, 14, 30, 0);
        jwtTokenProvider.expiredAt = LocalDateTime.of(2023, 8, 20, 14, 29, 59);
        String requestToken = JwtVO.TOKEN_PREFIX + correctToken;

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/test").header(JwtVO.HEADER, requestToken));

        // then
        resultActions.andExpect(status().isUnauthorized());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("토큰이 만료 되었습니다."));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void 토큰에서_가져온_값에서_키에_id가_없으면_예외를_던진다() throws Exception {
        // given
        jwtTokenProvider.token = correctToken;
        jwtTokenProvider.error = "id";
        String requestToken = JwtVO.TOKEN_PREFIX + correctToken;

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/test").header(JwtVO.HEADER, requestToken));

        // then
        resultActions.andExpect(status().isUnauthorized());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("토큰 검증에 실패 했습니다."));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void 토큰에서_가져온_값에서_id_타입이_Long이_아니면_예외를_던진다() throws Exception {
        // given
        jwtTokenProvider.token = correctToken;
        jwtTokenProvider.error = "idType";
        String requestToken = JwtVO.TOKEN_PREFIX + correctToken;

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/test").header(JwtVO.HEADER, requestToken));

        // then
        resultActions.andExpect(status().isUnauthorized());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("토큰 검증에 실패 했습니다."));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void 토큰에서_가져온_id값이_DB에_존재하지_않는_유저이면_예외를_던진다() throws Exception {
        // given
        jwtTokenProvider.token = correctToken;
        jwtTokenProvider.userId = 100L;
        String requestToken = JwtVO.TOKEN_PREFIX + correctToken;

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/test").header(JwtVO.HEADER, requestToken));

        // then
        resultActions.andExpect(status().isUnauthorized());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("토큰 검증에 실패 했습니다."));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    private static Stream<Arguments> allowedUsers() {
        return Stream.of(
                Arguments.of(UserRole.CUSTOMER, "/api/s/test"),
                Arguments.of(UserRole.ADMIN, "/api/s/test"),
                Arguments.of(UserRole.ADMIN, "/api/admin/test")
        );
    }
}