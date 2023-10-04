package shop.woosung.bank.mock.util;

import org.springframework.stereotype.Component;
import shop.woosung.bank.common.service.port.PasswordEncoder;

public class FakePasswordEncoder implements PasswordEncoder {

    private String encodedPassword;

    public FakePasswordEncoder(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public FakePasswordEncoder() {
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    @Override
    public String encode(String password) {
        return encodedPassword;
    }

    @Override
    public boolean matches(String password, String encodedPassword) {
        System.out.println("password: " + password);
        System.out.println("encodedPassword: " + encodedPassword);
        return this.encodedPassword.equals(encodedPassword);
    }
}
