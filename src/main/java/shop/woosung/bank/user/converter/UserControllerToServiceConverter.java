package shop.woosung.bank.user.converter;

import shop.woosung.bank.user.controller.dto.JoinRequestDto;
import shop.woosung.bank.user.service.dto.JoinRequestServiceDto;

public class UserControllerToServiceConverter {

    public static JoinRequestServiceDto joinRequestConvert(JoinRequestDto joinRequestDto){
        return JoinRequestServiceDto.builder()
                .email(joinRequestDto.getEmail())
                .password(joinRequestDto.getPassword())
                .name(joinRequestDto.getName())
                .build();
    }
}
