package shop.woosung.bank.transaction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.account.service.AccountServiceImpl;
import shop.woosung.bank.account.service.dto.AccountDto;
import shop.woosung.bank.transaction.controller.port.TransactionService;
import shop.woosung.bank.transaction.domain.Transaction;
import shop.woosung.bank.transaction.service.dto.TransactionResponseListDto;
import shop.woosung.bank.transaction.service.port.TransactionRepository;
import shop.woosung.bank.user.domain.User;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountServiceImpl accountService;

    @Transactional(readOnly = true)
    public TransactionResponseListDto getTransactionList(Long accountFullNumber, String type, int page, User user) {
        AccountDto accountDto = accountService.checkAccountOwner(accountFullNumber, user);

        List<Transaction> transactionList = transactionRepository.findTransactionList(accountDto.getId(), type, page);

        return TransactionResponseListDto.from(accountDto, transactionList);
    }
}
