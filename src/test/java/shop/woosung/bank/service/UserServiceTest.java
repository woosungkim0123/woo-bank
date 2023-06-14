package shop.woosung.bank.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.woosung.bank.domain.user.User;
import shop.woosung.bank.domain.user.UserEnum;
import shop.woosung.bank.domain.user.repository.UserRepository;
import shop.woosung.bank.service.UserService.SignUpResDto;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // 모키토 환경에서 테스트 진행
class UserServiceTest {

    // 가짜환경을 만들어서 넣음
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    // 진짜를 가짜에 집어넣는 방법(가짜가 아니라 진짜를 띄우는 방법) :: Mock과 차이
    // spy를 사용하면 spring ioc에 있는 걸 꺼내서 InjectMock에 집어넣음
    // userService에 userRepository는 가짜지만 passwordEncoder는 진짜임
    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void 회원가입_test() throws Exception {
        // given
        UserService.SignUpReqDto signUpReqDto =  new UserService.SignUpReqDto();
        signUpReqDto.setUsername("user1");
        signUpReqDto.setPassword("1234");
        signUpReqDto.setEmail("sibu2005@Naver.com");
        signUpReqDto.setFullname("쌀");

        // stub1 : 가정법, 가설
        // 니가 이걸 실행하면 이게 리턴될거야
        // 매개변수에 뭐라도 들어가면
        //when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));

        // stub2
        User woo = User.builder()
                .id(1L)
                .username("user1")
                .password("1234")
                .email("sibu2005@Naver.com")
                .fullname("쌀")
                .role(UserEnum.CUSTOMER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.save(any())).thenReturn(woo);

        // when

        SignUpResDto signUpResDto = userService.signUp(signUpReqDto);
        // 그대로하면 에러뜸 null pointer -> findByUsername에서 터짐
        // Mock = 가짜 환경이라서 스프링 관련 빈들이 하나도 없는 상태
        // 가짜환경에다가 서비스할때는 필요한걸 등록해줘야함
        // private UserRepository userRepository;
        // 근데 우리가 서비스를 테스트하는거지 userRepository를 테스트하는게 아님
        // 가짜로 메모리에 띄워서 InjectMocks 어노테이션 UserService에 주입을 해줌
        // 메서드가 없어서 nullPointer가 뜸 stub이라는 처리를 해줘야함
        // findByUsername과 save 둘다 stub 처리를 하고
        // bcrpt에 encoder에 대한 처리를 해줘야함
        System.out.println(signUpResDto);

        Assertions.assertThat(signUpResDto.getId()).isEqualTo(1L);
        Assertions.assertThat(signUpResDto.getUsername()).isEqualTo("user1");
    }
}