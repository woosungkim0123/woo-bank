package shop.woosung.bank.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.dto.ResponseDto;
import shop.woosung.bank.dto.transaction.TransactionResponseDto;
import shop.woosung.bank.dto.transaction.TransactionResponseDto.TransactionResponseListDto;
import shop.woosung.bank.service.TransactionService;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/s/account/{number}/transaction")
    public ResponseEntity<?> findTransactionList(
        @PathVariable Long number,
        @RequestParam(value = "type", defaultValue = "ALL") String type,
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @AuthenticationPrincipal LoginUser loginUser
    ) {
        TransactionResponseListDto transactionResponseListDto = transactionService.getTransactionList(loginUser.getUser().getId(), number, type, page);

        return new ResponseEntity<>(new ResponseDto<>(1, "입출금목록보기 성공", transactionResponseListDto), HttpStatus.OK);
    }
}
