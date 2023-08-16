package shop.woosung.bank.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.woosung.bank.user.handler.exception.EmailAlreadyInUseException;
import shop.woosung.bank.mock.FakePasswordEncoder;
import shop.woosung.bank.mock.FakeUserRepository;
import shop.woosung.bank.user.controller.port.UserService;
import shop.woosung.bank.user.service.dto.JoinRequestServiceDto;
import shop.woosung.bank.user.service.dto.JoinResponseDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class UserServiceImplTest {

    private UserService userService;

    @BeforeEach
    void init() {
        this.userService = UserServiceImpl.builder()
                .userRepository(new FakeUserRepository())
                .passwordEncoder(new FakePasswordEncoder("aaaa_bbbb_cccc_dddd"))
                .build();
    }

    @Test
    void JoinRequestServiceDto_를_이용_하여_유저를_생성_할_수_있다() {
        // given
        JoinRequestServiceDto joinRequestServiceDto = JoinRequestServiceDto.builder()
                .email("test1@test.com")
                .password("1234")
                .name("test1")
                .build();

        // when
        JoinResponseDto result = userService.join(joinRequestServiceDto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test1@test.com");
        assertThat(result.getName()).isEqualTo("test1");
    }
    
    @Test
    void 동일한_이메일을_가진_유저가_있다면_예외를_던진다() {
        // given & when
        userService.join(JoinRequestServiceDto.builder()
                .email("test1@test.com")
                .password("1234")
                .name("test1")
                .build());
        JoinRequestServiceDto joinRequestServiceDto = JoinRequestServiceDto.builder()
                .email("test1@test.com")
                .password("1234")
                .name("test1")
                .build();

        // then
        assertThatThrownBy(() -> userService.join(joinRequestServiceDto))
                .isInstanceOf(EmailAlreadyInUseException.class);
    }
}