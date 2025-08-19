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


	// Spring injects the service interface and the gson bean
	private final OrderService orderService;
	private final Gson gson;

	@Bean // This code will run automatically when the application starts
	public CommandLineRunner run() {
		return args -> {
			System.out.println("### STARTING EVENT PROCESSING ###");
			// Read the events.json file from the resources folder
			ClassPathResource resource = new ClassPathResource("events.json");
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
				// Read all lines from the file
				String jsonContent = reader.lines().collect(Collectors.joining(System.lineSeparator()));
				// Split file content into individual JSON objects (one per line)
				for (String line : jsonContent.split(System.lineSeparator())) {
					if (line.trim().isEmpty()) continue;
					EventDTO event = gson.fromJson(line, EventDTO.class);
					if (event != null) {
						// Use the service to process the event
						orderService.processEvent(event);
					}
				}
			}
			System.out.println("### EVENT PROCESSING COMPLETE ###");
		};
	}
}
