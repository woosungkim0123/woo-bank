package shop.woosung.bank.dto.user;

import lombok.Getter;
import lombok.Setter;


public class UserReqDto {


    @Getter @Setter
    public static class LoginReqDto {
        private String username;
        private String password;
    }

}
