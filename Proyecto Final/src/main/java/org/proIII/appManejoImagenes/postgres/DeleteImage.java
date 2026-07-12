package org.proIII.appManejoImagenes.postgres;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DeleteImage {
    private static final Logger logger = LogManager.getRootLogger();
    private String url, user, password, sql;
    private boolean isThere;
    private int imageId;
    public  DeleteImage() {
        url = "jdbc:postgresql://localhost:5434/Gestor_imagenes";
        user = "user3";
        password = "password3";
        isThere = false;
    }
    public int getTagIdByName(String imagenName) {
        sql = "SELECT buscar_id_imagen_por_nombre(?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, imagenName);
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

    public boolean deleteImageById(int imageId) {
        String sql = "CALL actualizar_estado_imagen(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, imageId);
            stmt.setBoolean(2, false);
            stmt.execute();
            logger.info("Image with ID '{}' has been deactivated.", imageId);
            return true;
        } catch (SQLException e) {
            logger.error("Error calling stored procedure for image ID '{}': {}", imageId, e.getMessage());
            return false;
        }
    }


    public int getImageIdByName(String imageName) {
        String sql = "SELECT id_imagen FROM Imagen WHERE nombre = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, imageName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_imagen");
            }
            return -1; // Imagen no encontrada
        } catch (SQLException e) {
            logger.error("Error fetching image ID by name '{}': {}", imageName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
