package shop.woosung.bank.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.domain.user.User;
import shop.woosung.bank.domain.user.repository.UserRepository;
import shop.woosung.bank.dto.user.UserReqDto;
import shop.woosung.bank.dto.user.UserResDto;
import shop.woosung.bank.handler.ex.CustomApiException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public UserResDto.JoinResDto join(UserReqDto.JoinReqDto signUpReqDto) {
        Optional<User> user = userRepository.findByUsername(signUpReqDto.getUsername());
        if(user.isPresent()) {
            throw new CustomApiException("동일한 username이 존재합니다.");
        }
        User userPS = userRepository.save(signUpReqDto.toEntity(passwordEncoder));

        return new UserResDto.JoinResDto(userPS);
    }
}
