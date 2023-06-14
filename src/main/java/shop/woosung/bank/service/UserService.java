package shop.woosung.bank.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.domain.user.User;
import shop.woosung.bank.domain.user.UserEnum;
import shop.woosung.bank.domain.user.repository.UserRepository;
import shop.woosung.bank.handler.ex.CustomApiException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    // 서비스는 DTO를 받고 DTO로 응답한다.(고정)
    @Transactional // 트랜잭션이 메서드 시작할 때 시작되고, 메서드가 정상 종료되면 커밋, 예외가 발생하면 롤백
    public SignUpResDto signUp(SignUpReqDto signUpReqDto) {
        // 1. 동일 유저네임 존재 검사
        Optional<User> user = userRepository.findByUsername(signUpReqDto.getUsername());
        if(user.isPresent()) {
           // 유저 네임 중복 -> 이것도 내가 제어해줘야함
            throw new CustomApiException("동일한 username이 존재합니다."); // 날라가면 RestControllerAdvice가 받아서 해당 메서드가 실행됨

        }
        // 2. 패스워드 인코딩 + 회원가입 동시에

        User userPS = userRepository.save(signUpReqDto.toEntity(passwordEncoder));
        // 일단적인 객체는 user하면 되는데 강사는 영속성 컨텍스트에 들어갓다 나온건 userPS붙임

        // 3. dto 응답
        return new SignUpResDto(userPS);

    }

    @ToString
    @Getter @Setter
    public static class SignUpResDto {
        private Long id;
        private String username;
        private String fullname;


        public SignUpResDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.fullname = user.getFullname();
        }
    }



    @ToString
    @Getter @Setter
    public static class SignUpReqDto {
        // 유효성 검사 필요
        private String username;
        private String password;
        private String email;
        private String fullname;

        // dto로 받은걸 엔티티로 리턴하는 걸 만듬
        public User toEntity(BCryptPasswordEncoder passwordEncoder) {
            return User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .fullname(fullname)
                    .role(UserEnum.CUSTOMER)
                    .build();
        }
    }



}
