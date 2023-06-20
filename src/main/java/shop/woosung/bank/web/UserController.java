package shop.woosung.bank.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.woosung.bank.dto.ResponseDto;
import shop.woosung.bank.service.UserService;

import javax.validation.Valid;

import static shop.woosung.bank.dto.user.UserReqDto.*;
import static shop.woosung.bank.dto.user.UserResDto.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid JoinReqDto joinReqDto, BindingResult bindingResult) {
        JoinResDto joinResDto = userService.join(joinReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", joinResDto), HttpStatus.CREATED);
    }
}
