package shop.woosung.bank.mock.util;

import shop.woosung.bank.common.service.port.PasswordEncoder;

public class FakePasswordEncoder implements PasswordEncoder {

    private final String encodedPassword;

    public FakePasswordEncoder(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    @Override
    public String encode(String password) {
        return encodedPassword;
    }

    @Override
    public boolean matches(String password, String encodedPassword) {
        return this.encodedPassword.equals(encodedPassword);
    }
}
