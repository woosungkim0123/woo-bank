package shop.woosung.bank.user.service.port;

import shop.woosung.bank.user.domain.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    Optional<User> findById(long id);

    User save(User user);
}
