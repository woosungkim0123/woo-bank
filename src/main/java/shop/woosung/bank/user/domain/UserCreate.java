package shop.woosung.bank.user.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCreate {
    private final String email;
    private final String password;
    private final String name;
    private final UserRole role;

    @Builder
    public UserCreate(String email, String password, String name, UserRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }
}
