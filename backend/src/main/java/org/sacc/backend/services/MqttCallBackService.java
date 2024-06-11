package org.sacc.backend.services;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttCallBackService implements MqttCallback {
    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Conexão perdida com o broker!!!");
    }

    @Override
    public void messageArrived(String topico, MqttMessage mqttMessage) throws Exception {
        System.out.println("Conteudo da mensagem do " + topico + ": " + new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Entrega completa: " + token);
    }
}
