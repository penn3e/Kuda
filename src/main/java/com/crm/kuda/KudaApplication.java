package com.crm.kuda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
public class KudaApplication {
	public static void main(String[] args) {
		// Version avec contexte et info de démarrage
		ApplicationContext ctx = SpringApplication.run(KudaApplication.class, args);

		Environment env = ctx.getEnvironment();
		String appName = env.getProperty("spring.application.name", "Kuda CRM");
		String port = env.getProperty("server.port", "9090");

		System.out.println("\n----------------------------------------------------------");
		System.out.println(appName + " is running! Access URLs:");
		System.out.println("Local: \t\thttp://localhost:" + port + "/");
		System.out.println("Profiles: \t" + Arrays.toString(env.getActiveProfiles()));
		System.out.println("----------------------------------------------------------\n");
	}
}