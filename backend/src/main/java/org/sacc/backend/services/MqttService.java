package org.sacc.backend.services;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.util.List;
import java.util.ArrayList;

@Singleton
@Startup
public class MqttService {

    private static final String BROKER = "tcp://localhost:1883";
    private static final String CLIENT_ID = "JavaClient";
    private MqttClient client;
    private List<String> messages = new ArrayList<>();

    public MqttService() {
        try {
            this.client = new MqttClient(BROKER, CLIENT_ID);
            this.client.setCallback(new MqttCallBackService() );
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            client.connect(options);
            System.out.println("Conectado ao broker: " + BROKER);
        } catch (MqttException e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
/*
    @PostConstruct
    public void init() {

    }
*/
    public void subscribeInTopic(String topic) throws MqttException {
        if (!client.isConnected()) {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            client.connect(options);
        }
        client.subscribe(topic);
    }

    public void publishMessage(String topic, String message) {
        System.out.println("Service topic: "+ topic + "message: " + message);
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(2);
            client.publish(topic, mqttMessage);
            System.out.println("Mensagem publicada no tópico: " + topic);
        } catch (MqttException e) {
            System.out.println("Erro ao publicar mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<String> getMessages() {
        synchronized (messages) {
            return new ArrayList<>(messages);
        }
    }

    public void clearMessages() {
        synchronized (messages) {
            messages.clear();
        }
    }
}
