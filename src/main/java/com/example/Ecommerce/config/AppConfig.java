package com.example.Ecommerce.config;

import com.example.Ecommerce.dto.*;
import com.example.Ecommerce.entity.OrderItem;
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
        modelMapper.createTypeMap(ItemDTO.class, OrderItem.class).addMappings(mapper -> {

            mapper.skip(OrderItem::setId);
            mapper.map(ItemDTO::getItemId, OrderItem::setProductId);
        });
        return modelMapper;

    }
    @Bean
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (json, typeOfT, context) -> Instant.parse(json.getAsString()));

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