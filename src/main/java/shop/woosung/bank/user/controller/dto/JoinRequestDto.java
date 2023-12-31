package shop.woosung.bank.user.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinRequestDto {

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]{1,9}@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식으로 작성해주세요.")
    @NotEmpty
    private String email;

    @Size(min = 4, max = 20)
    @NotEmpty
    private String password;

    @Pattern(regexp = "^[a-zA-Z가-힣0-9]{2,20}$", message = "영문/한글 2~20자 이내로 작성해주세요.")
    @NotEmpty
    private String name;

    @Builder
    public JoinRequestDto(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
