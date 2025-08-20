package com.example.Ecommerce.config;

import com.example.Ecommerce.dto.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        return modelMapper;

    }
    @Bean // Tells Spring to manage this Gson object for JSON parsing
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Custom parser for Instant type
        gsonBuilder.registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (json, typeOfT, context) -> Instant.parse(json.getAsString()));
        // Custom parser to handle different event DTO types
        gsonBuilder.registerTypeAdapter(EventDTO.class, (JsonDeserializer<EventDTO>) (json, typeOfT, context) -> {
            JsonObject jsonObject = json.getAsJsonObject();
            String type = jsonObject.get("eventType").getAsString();
            switch (type) {
                case "OrderCreated":
                    return context.deserialize(jsonObject, OrderCreatedEventDTO.class);
                case "PaymentReceived":
                    return context.deserialize(jsonObject, PaymentReceivedEventDTO.class);
                case "ShippingScheduled":
                    return context.deserialize(jsonObject, ShippingScheduledEventDTO.class);
                case "OrderCancelled":
                    return context.deserialize(jsonObject, OrderCancelledEventDTO.class);

                default:
                    return null;
            }
        });
        return gsonBuilder.create();
    }
}