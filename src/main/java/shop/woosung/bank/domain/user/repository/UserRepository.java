package shop.woosung.bank.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.woosung.bank.domain.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
