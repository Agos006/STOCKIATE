package utils;

import java.sql.Connection;

public class PruebaConexion {

    public static void main(String[] args) {

        Connection conexion = ConexionBD.conectar();

        if (conexion != null) {
            System.out.println("Stockia está conectada a MySQL.");

            try {
                conexion.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar la conexión.");
            }
        } else {
            System.out.println("No se pudo conectar con la base de datos.");
        }
    }
}