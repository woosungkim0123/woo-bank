package shop.woosung.bank.config.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    private String email;
    private String password;

    public LoginRequestDto() {
    }

    @Builder
    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
