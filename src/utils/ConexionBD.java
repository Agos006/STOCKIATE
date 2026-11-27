package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/stockiate?useSSL=false&serverTimezone=UTC";

    private static final String USUARIO = "maryyc";

    private static final String CONTRASENA = "95101093Mary";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}