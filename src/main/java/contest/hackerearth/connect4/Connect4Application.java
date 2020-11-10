package contest.hackerearth.connect4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class Connect4Application {

	public static void main(String[] args) {
		SpringApplication.run(Connect4Application.class, args);
	}

}
