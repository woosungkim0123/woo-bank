package shop.woosung.bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.woosung.bank.domain.user.User;
import shop.woosung.bank.domain.user.UserEnum;
import shop.woosung.bank.domain.user.repository.UserRepository;
import shop.woosung.bank.dto.user.UserReqDto;
import shop.woosung.bank.dto.user.UserResDto;
import shop.woosung.bank.util.dummy.DummyUserObject;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest extends DummyUserObject {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void 회원가입_test() throws Exception {
        // given
        UserReqDto.SignUpReqDto signUpReqDto =  new UserReqDto.SignUpReqDto();
        signUpReqDto.setUsername("user1");
        signUpReqDto.setPassword("1234");
        signUpReqDto.setEmail("test@naver.com");
        signUpReqDto.setFullname("테스터");
        // stub1
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        // stub2
        User mockUser = newMockUser(1L, "user1", "1234", "test@naver.com", "테스터", UserEnum.CUSTOMER);
        when(userRepository.save(any())).thenReturn(mockUser);

        // when
        UserResDto.SignUpResDto signUpResDto = userService.signUp(signUpReqDto);

        // then
        assertThat(signUpResDto.getId()).isEqualTo(1L);
        assertThat(signUpResDto.getUsername()).isEqualTo("user1");
    }
}