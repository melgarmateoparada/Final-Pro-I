package org.proIII.appManejoImagenes.postgres;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class LoadImage {
    private final Logger logger = LogManager.getRootLogger();
    private String url, user, password, sql;
    private byte[] image;

    public LoadImage() {
        url = "jdbc:postgresql://localhost:5434/Gestor_imagenes";
        user = "user3";
        password = "password3";

    }

    public void loadImageToModify(String imageName) {
         sql = "SELECT obtener_imagen_por_nombre_funcion(?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, imageName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()&& rs.getBytes(1) != null) {
                    image = rs.getBytes(1);
                    logger.info("Image successfully loaded: " + imageName);
                } else {
                    logger.warn("Image not found: " + imageName);
                }
            }
        } catch (SQLException e) {
            logger.error("Error loading image from database: ", e);
        }
    }

    public int authorImage(String imageName){
        sql = "SELECT obtener_id_autor_por_imagen(?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, imageName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int autorId = rs.getInt(1);
                    logger.info("Author ID for image " + imageName + " is: " + autorId);
                    return autorId;
                } else {
                    logger.warn("No author found for image: " + imageName);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching author ID: ", e);
        }
        return 0;
    }

    public boolean validateAuthorPassword(int autorId, String inputPassword) {
        String sql = "SELECT validar_contraseña_por_autor(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurar parámetros de la consulta
            stmt.setInt(1, autorId);          // ID del autor
            stmt.setString(2, inputPassword); // Contraseña proporcionada por el usuario

            // Ejecutar la consulta y obtener el resultado
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    boolean isValid = rs.getBoolean(1); // Obtener el resultado de validación
                    if (isValid) {
                        logger.info("Password validation successful for author ID " + autorId);
                    } else {
                        logger.warn("Password validation failed for author ID " + autorId);
                    }
                    return isValid; // Retornar si la validación fue exitosa
                } else {
                    logger.warn("No result returned for password validation of author ID: " + autorId);
                }
            }
        } catch (SQLException e) {
            logger.error("Error validating password for author ID " + autorId, e);
        }
        return false; // Retornar falso si ocurre algún error o no hay coincidencia
    }







    public  File byteArrayToFile(byte[] data, String filePath) throws IOException {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("El arreglo de bytes está vacío o es nulo.");
        }

        File file = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
        }

        return file;
    }


    public void loadImageFromModify(String imageName) {
        sql = "SELECT obtener_imagen_modificada_por_nombre_funcion(?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, imageName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()&& rs.getBytes(1) != null) {
                    image = rs.getBytes(1);
                    logger.info("Image successfully loaded: " + imageName);
                } else {
                    logger.warn("Image not found: " + imageName);
                }
            }
        } catch (SQLException e) {
            logger.error("Error loading image from database: ", e);
        }
    }
    public byte[] getImage() {
        return image;
    }
    public int authorImageModified(String imageName){
        sql = "SELECT obtener_id_autor_por_nombre_modificacion(?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, imageName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int autorId = rs.getInt(1);
                    logger.info("Author ID for image " + imageName + " is: " + autorId);
                    return autorId;
                } else {
                    logger.warn("No author found for image: " + imageName);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching author ID: ", e);
        }
        return 0;
    }

}

