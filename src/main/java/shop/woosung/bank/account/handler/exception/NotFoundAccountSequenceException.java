package shop.woosung.bank.account.handler.exception;

import shop.woosung.bank.account.domain.AccountType;

public class NotFoundAccountSequenceException extends RuntimeException {
    public NotFoundAccountSequenceException(AccountType accountType) {
        super("계좌 시퀀스 정보를 찾을 수 없습니다. 들어온 계좌 타입 : " + accountType.name());
    }
}
