import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodeTest {

    @Test
    public void PasswordEncoder() {
        String password = "123456";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        System.out.println(hashedPassword);
    }

    @Test
    public void PasswordDecoder() {
        String encodedPassword = "$2a$10$S7Zyh3xQUNAnLr81lK7.k.feg09O6P9JSpZQh4RpEy.Ig34wFryQu";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.matches("123456", encodedPassword)); // true
        System.out.println(passwordEncoder.matches("111111", encodedPassword)); // false
    }
}
