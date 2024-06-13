package org.sacc.backend.controllers;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.sacc.backend.models.Patient;
import org.sacc.backend.services.MqttService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "MqttController", urlPatterns = {"/mqtt", "/mqtt/*"})
public class MqttController extends HttpServlet {

    @Inject
    private MqttService mqttService = new MqttService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();

        resp.setContentType("application/json");

        // Log para debug
        System.out.println("Request URI: " + requestURI);
        System.out.println("Context Path: " + contextPath);
        System.out.println("Servlet Path: " + servletPath);
        System.out.println("Path Info: " + pathInfo);

        // Ajuste para verificar o caminho completo
        if (pathInfo == null || pathInfo.equals("/")) {
            //mqttService.publishMessage("ECG", "message");
            try {
                mqttService.subscribeInTopic("ECG");
                resp.getWriter().println("{\"message\":\"MQTT\"}");
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        } else if (pathInfo.equals("/status")) {
            resp.getWriter().println("{\"status\":\"MQTT Service is running\"}");
        } else if (pathInfo.equals("/messages")) {
        List<String> messages = mqttService.getMessages();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (String message : messages) {
            jsonArrayBuilder.add(message);
        }
        resp.getWriter().println(jsonArrayBuilder.build().toString());
    } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println("{\"error\":\"Page not found\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        // Leitura do corpo da requisição JSON
        try (InputStream is = req.getInputStream(); JsonReader jsonReader = Json.createReader(is)) {
            JsonObject json = jsonReader.readObject();

            String topic = json.getString("topic", "ECG");
            String message = json.getString("message", "");
            System.out.println("topic: " + topic + "message: " + message);
            if (message.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(Json.createObjectBuilder().add("error", "Message is required").build());
                return;
            }

            mqttService.publishMessage(topic, message);

            resp.getWriter().println(Json.createObjectBuilder().add("status", "Mensagem publicada com sucesso").build());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(Json.createObjectBuilder().add("error", "Internal Server Error").build());
        }
    }
}
