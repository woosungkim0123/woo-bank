package shop.woosung.bank.transaction.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woosung.bank.account.domain.Account;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionTest {

    @DisplayName("입금 계좌 내역을 생성한다.")
    @Test
    void create_deposit_transaction() {
        // given
        DepositTransactionCreate depositTransactionCreate = DepositTransactionCreate.builder()
                .depositAccount(Account.builder().id(1L).build())
                .depositAccountBalance(1000L)
                .amount(1000L)
                .type(TransactionType.DEPOSIT)
                .sender("ATM")
                .receiver("2321111111111")
                .tel("010-1111-1111")
                .build();

        // when
        Transaction result = Transaction.createDepositTransaction(depositTransactionCreate);

        // then
        assertThat(result.getDepositAccount().getId()).isEqualTo(1L);
        assertThat(result.getDepositAccountBalance()).isEqualTo(1000L);
        assertThat(result.getAmount()).isEqualTo(1000L);
        assertThat(result.getType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(result.getSender()).isEqualTo("ATM");
        assertThat(result.getReceiver()).isEqualTo("2321111111111");
        assertThat(result.getTel()).isEqualTo("010-1111-1111");
    }

    @DisplayName("출금 계좌 내역을 생성한다.")
    @Test
    void create_withdraw_transaction() {
        // given
        WithdrawTransactionCreate withdrawTransactionCreate = WithdrawTransactionCreate.builder()
                .withdrawAccount(Account.builder().id(1L).build())
                .withdrawAccountBalance(1000L)
                .amount(1000L)
                .type(TransactionType.WITHDRAW)
                .sender("2321111111111")
                .receiver("ATM")
                .build();

        // when
        Transaction result = Transaction.createWithdrawTransaction(withdrawTransactionCreate);

        // then
        assertThat(result.getWithdrawAccount().getId()).isEqualTo(1L);
        assertThat(result.getWithdrawAccountBalance()).isEqualTo(1000L);
        assertThat(result.getAmount()).isEqualTo(1000L);
        assertThat(result.getType()).isEqualTo(TransactionType.WITHDRAW);
        assertThat(result.getSender()).isEqualTo("2321111111111");
        assertThat(result.getReceiver()).isEqualTo("ATM");
    }
}