package org.proIII.appManejoImagenes.postgres.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class AuthorPostgres {
    private static final Logger logger = LogManager.getRootLogger();
    private String url, user, password, sql;
    public AuthorPostgres(){
        url = "jdbc:postgresql://localhost:5434/Gestor_imagenes";
        user = "user3";
        password = "password3";
    }


    public boolean searchAuthor(String name) {
        sql = "SELECT 1 FROM Autor WHERE nombre = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            logger.error("Error while querying the database: {}", e.getMessage());
            return false;
        }
    }


    //Insert without duplicated mail
    public void insertAuthor(String name, String passwordUser, String email, String biography) {
        sql = "CALL registrar_autor(?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, passwordUser);
            stmt.setString(3, email);
            stmt.setString(4, biography);
            stmt.execute();
            logger.info("Author '{}' registered successfully.", name);
        } catch (SQLException e) {
            // Manejo de error cuando el correo ya existe o cualquier otra excepción
            if (e.getSQLState().equals("P0001")) { // Código SQL para una excepción RAISE EXCEPTION
                logger.warn("Failed to register author: {}", e.getMessage());
            } else {
                logger.error("Error while executing stored procedure: {}", e.getMessage());
            }
        }
    }


    public int getAuthorIdByName(String name) {

         sql = "SELECT buscar_id_por_nombre(?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            logger.error("Error while querying the database: {}", e.getMessage());
            return -1; // Error en la consulta
        }
    }

}