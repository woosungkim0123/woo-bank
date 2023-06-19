package shop.woosung.bank.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.woosung.bank.domain.user.User;
import shop.woosung.bank.domain.user.UserEnum;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class UserReqDto {


    @Getter
    public static class LoginReqDto {
        private String username;
        private String password;
    }


    @Getter @Setter
    public static class JoinReqDto {
        // NotEmpty 이런거 걸어놓고 잘동작하려면
        @NotEmpty // null이거나, 공백일 수 없다.
        private String username;
        @NotEmpty
        private String password;
        @NotEmpty
        private String email;
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
