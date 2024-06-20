package org.sacc.backend.services;

import org.sacc.backend.config.DataBaseConfig;
import org.sacc.backend.models.Batida;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BatidaService {
    private DataBaseConfig dbConfig;

    public BatidaService() {
        dbConfig = new DataBaseConfig();
    }

    public List<Batida> readAll() throws SQLException {
        System.out.println("passou");
        String sql = "SELECT * FROM batidas";
        List<Batida> records = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Batida record = new Batida();
                record.setValue(rs.getDouble("value"));
                record.setStatus(rs.getString("status"));
                record.setExpected(rs.getString("expected"));
                records.add(record);
            }
        }
        return records;
    }

}
