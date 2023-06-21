package shop.woosung.bank.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.woosung.bank.domain.user.User;
import shop.woosung.bank.domain.user.UserEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class UserReqDto {


    @Getter
    public static class LoginReqDto {
        private String username;
        private String password;
    }

    @Getter @Setter
    public static class JoinReqDto {
        // 영문, 숫자는 되고, 길이 최소 2~20자 이내
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해주세요.")
        private String username;
        // 길이 4~ 20
        @Size(min = 4, max = 20) // String에만 됨
        @NotEmpty
        private String password;
        // 이메일 형식
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]{1,9}@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식으로 작성해주세요.")
        @NotEmpty
        private String email;
        // 영어, 한글 1 ~ 20
        @Pattern(regexp = "^[a-zA-Z가-힣 ]{1,20}$", message = "영어/한글 1~20자 이내로 작성해주세요.")
        @NotEmpty
        private String fullname;

        public User toEntity(BCryptPasswordEncoder passwordEncoder) {
            return User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .fullname(fullname)
                    .role(UserEnum.CUSTOMER)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }
    }
}
