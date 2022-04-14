package com.example.lynxlaststand.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.lynxlaststand.model.Client;
import com.example.lynxlaststand.service.KafKaProducerService;

@Component
public class RunAtStartUp {

	private final KafKaProducerService producerService;

	@Autowired
	public RunAtStartUp(KafKaProducerService producerService) {
		this.producerService = producerService;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void runAfterStartup() {
    	
        this.producerService.saveCreateClientLog();

	}
}
