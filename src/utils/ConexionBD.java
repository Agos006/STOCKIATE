package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/stockiate?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "maryyc";
<<<<<<< HEAD
    private static final String PASSWORD = "";
=======
    private static final String PASSWORD = System.getenv("STOCKIA_DB_PASSWORD");
>>>>>>> e786fc3 (Actualiza persistencia de productos y movimientos)

    public static Connection conectar() {
        try {
            Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("¡Conexión con MySQL exitosa!");
            return conexion;

        } catch (SQLException e) {
            System.out.println("Error al conectar con MySQL: " + e.getMessage());
            return null;
        }
    }
}
