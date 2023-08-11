package shop.woosung.bank.user.domain;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.user.UserRole;

@Getter
public class UserCreate {
    private String email;
    private String password;
    private String name;
    private UserRole role;

    @Builder
    public UserCreate(String email, String password, String name, UserRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }
}
