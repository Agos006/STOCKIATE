package utils;

import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) {

        try (Connection conexion = ConexionBD.conectar()) {

            if (conexion != null) {
                System.out.println("¡CONEXIÓN EXITOSA CON MYSQL!");
                System.out.println("Base de datos: " +
                        conexion.getCatalog());
            }

        } catch (SQLException e) {
            System.out.println("ERROR AL CONECTAR CON MYSQL:");
            e.printStackTrace();
        }
    }
}