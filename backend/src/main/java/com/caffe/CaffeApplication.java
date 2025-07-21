package com.caffe;

import com.caffe.global.config.AppConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class CaffeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaffeApplication.class, args);
	}

	@Bean
	public ApplicationRunner applicationRunner() {
		return args -> {
			System.out.println();
			System.out.println("======================================================");
			System.out.println("           )  )  )");
			System.out.println("          (  (  (");
			System.out.println("       _________");
			System.out.println("      .-----------.");
			System.out.println("     |   NBE6-8  |===");
			System.out.println("     |  TEAM 12  |  ||");
			System.out.println("     |           |===");
			System.out.println("     '-----------'");
			if(AppConfig.isDevMode()){
				System.out.println("=====================");
				System.out.println("애플리케이션이 'dev' 모드로 실행되었습니다.");
				System.out.println("=====================");
			}

			if (AppConfig.isTestMode()) {
				System.out.println("=====================");
				System.out.println("애플리케이션이 'test' 모드로 실행되었습니다.");
				System.out.println("=====================");
			}
		};
	}

}
