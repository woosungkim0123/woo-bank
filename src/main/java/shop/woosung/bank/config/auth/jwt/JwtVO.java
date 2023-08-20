package shop.woosung.bank.config.auth.jwt;

public interface JwtVO {
    String SECRET = "woosung1";
    int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER = "Authorization";

    String ADMIN_TEST_TOKEN = "admintesttoken";
    String CUSTOMER_TEST_TOKEN = "customertesttoken";
}
