package shop.woosung.bank.user.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JoinRequestServiceDto {
    private String email;
    private String password;
    private String name;

    @Builder
    public JoinRequestServiceDto(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
