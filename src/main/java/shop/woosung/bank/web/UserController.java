package shop.woosung.bank.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.woosung.bank.dto.ResponseDto;
import shop.woosung.bank.dto.user.UserReqDto;
import shop.woosung.bank.dto.user.UserResDto;
import shop.woosung.bank.service.UserService;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

        if(bindingResult.hasErrors()) {
            Map<String, String> errorMap = bindingResult.getFieldErrors().stream()
                    .peek(error -> log.error("error: {}", error.getDefaultMessage()))
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return new ResponseEntity<>(new ResponseDto<>(-1, "유효성 검사 실패", errorMap), HttpStatus.BAD_REQUEST);
        }


        JoinResDto joinResDto = userService.join(joinReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", joinResDto), HttpStatus.CREATED);
    }
    /*
    Spring은 Reflection을 사용하여 객체의 필드나 setter 메서드에 값을 설정하기 때문에, 생성자나 setter 메서드의 접근 제어자는 public이어야 합니다.

어떤 방식을 선택할지는 개발자의 판단에 달려있습니다. 일반적으로 생성자 방식은 객체를 불변으로 만들고자 할 때 주로 사용되고, setter 방식은 가변 객체의 경우 사용됩니다.

/ username=test&password=1234 이런식으로 받기 떄문에 RequestBody가 있어야함
    비밀번호가 30자리가 넘어가도 통과됨 - 유효성 검사

    내가원하는 dto 앞에 valid
    혹시 얘가 검사에 통과하지못하면 BindingResult에 모든 오류가 담김

    매번 이렇게 적기 귀찮으니 AOP로 처리해보자

    AOP를 사용해보자
     */

}
