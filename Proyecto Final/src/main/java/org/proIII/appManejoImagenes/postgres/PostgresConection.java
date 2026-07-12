package org.proIII.appManejoImagenes.postgres;
 import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConection {
    private static final String URL = "jdbc:postgresql://localhost:5434/Gestor_imagenes";
    private static final String USUARIO = "user3";
    private static final String CONTRASENA = "password3";
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        }
    }


