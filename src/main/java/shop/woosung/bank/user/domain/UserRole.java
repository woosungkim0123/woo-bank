package shop.woosung.bank.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
    ADMIN("관리자"), CUSTOMER("고객");

    private String value;
}
