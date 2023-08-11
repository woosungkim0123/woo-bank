package shop.woosung.bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.woosung.bank.user.infrastructure.UserEntity;
import shop.woosung.bank.user.UserRole;
import shop.woosung.bank.user.infrastructure.UserJpaRepository;
import shop.woosung.bank.dto.user.UserReqDto;
import shop.woosung.bank.dto.user.UserResDto;
import shop.woosung.bank.user.service.UserServiceImpl;
import shop.woosung.bank.util.dummy.DummyUserObject;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
//class UserEntityServiceImplTest extends DummyUserObject {
//
//    @InjectMocks
//    private UserServiceImpl userServiceImpl;
//
//    @Mock
//    private UserJpaRepository userJpaRepository;
//
//    @Spy
//    private BCryptPasswordEncoder passwordEncoder;
//
//    @Test
//    void 회원가입_test() throws Exception {
//        // given
//        UserReqDto.JoinReqDto signUpReqDto =  new UserReqDto.JoinReqDto();
//        signUpReqDto.setUsername("user1");
//        signUpReqDto.setPassword("1234");
//        signUpReqDto.setEmail("test@naver.com");
//        signUpReqDto.setFullname("테스터");
//        // stub1
//        when(userJpaRepository.findByName(any())).thenReturn(Optional.empty());
//        // stub2
//        UserEntity mockUserEntity = newMockUser(1L, "user1", "1234", "test@naver.com", "테스터", UserRole.CUSTOMER);
//        when(userJpaRepository.save(any())).thenReturn(mockUserEntity);
//
//        // when
//        UserResDto.JoinResDto signUpResDto = userServiceImpl.join(signUpReqDto);
//
//        // then
//        assertThat(signUpResDto.getId()).isEqualTo(1L);
//        assertThat(signUpResDto.getUsername()).isEqualTo("user1");
//    }
//}