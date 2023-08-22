package shop.woosung.bank.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.dto.ResponseDto;
import shop.woosung.bank.account.service.AccountService;

import javax.validation.Valid;

import static shop.woosung.bank.account.AccountReqDto.*;
import static shop.woosung.bank.account.AccountResDto.*;


@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/s/account")
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountRegisterReqDto accountRegisterReqDto,
                                           BindingResult bindingResult,
                                           @AuthenticationPrincipal LoginUser loginUser) {
        AccountRegisterResDto accountRegisterResDto = accountService.registerAccount(accountRegisterReqDto, loginUser.getUser().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "계좌등록 성공", accountRegisterResDto), HttpStatus.CREATED);
    }

    @GetMapping("/s/accounts")
    public ResponseEntity<?> findUserAccount(@AuthenticationPrincipal LoginUser loginUser) {

        AccountListResDto accountListResDto = accountService.getAccountList(loginUser.getUser().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "계좌조회 성공", accountListResDto), HttpStatus.OK);
    }

    @DeleteMapping("/s/account/{number}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long number,
                                           @AuthenticationPrincipal LoginUser loginUser) {
        accountService.deleteAccount(number, loginUser.getUser().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "계좌삭제 성공", null), HttpStatus.OK);
    }

    @PostMapping("/account/deposit")
    public ResponseEntity<?> depositAccount(@RequestBody @Valid AccountDepositReqDto accountDepositReqDto, BindingResult bindingResult) {
        AccountDepositResDto accountDepositResDto = accountService.depositAccount(accountDepositReqDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 입금 완료", accountDepositResDto), HttpStatus.CREATED);
    }

    @PostMapping("/s/account/withdraw")
    public ResponseEntity<?> withdrawAccount(@RequestBody @Valid AccountWithdrawReqDto accountWithdrawReqDto, BindingResult bindingResult,
                                             @AuthenticationPrincipal LoginUser loginUser) {
        AccountWithdrawResDto accountWithdrawResDto = accountService.withdraw(accountWithdrawReqDto, loginUser.getUser().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 출금 완료", accountWithdrawResDto), HttpStatus.CREATED);
    }

    @PostMapping("/s/account/transfer")
    public ResponseEntity<?> transferAccount(@RequestBody @Valid AccountTransferReqDto accountTransferReqDto, BindingResult bindingResult,
                                             @AuthenticationPrincipal LoginUser loginUser) {
        AccountTransferResDto accountTransferResDto = accountService.transfer(accountTransferReqDto, loginUser.getUser().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 이체 완료", accountTransferResDto), HttpStatus.CREATED);
    }

    @GetMapping("/s/account/{number}")
    public ResponseEntity<?> getDetailAccount(@PathVariable Long number,
                                             @RequestParam(value = "page", defaultValue = "0") Integer page,
                                             @AuthenticationPrincipal LoginUser loginUser) {
        AccountDetailResDto accountDetailResDto = accountService.getAccountDetail(number, loginUser.getUser().getId(), page);

        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 상세보기 성공", accountDetailResDto), HttpStatus.OK);
    }
}
