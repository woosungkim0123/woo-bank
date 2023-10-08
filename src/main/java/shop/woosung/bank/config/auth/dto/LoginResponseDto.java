package shop.woosung.bank.config.auth.dto;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.domain.UserRole;

@Getter
public class LoginResponseDto {
    private final Long id;
    private final String name;
    private final UserRole role;

    @Builder
    public LoginResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.role = user.getRole();
    }
}
