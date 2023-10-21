package shop.woosung.bank.transaction.controller.port;

import shop.woosung.bank.transaction.service.dto.TransactionResponseListDto;
import shop.woosung.bank.user.domain.User;

public interface TransactionService {

    TransactionResponseListDto getTransactionList(Long accountNumber, String type, int page, User user);
}
