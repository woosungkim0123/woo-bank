package shop.woosung.bank.config.jwt;

import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.infrastructure.UserEntity;
import shop.woosung.bank.user.UserRole;

import java.util.HashMap;
import java.util.Map;

public class JwtTestHolder implements JwtHolder {
    private static final Map<String, String> ROLE_TOKEN_MAP = new HashMap<>();

    static {
        ROLE_TOKEN_MAP.put("ADMIN", JwtVO.ADMIN_TEST_TOKEN);
        ROLE_TOKEN_MAP.put("CUSTOMER", JwtVO.CUSTOMER_TEST_TOKEN);
    }

    @Override
    public String createToken(LoginUser loginUser) {
        String role = loginUser.getUser().getRole().name();
        return ROLE_TOKEN_MAP.get(role);
    }

    @Override
    public User verifyToken(String token) {
        for (Map.Entry<String, String> entry : ROLE_TOKEN_MAP.entrySet()) {
            if (entry.getValue().equals(token)) {
                UserRole userRole = UserRole.valueOf(entry.getKey());
                return User.builder().role(userRole).build();
            }
        }
        return null;
    }
}
