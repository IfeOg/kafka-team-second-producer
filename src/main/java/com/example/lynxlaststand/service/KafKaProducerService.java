package com.example.lynxlaststand.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.example.lynxlaststand.model.Client;

@Service
public class KafKaProducerService {

	private static final Logger logger = LoggerFactory.getLogger(KafKaProducerService.class);

	private String topicName = "client-2-log";

	@Autowired
	private KafkaTemplate<String, Client> clientKafkaTemplate;

	public void saveCreateClientLog() {

		String jdbcUrl = "jdbc:sqlite:/C:\\Users\\brain\\Lynx-Producer-2\\kafka-team-producer-2\\customer_risk.db";


		try {
			Connection connection = DriverManager.getConnection(jdbcUrl);
			String sql = "SELECT * FROM customer_risk";
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			Client client = new Client();

			while (result.next()) {

				// assign values from database to newly created client object
				client.setId(Integer.valueOf(result.getString("ID")));
				client.setCrypto(result.getBoolean("Crypto"));
				client.setGambling(result.getBoolean("Gambling"));
				client.setAtRisk(result.getBoolean("At Risk"));

				//System.out.println(result.getString("Crypto"));
				//System.out.println(Boolean.valueOf(result.getString("Crypto")));


				ListenableFuture<SendResult<String, Client>> future = this.clientKafkaTemplate.send(topicName, client);

				future.addCallback(new ListenableFutureCallback<SendResult<String, Client>>() {
					@Override
					public void onSuccess(SendResult<String, Client> result) {
						logger.info(
								"Client created: " + client + " with offset: " + result.getRecordMetadata().offset());
					}

					@Override
					public void onFailure(Throwable ex) {
						logger.error("Client created : " + client, ex);

					}

				});

			}

		} catch (SQLException e) {
			System.out.println("Error connecting to SQLite database");
			e.printStackTrace();
		}
	}

}
