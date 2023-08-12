package shop.woosung.bank.mock;

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
}
