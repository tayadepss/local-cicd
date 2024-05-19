package local.cicd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class LocalCicdApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocalCicdApplication.class, args);
	}

}
