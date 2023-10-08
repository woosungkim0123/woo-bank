package shop.woosung.bank.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.woosung.bank.common.service.port.PasswordEncoder;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.handler.exception.EmailAlreadyInUseException;
import shop.woosung.bank.user.service.dto.JoinRequestServiceDto;
import shop.woosung.bank.user.service.dto.JoinResponseDto;
import shop.woosung.bank.user.service.port.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName("회원가입에 성공한다.")
    @Test
    void join_success() {
        // given
        JoinRequestServiceDto joinRequestServiceDto = JoinRequestServiceDto.builder()
                .email("test1@test.com")
                .password("1234")
                .name("test1")
                .build();

        // stub
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("aaaa_bbbb_cccc_dddd");
        when(userRepository.save(any(User.class))).thenReturn(User.builder().id(1L).email("test1@test.com").name("test1").build());

        // when
        JoinResponseDto result = userService.join(joinRequestServiceDto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test1@test.com");
        assertThat(result.getName()).isEqualTo("test1");
    }

    @DisplayName("회원가입시 이메일이 중복된다면 예외를 발생시킨다.")
    @Test
    void duplicate_email_when_join_throw_exception() {
        // given
        JoinRequestServiceDto joinRequestServiceDto = JoinRequestServiceDto.builder()
                .email("test1@test.com")
                .password("1234")
                .name("test1")
                .build();

        // stub
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(User.builder().build()));

        // when & then
        assertThatThrownBy(() -> userService.join(joinRequestServiceDto))
                .isInstanceOf(EmailAlreadyInUseException.class);
    }
}