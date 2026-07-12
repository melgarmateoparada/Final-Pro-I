package org.proIII.appManejoImagenes.postgres.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class TagImage {
    private static final Logger logger = LogManager.getRootLogger();
    private String url, user, password, sql;
    public TagImage(){
        url = "jdbc:postgresql://localhost:5434/Gestor_imagenes";
        user = "user3";
        password = "password3";
    }

    public int getTagIdByName(String category) {
        sql = "SELECT buscar_id_por_categoria(?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            logger.error("Error while querying the database: {}", e.getMessage());
            return -1;
        }
    }
}
