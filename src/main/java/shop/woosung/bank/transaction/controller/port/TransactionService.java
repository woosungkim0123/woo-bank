package shop.woosung.bank.transaction.controller.port;

import shop.woosung.bank.transaction.service.dto.TransactionResponseListDto;

public interface TransactionService {

    TransactionResponseListDto getTransactionList(Long userId, Long accountNumber, String type, int page);
}
