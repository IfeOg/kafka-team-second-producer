package com.example.lynxlaststand.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.lynxlaststand.model.Client;

@Configuration
public class KafkaProducerConfig {
	
	private String bootstrapServers = "127.0.0.1:9092";
	
	@Bean
	public ProducerFactory<String, Client> clientProducerFactory(){
		Map<String, Object> configProperties = new HashMap<>();
		configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,  JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProperties);
	}
	
	@Bean
	public KafkaTemplate<String, Client> userKafkaTemplate(){
		return new KafkaTemplate<>(clientProducerFactory());
	}

}
