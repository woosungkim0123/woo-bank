package shop.woosung.bank.user.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.user.domain.User;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class JoinResponseDto {
    private final Long id;
    private final String email;
    private final String name;

    public static JoinResponseDto from(User user) {
        return JoinResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
