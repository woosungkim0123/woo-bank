package shop.woosung.bank.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.dto.ResponseDto;
import shop.woosung.bank.dto.account.AccountReqDto;
import shop.woosung.bank.dto.account.AccountResDto;
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

}
