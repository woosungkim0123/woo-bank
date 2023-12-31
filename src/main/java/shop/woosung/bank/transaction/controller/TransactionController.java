package shop.woosung.bank.transaction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.woosung.bank.common.ApiResponse;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.transaction.controller.port.TransactionService;
import shop.woosung.bank.transaction.service.dto.TransactionResponseListDto;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/s/account/{accountFullNumber}/transaction")
    public ResponseEntity<ApiResponse<TransactionResponseListDto>> findTransactionList(
            @PathVariable Long accountFullNumber,
            @RequestParam(value = "type", defaultValue = "ALL") String type,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @AuthenticationPrincipal LoginUser loginUser) {

        TransactionResponseListDto transactionResponseListDto = transactionService.getTransactionList(accountFullNumber, type, page, loginUser.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("입출금 목록보기 성공", transactionResponseListDto));
    }
}
