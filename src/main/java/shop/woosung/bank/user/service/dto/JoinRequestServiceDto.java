package shop.woosung.bank.user.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JoinRequestServiceDto {
    private final String email;
    private final String password;
    private final String name;

    @Builder
    public JoinRequestServiceDto(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
