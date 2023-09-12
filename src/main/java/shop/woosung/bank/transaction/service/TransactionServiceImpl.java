package shop.woosung.bank.transaction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.handler.exception.NotFoundAccountFullNumberException;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.transaction.controller.port.TransactionService;
import shop.woosung.bank.transaction.domain.Transaction;
import shop.woosung.bank.transaction.service.dto.TransactionResponseListDto;
import shop.woosung.bank.transaction.service.port.TransactionRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionResponseListDto getTransactionList(Long userId, Long accountFullnumber, String type, int page) {
        Account account = accountRepository.findByFullnumber(accountFullnumber)
                .orElseThrow(() -> new NotFoundAccountFullNumberException(accountFullnumber));

        account.checkOwner(userId);

        List<Transaction> transactionList = transactionRepository.findTransactionList(account.getId(), type, page);

        return new TransactionResponseListDto(account, transactionList);
    }
}
