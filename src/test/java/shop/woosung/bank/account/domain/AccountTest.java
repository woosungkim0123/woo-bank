package shop.woosung.bank.account.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woosung.bank.account.handler.exception.NotAccountOwnerException;
import shop.woosung.bank.user.domain.User;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @DisplayName("계좌가 자신의 계좌인지 체크한다.")
    @Test
    public void account_check_owner_success() {
        // given & when
        User user = User.builder().id(1L).email("test1@tset.com").name("test1").build();
        Account account = Account.builder().number(11111111L).fullNumber(23211111111L).type(AccountType.NORMAL).balance(1000L).user(user).build();

        // then
        assertDoesNotThrow(() -> account.checkOwner(user.getId()));
    }

    @DisplayName("자신의 계좌가 아니면 예외를 뱉는다.")
    @Test
    public void account_check_owner_fail() {
        // given & when
        User user = User.builder().id(1L).email("test1@tset.com").name("test1").build();
        User anotherUser = User.builder().id(2L).email("test2@tset.com").name("test2").build();
        Account account = Account.builder().number(11111111L).fullNumber(23211111111L).type(AccountType.NORMAL).balance(1000L).user(user).build();

        // then
        assertThatThrownBy(() -> account.checkOwner(anotherUser.getId())).isInstanceOf(NotAccountOwnerException.class);
    }
}