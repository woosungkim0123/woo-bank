package shop.woosung.bank.user.service.dto;

import lombok.Getter;
import shop.woosung.bank.user.domain.User;

@Getter
public class JoinResponseDto {
    private Long id;
    private String email;
    private String name;

    public JoinResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
    }
}
