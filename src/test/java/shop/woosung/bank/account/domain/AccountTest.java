package shop.woosung.bank.account.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import shop.woosung.bank.account.handler.exception.NotAccountOwnerException;
import shop.woosung.bank.account.handler.exception.NotEnoughBalanceException;
import shop.woosung.bank.account.handler.exception.NotMatchAccountPasswordException;
import shop.woosung.bank.mock.util.FakePasswordEncoder;
import shop.woosung.bank.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @DisplayName("등록할 계좌를 만든다.")
    @Test
    void create_register_account() {
        // given
        AccountRegister accountRegister = AccountRegister.builder().
                typeNumber(232L).
                newNumber(11111111L).
                password("1234").
                balance(1000L).
                accountType(AccountType.NORMAL).
                user(User.builder().id(1L).build()).build();
        FakePasswordEncoder passwordEncoder = new FakePasswordEncoder("aaaa-bbbb-cccc");

        // when
        Account result = Account.register(accountRegister, passwordEncoder);

        // then
        assertThat(result.getNumber()).isEqualTo(11111111L);
        assertThat(result.getFullNumber()).isEqualTo(23211111111L);
        assertThat(result.getPassword()).isEqualTo("aaaa-bbbb-cccc");
        assertThat(result.getBalance()).isEqualTo(1000L);
        assertThat(result.getType()).isEqualTo(AccountType.NORMAL);
        assertThat(result.getUser().getId()).isEqualTo(1L);
    }

    @DisplayName("자신의 계좌면 예외를 뱉지 않는다.")
    @Test
    void if_account_owner_is_mine_not_exception() {
        // given
        User user = User.builder().id(1L).build();
        Account account = Account.builder().user(user).build();

        // when & then
        assertDoesNotThrow(() -> account.checkOwner(user.getId()));
    }

    @DisplayName("자신의 계좌가 아니면 예외를 뱉는다.")
    @Test
    void if_account_owner_is_others_throw_exception() {
        // given
        User user = User.builder().id(1L).build();
        User anotherUser = User.builder().id(2L).build();
        Account account = Account.builder().user(user).build();

        // when & then
        assertThatThrownBy(() -> account.checkOwner(anotherUser.getId()))
                .isInstanceOf(NotAccountOwnerException.class);
    }

    @DisplayName("계좌에 금액을 입금한다.")
    @Test
    void deposit_account() {
        // given
        Account account = Account.builder().balance(1000L).build();

        // when
        account.deposit(1000L);

        // then
        assertThat(account.getBalance()).isEqualTo(2000L);
    }

    @DisplayName("계좌 비밀번호가 일치하면 예외를 뱉지 않는다.")
    @Test
    void match_account_password_not_exception() {
        // given
        Account account = Account.builder().password("aaaa-bbbb-cccc").build();

        // when & then
        assertDoesNotThrow(() -> account.checkPasswordMatch("1234", new FakePasswordEncoder("aaaa-bbbb-cccc")));
    }

    @DisplayName("계좌 비밀번호가 일치하지 않으면 예외를 뱉는다.")
    @Test
    void if_account_password_not_match_throw_exception() {
        // given
        Account account = Account.builder().password("dddd-eeee-ffff").build();

        // when & then
        assertThatThrownBy(() -> account.checkPasswordMatch("1234", new FakePasswordEncoder("aaaa-bbbb-cccc")))
                .isInstanceOf(NotMatchAccountPasswordException.class);
    }

    @DisplayName("계좌 잔액이 출금 금액보다 많으면 예외를 뱉지 않는다.")
    @ParameterizedTest
    @CsvSource({
            "1000, 1000",
            "1000, 999",
            "1000, 0"
    })
    void account_balance_is_enough_not_exception(Long accountBalance, Long amount) {
        // given
        Account account = Account.builder().balance(accountBalance).build();

        // when & then
        assertDoesNotThrow(() -> account.checkEnoughBalance(amount));
    }

    @DisplayName("계좌 잔액이 출금 금액보다 적으면 예외를 뱉는다.")
    @Test
    void if_account_balance_is_not_enough_throw_exception() {
        // given
        Account account = Account.builder().balance(1000L).build();

        // when & then
        assertThatThrownBy(() -> account.checkEnoughBalance(1001L))
                .isInstanceOf(NotEnoughBalanceException.class);
    }

    @DisplayName("계좌에서 출금한다.")
    @Test
    void withdraw_account() {
        // given
        Account account = Account.builder().balance(1000L).build();

        // when
        account.withdraw(100L);

        // then
        assertThat(account.getBalance()).isEqualTo(900L);
    }
}