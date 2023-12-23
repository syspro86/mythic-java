package net.zsoo.mythic.mythicweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableRetry
public class MythicWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MythicWebApplication.class, args);
	}

}
