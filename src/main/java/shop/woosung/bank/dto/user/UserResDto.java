package shop.woosung.bank.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import shop.woosung.bank.domain.user.User;

public class UserResDto {

    @ToString
    @Getter
    @Setter
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
}
