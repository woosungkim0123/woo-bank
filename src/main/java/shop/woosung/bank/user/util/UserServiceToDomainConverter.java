package shop.woosung.bank.user.util;

import shop.woosung.bank.user.domain.UserRole;
import shop.woosung.bank.user.domain.UserCreate;
import shop.woosung.bank.user.service.dto.JoinRequestServiceDto;

public class UserServiceToDomainConverter {

    static public UserCreate userCreateConvert(JoinRequestServiceDto joinRequestServiceDto, UserRole role){
        return UserCreate.builder()
                .email(joinRequestServiceDto.getEmail())
                .password(joinRequestServiceDto.getPassword())
                .name(joinRequestServiceDto.getName())
                .role(role)
                .build();
    }
}
