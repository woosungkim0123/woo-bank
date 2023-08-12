package shop.woosung.bank.mock;

import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.service.port.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {

    private Long autoIncrementId = 0L;
    private final List<User> data = new ArrayList<>();
    private final LocalDateTime currentDateTime = LocalDateTime.of(2023, Month.AUGUST, 11, 15, 30, 0);

    @Override
    public Optional<User> findById(long id) {
        return data.stream().filter(item -> Objects.equals(item.getId(), id)).findAny();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return data.stream().filter(item -> Objects.equals(item.getEmail(), email)).findAny();
    }

    @Override
    public Optional<User> findByName(String name) {
        return data.stream().filter(item -> Objects.equals(item.getName(), name)).findAny();
    }

    @Override
    public User save(User user) {
        if(user.getId() == null || user.getId() == 0) {
            User newUser = user.builder()
                    .id(++autoIncrementId)
                    .email(user.getEmail())
                    .name(user.getName())
                    .password(user.getPassword())
                    .createdAt(currentDateTime)
                    .updatedAt(currentDateTime)
                    .build();
            data.add(newUser);
            return newUser;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), user.getId()));
            data.add(user);
            return user;
        }
    }
}