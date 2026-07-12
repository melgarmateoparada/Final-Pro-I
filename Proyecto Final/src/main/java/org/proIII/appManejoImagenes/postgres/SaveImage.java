package org.proIII.appManejoImagenes.postgres;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class SaveImage {

    private static final Logger logger = LogManager.getRootLogger();

    private String url, user, password, sql;

    public SaveImage(){
        url = "jdbc:postgresql://localhost:5434/Gestor_imagenes";
        user = "user3";
        password = "password3";
    }


    public void insertImage(byte[] imageData, String imageName, int authorId, int tagId) {
         sql = "CALL registrar_imagen(?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBytes(1, imageData);
            stmt.setString(2, imageName);
            stmt.setInt(3, authorId);
            stmt.setInt(4, tagId);

            stmt.execute();
            logger.info("Image '{}' registered successfully.", imageName);
        } catch (SQLException e) {
            if (e.getSQLState().equals("P0001")) { // Código SQL para excepciones RAISE EXCEPTION
                logger.warn("Failed to register image: {}", e.getMessage());
            } else {
                logger.error("Error while executing stored procedure: {}", e.getMessage());
            }

        }
    }
    public void insertImagetoModified(int idImagen, int idAutor, byte[] nuevaImagen, String nombreModificacion, String descripcion) {
        String sql = "CALL insertar_imagen_modificada(?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             CallableStatement stmt = conn.prepareCall(sql)) {

            // Configurar los parámetros del procedimiento almacenado
            stmt.setInt(1, idImagen);
            stmt.setInt(2, idAutor);
            stmt.setBytes(3, nuevaImagen);
            stmt.setString(4, nombreModificacion);
            stmt.setString(5, descripcion);

            // Ejecutar el procedimiento
            stmt.execute();

            logger.info("La imagen modificada fue insertada exitosamente.");
        } catch (SQLException e) {
            logger.error("Error al llamar al procedimiento almacenado 'insertar_imagen_modificada': ", e);
        }
    }

}
