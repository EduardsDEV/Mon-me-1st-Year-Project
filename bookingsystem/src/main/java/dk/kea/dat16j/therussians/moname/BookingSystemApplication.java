package dk.kea.dat16j.therussians.moname;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BookingSystemApplication {

	public static void main(String[] args) {
        SpringApplication.run(BookingSystemApplication.class, args);
	}

    @Bean
    public PasswordEncoder requestPasswordEncoder() {
	    return new BCryptPasswordEncoder();
    }
}
