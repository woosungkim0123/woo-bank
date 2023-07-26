package shop.woosung.bank.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.dto.ResponseDto;
import shop.woosung.bank.service.AccountService;

import javax.validation.Valid;

import static shop.woosung.bank.dto.account.AccountReqDto.*;
import static shop.woosung.bank.dto.account.AccountResDto.*;


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


}
