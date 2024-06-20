package org.sacc.backend.services;

import org.sacc.backend.config.DataBaseConfig;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class MqttCallBackService implements MqttCallback {
    private Connection connection;

    public MqttCallBackService() {
        try {
            this.connection = new DataBaseConfig().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("sucesso de conexão");
    }

    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println("Message received:\t"+ new String(mqttMessage.getPayload()) );
        String payload = new String(mqttMessage.getPayload());
        JsonReader jsonReader = Json.createReader(new StringReader(payload));
        JsonObject json = jsonReader.readObject();
        double value = json.getJsonNumber("value").doubleValue();
        String status = json.getString("status");
        String expected = json.getString("expected");
        insertData(value, status, expected);
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }

    private void insertData(Double value, String status, String expected) {
        String query = "INSERT INTO batidas (value, status, expected) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, value);
            stmt.setString(2, status);
            stmt.setString(3, expected);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Timestamp convertStringToTimestamp(double timestampDouble) {
        long milliseconds = (long) (timestampDouble * 1000); // Convert seconds to milliseconds
        return new Timestamp(milliseconds);
    }

}
