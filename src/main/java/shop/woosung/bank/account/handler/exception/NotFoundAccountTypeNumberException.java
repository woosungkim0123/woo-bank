package shop.woosung.bank.account.handler.exception;

import shop.woosung.bank.account.domain.AccountType;

public class NotFoundAccountTypeNumberException extends RuntimeException {
    public NotFoundAccountTypeNumberException(AccountType accountType) {
        super("계좌 타입에 해당되는 타입 번호가 없습니다. 들어온 계좌 타입 : " + accountType.name());
    }
}
