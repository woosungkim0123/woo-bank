package shop.woosung.bank.common.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import shop.woosung.bank.common.service.port.PasswordEncoder;

@RequiredArgsConstructor
@Component
public class PasswordEncoderImpl implements PasswordEncoder {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String encode(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    @Override
    public boolean matches(String password, String encodedPassword) {
        return bCryptPasswordEncoder.matches(password, encodedPassword);
    }
}
