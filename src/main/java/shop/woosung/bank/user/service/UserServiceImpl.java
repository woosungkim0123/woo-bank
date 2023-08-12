package shop.woosung.bank.user.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.common.exception.EmailAlreadyInUseException;
import shop.woosung.bank.common.service.port.PasswordEncoder;
import shop.woosung.bank.user.domain.UserRole;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.controller.port.UserService;
import shop.woosung.bank.user.service.dto.JoinRequestServiceDto;
import shop.woosung.bank.user.service.dto.JoinResponseDto;
import shop.woosung.bank.user.service.port.UserRepository;

import java.util.Optional;

import static shop.woosung.bank.user.util.UserServiceToDomainConverter.userCreateConvert;

@Builder
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JoinResponseDto join(JoinRequestServiceDto joinRequestServiceDto) {
        checkDuplicateEmail(joinRequestServiceDto.getEmail());

        User user = User.join(userCreateConvert(joinRequestServiceDto, UserRole.CUSTOMER), passwordEncoder);
        User newUser = userRepository.save(user);

        return JoinResponseDto.from(newUser);
    }

    private void checkDuplicateEmail(String requestEmail) {
        Optional<User> existingUser = userRepository.findByEmail(requestEmail);
        if (existingUser.isPresent()) {
            throw new EmailAlreadyInUseException(requestEmail, existingUser.get().getEmail(), "이미 사용 중인 이메일 입니다.");
        }
    }
}
