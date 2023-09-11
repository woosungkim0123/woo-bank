package shop.woosung.bank.transaction.controller.port;

import shop.woosung.bank.transaction.TransactionResponseDto;

public interface TransactionService {

    TransactionResponseDto.TransactionResponseListDto getTransactionList(Long userId, Long accountNumber, String type, int page);
}
