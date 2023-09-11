package shop.woosung.bank.transaction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.woosung.bank.common.ApiResponse;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.transaction.TransactionResponseDto.TransactionResponseListDto;
import shop.woosung.bank.transaction.controller.port.TransactionService;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/s/account/{number}/transaction")
    public ResponseEntity<ApiResponse<TransactionResponseListDto>> findTransactionList(
            @PathVariable Long number,
            @RequestParam(value = "type", defaultValue = "ALL") String type,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @AuthenticationPrincipal LoginUser loginUser) {

        TransactionResponseListDto transactionResponseListDto = transactionService.getTransactionList(loginUser.getUser().getId(), number, type, page);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("입출금목록보기 성공", transactionResponseListDto));
    }
}
