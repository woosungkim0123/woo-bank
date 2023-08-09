package shop.woosung.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.domain.account.Account;
import shop.woosung.bank.domain.account.repository.AccountRepository;
import shop.woosung.bank.domain.transaction.Transaction;
import shop.woosung.bank.domain.transaction.repository.TransactionRepository;
import shop.woosung.bank.dto.transaction.TransactionResponseDto;
import shop.woosung.bank.handler.ex.CustomApiException;

import java.util.List;

import static shop.woosung.bank.dto.transaction.TransactionResponseDto.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionResponseListDto getTransactionList(Long userId, Long accountNumber, String type, int page) {
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new CustomApiException("해당 계좌를 찾을 수 없습니다."));

        account.checkOwner(userId);

        List<Transaction> transactionList = transactionRepository.findTransactionList(account.getId(), type, page);

        return new TransactionResponseListDto(account, transactionList);
    }
}
