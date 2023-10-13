package shop.woosung.bank.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.woosung.bank.account.controller.dto.AccountDepositRequestDto;
import shop.woosung.bank.account.controller.dto.AccountRegisterRequestDto;
import shop.woosung.bank.account.controller.dto.AccountTransferRequestDto;
import shop.woosung.bank.account.controller.dto.AccountWithdrawRequestDto;
import shop.woosung.bank.account.controller.port.AccountService;
import shop.woosung.bank.account.service.dto.*;
import shop.woosung.bank.common.ApiResponse;
import shop.woosung.bank.config.auth.LoginUser;

import javax.validation.Valid;

import static shop.woosung.bank.account.util.AccountControllerToServiceConverter.*;


@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/s/accounts")
    public ResponseEntity<ApiResponse<AccountListResponseDto>> findUserAccounts(@AuthenticationPrincipal LoginUser loginUser) {

        AccountListResponseDto accountListResponseDto = accountService.getAccountList(loginUser.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(accountListResponseDto));
    }

    @PostMapping("/s/account")
    public ResponseEntity<ApiResponse<AccountRegisterResponseDto>> register(@RequestBody @Valid AccountRegisterRequestDto accountRegisterRequestDto,
                                           @AuthenticationPrincipal LoginUser loginUser) {
        AccountRegisterResponseDto accountRegisterResponseDto = accountService.register(accountRegisterRequestConvert(accountRegisterRequestDto), loginUser.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("계좌등록 성공", accountRegisterResponseDto));
    }

    @DeleteMapping("/s/account/{number}")
    public ResponseEntity<ApiResponse<Object>> deleteAccount(@PathVariable Long number,
                                           @AuthenticationPrincipal LoginUser loginUser) {
        accountService.deleteAccount(number, loginUser.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("계좌 삭제 성공"));
    }

    @PostMapping("/account/deposit")
    public ResponseEntity<ApiResponse<AccountDepositResponseDto>> depositAccount(@RequestBody @Valid AccountDepositRequestDto accountDepositRequestDto) {
        AccountDepositResponseDto accountDepositResponseDto = accountService.deposit(accountDepositRequestConvert(accountDepositRequestDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("계좌 입금 완료", accountDepositResponseDto));
    }

    @PostMapping("/s/account/withdraw")
    public ResponseEntity<ApiResponse<AccountWithdrawResponseDto>> withdrawAccount(@RequestBody @Valid AccountWithdrawRequestDto accountWithdrawRequestDto,
                                             @AuthenticationPrincipal LoginUser loginUser) {

        AccountWithdrawResponseDto accountWithdrawResponseDto = accountService.withdraw(accountWithdrawRequestConvert(accountWithdrawRequestDto), loginUser.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("계좌 출금 완료", accountWithdrawResponseDto));
    }

    @PostMapping("/s/account/transfer")
    public ResponseEntity<ApiResponse<AccountTransferResponseDto>> transferAccount(@RequestBody @Valid AccountTransferRequestDto accountTransferRequestDto,
                                             @AuthenticationPrincipal LoginUser loginUser) {

        AccountTransferResponseDto accountTransferResponseDto = accountService.transfer(accountTransferRequestConvert(accountTransferRequestDto), loginUser.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("계좌 이체 완료", accountTransferResponseDto));
    }

//    @GetMapping("/s/account/{number}")
//    public ResponseEntity<?> getDetailAccount(@PathVariable Long number,
//                                             @RequestParam(value = "page", defaultValue = "0") Integer page,
//                                             @AuthenticationPrincipal LoginUser loginUser) {
//        AccountDetailResDto accountDetailResDto = accountService.getAccountDetail(number, loginUser.getUser().getId(), page);
//
//        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 상세보기 성공", accountDetailResDto), HttpStatus.OK);
//    }
}
