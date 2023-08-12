package shop.woosung.bank.user.service.dto;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.user.domain.User;

@Getter
@Builder
public class JoinResponseDto {
    private Long id;
    private String email;
    private String name;

    public static JoinResponseDto from(User user) {
        return JoinResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
