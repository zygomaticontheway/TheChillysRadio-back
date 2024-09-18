import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptDemo {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "qwerty";
//
//        String securePassword = encoder.encode(password);
//        System.out.println(securePassword);


//        $2a$10$.f5ScnQNqyw9bwQqsWbEQujLbMYw4Ap9TCFD2ldrSXAIcj17updP2

        boolean matches1 = encoder.matches(password, "$2a$10$.f5ScnQNqyw9bwQqsWbEQujLbMYw4Ap9TCFD2ldrSXAIcj17updP2");
        System.out.println(matches1);

    }
}
