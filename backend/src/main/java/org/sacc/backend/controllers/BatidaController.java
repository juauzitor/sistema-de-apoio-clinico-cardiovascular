package org.sacc.backend.controllers;


import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sacc.backend.models.Batida;
import org.sacc.backend.services.BatidaService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "BatidaController", urlPatterns = {"/batida", "/batida/*"})
public class BatidaController extends HttpServlet {
    private BatidaService batidaService;

    @Override
    public void init() throws ServletException{
        batidaService = new BatidaService();
    }

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
            try {
                List<Batida> batidas = batidaService.readAll();
                JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
                for (Batida batida : batidas) {
                    JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder()
                            .add("value", batida.getValue())
                            .add("status", batida.getStatus())
                            .add("expected", batida.getExpected());
                    jsonArrayBuilder.add(jsonObjectBuilder);
                }
                JsonObject jsonResponse = Json.createObjectBuilder()
                        .add("batidas", jsonArrayBuilder)
                        .build();

                resp.getWriter().println(jsonResponse.toString());
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().println("{\"error\":\"Failed to fetch data\"}");
                e.printStackTrace();
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println("{\"error\":\"Page not found\"}");
        }
    }

}
