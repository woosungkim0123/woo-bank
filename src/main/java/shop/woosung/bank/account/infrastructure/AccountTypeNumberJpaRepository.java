package shop.woosung.bank.account.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.woosung.bank.account.infrastructure.entity.AccountTypeNumberEntity;

public interface AccountTypeNumberJpaRepository extends JpaRepository<AccountTypeNumberEntity, String> {

}
