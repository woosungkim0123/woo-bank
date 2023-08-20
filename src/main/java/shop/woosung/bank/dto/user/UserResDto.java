package shop.woosung.bank.dto.user;

import lombok.Getter;
import lombok.Setter;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.common.util.CustomDateUtil;

@Getter @Setter
public class UserResDto {

    @Getter @Setter
    public static class LoginResDto {
        private Long id;
        private String username;
        private String createdAt;

        public LoginResDto(User userEntity) {
            this.id = userEntity.getId();
            this.username = userEntity.getName();
            this.createdAt = CustomDateUtil.toStringFormat(userEntity.getCreatedAt());
        }
    }

}
