package shop.woosung.bank.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.woosung.bank.common.ApiResponse;
import shop.woosung.bank.user.controller.dto.JoinRequestDto;
import shop.woosung.bank.user.controller.port.UserService;
import shop.woosung.bank.user.service.dto.JoinResponseDto;

import javax.validation.Valid;

import static shop.woosung.bank.user.converter.UserControllerToServiceConverter.joinRequestConvert;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<JoinResponseDto>> join(@RequestBody @Valid JoinRequestDto joinRequestDto) {
        JoinResponseDto joinResponseDto = userService.join(joinRequestConvert(joinRequestDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("회원가입 성공", joinResponseDto));
    }
}
