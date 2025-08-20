package com.example.Ecommerce;

import com.example.Ecommerce.dto.EventDTO;
import com.example.Ecommerce.services.service.OrderService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@SpringBootApplication
@RequiredArgsConstructor
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}



	private final OrderService orderService;
	private final Gson gson;

	@Bean
	public CommandLineRunner run() {
		return args -> {
			System.out.println("------------------------------STARTING EVENT PROCESSING-----------------------------------------------");

			ClassPathResource resource = new ClassPathResource("events.json");
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

				String jsonContent = reader.lines().collect(Collectors.joining(System.lineSeparator()));

				for (String line : jsonContent.split(System.lineSeparator())) {
					if (line.trim().isEmpty()) continue;
					EventDTO event = gson.fromJson(line, EventDTO.class);
					if (event != null) {
						orderService.processEvent(event);
					}
				}
			}
			System.out.println("--------------------------------------------EVENT PROCESSING COMPLETE-----------------------------------");
		};
	}
}
