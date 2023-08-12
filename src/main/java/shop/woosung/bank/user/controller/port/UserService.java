package shop.woosung.bank.user.controller.port;

import shop.woosung.bank.user.service.dto.JoinRequestServiceDto;
import shop.woosung.bank.user.service.dto.JoinResponseDto;

public interface UserService {
    JoinResponseDto join(JoinRequestServiceDto signUpReqDto);
}
