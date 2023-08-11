package shop.woosung.bank.config.jwt;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.user.infrastructure.UserEntity;
import shop.woosung.bank.user.UserRole;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthorizationFilterTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("권한 통과 테스트")
    @ParameterizedTest(name = "로그인이 필요한 요청 => {0}")
    @MethodSource("provideUserEnum")
    public void authorization_success_test(UserRole userRole, String allowUrl) throws Exception {
        // given
        String jwtToken = getCorrectToken(userRole);

        // when
        ResultActions resultActions = mvc.perform(get( allowUrl).header(JwtVO.HEADER,  jwtToken));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @DisplayName("권한 실패 테스트 - 권한없음")
    @Test
    @ValueSource(strings = {"", " ", JwtVO.HEADER + " ", " " + JwtVO.HEADER })
    public void authorization_fail_test() throws Exception {
        // given
        String jwtToken = getCorrectToken(UserRole.CUSTOMER);

        // when
        ResultActions resultActions = mvc.perform(get("/api/admin/test").header(JwtVO.HEADER, jwtToken));

        // then
        resultActions.andExpect(status().isForbidden());
    }

    @DisplayName("권한 실패 테스트 - 헤더 X")
    @ParameterizedTest(name = "헤더 Key => {0}")
    @ValueSource(strings = {"", " ", JwtVO.HEADER + " ", " " + JwtVO.HEADER })
    public void authorization_not_header_fail_test() throws Exception {
        // given & when
        ResultActions resultActions = mvc.perform(get("/api/s/test"));

        // then
        resultActions.andExpect(status().isUnauthorized());
    }

    @DisplayName("권한 실패 테스트 - 토큰 만료")
    @Test
    public void authorization_expired_token_fail_test() throws Exception {
        // given & when
        ResultActions resultActions = mvc.perform(get("/api/s/test").header(JwtVO.HEADER, getExpiredToken()));
        
        // then
        resultActions.andExpect(status().isUnauthorized());
        resultActions.andExpect(jsonPath("$.message").value("토큰이 만료되었습니다."));
    }

    @DisplayName("권한 실패 테스트 - 잘못된 토큰")
    @ParameterizedTest(name = "{0} - {1}")
    @MethodSource("provideWrongToken")
    public void authorization_wrong_token_fail_test(String key, String wrongToken) throws Exception {
        // given & when
        ResultActions resultActions = mvc.perform(get("/api/s/test").header(JwtVO.HEADER, wrongToken));

        // then
        resultActions.andExpect(status().isUnauthorized());
        resultActions.andExpect(jsonPath("$.message").value("토큰 검증에 실패했습니다."));
    }

    private static Stream<Arguments> provideUserEnum() {
        return Stream.of(
                Arguments.of(UserRole.CUSTOMER, "/api/s/test"),
                Arguments.of(UserRole.ADMIN, "/api/s/test"),
                Arguments.of(UserRole.ADMIN, "/api/admin/test")
        );
    }

    private static Stream<Arguments> provideWrongToken() {
        return Stream.of(
                Arguments.of("잘못된 토큰", getWrongToken()),
                Arguments.of("빈공백", ""),
                Arguments.of("한칸공백", " "),
                Arguments.of("토큰 앞에 한칸공백", " " + getCorrectToken(UserRole.CUSTOMER)),
                Arguments.of("토큰 뒤에 한칸공백", getCorrectToken(UserRole.CUSTOMER) + " ")
        );
    }

    private static String getCorrectToken(UserRole userRole) {
        UserEntity userEntity = UserEntity.builder().id(1L).role(userRole).build();
        LoginUser loginUser = new LoginUser(userEntity);
        JwtSystemHolder jwtSystemHolder = new JwtSystemHolder();
        return JwtProcess.create(jwtSystemHolder, loginUser);
    }

    private static String getWrongToken() {
        return JwtVO.TOKEN_PREFIX + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYW5rIiwicm9sZSI6IkNVU1RPTUVSIiwiaWQiOjEsImV4cCI6MTY4ODk5NzkzMH0.mLdls0X1C-dDiewYMGoK7fxw448_BVtYX4n8UBGImzHQDNzMcVeOmakHCEFsRLoIFDRr5TWBnWX444sHj-wrong";
    }

    private String getExpiredToken() {
        return JwtVO.TOKEN_PREFIX + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYW5rIiwicm9sZSI6IkNVU1RPTUVSIiwiaWQiOjEsImV4cCI6MTY4ODk5NzkzMH0.mLdls0X1C-dDiewYMGoK7fxw448_BVtYX4n8UBGImzHQDNzMcVeOmakHCEFsRLoIFDRr5TWBnWXy0sHj-Q9qJQ";
    }

}