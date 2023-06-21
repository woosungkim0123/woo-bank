package shop.woosung.bank.util.dummy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.woosung.bank.domain.user.User;
import shop.woosung.bank.domain.user.UserEnum;

import java.time.LocalDateTime;

public class DummyUserObject {

    protected User newUser(String username, String password, String email, String fullname, UserEnum role) {
        String encodingPassword = new BCryptPasswordEncoder().encode(password);
        return User.builder()
                .username(username)
                .password(encodingPassword)
                .email(email)
                .fullname(fullname)
                .role(role)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected User newMockUser(Long id, String username, String password, String email, String fullname, UserEnum role) {
        String encodingPassword = new BCryptPasswordEncoder().encode(password);
        return User.builder()
                .id(id)
                .username(username)
                .password(encodingPassword)
                .email(email)
                .fullname(fullname)
                .role(role)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
