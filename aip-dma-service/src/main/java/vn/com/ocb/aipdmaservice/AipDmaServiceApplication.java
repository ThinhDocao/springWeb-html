package vn.com.ocb.aipdmaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"vn.com.ocb"})
@EnableCaching
public class AipDmaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AipDmaServiceApplication.class, args);
	}

}
