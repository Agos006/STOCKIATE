package repository;

import model.Movimiento;
import model.Producto;
import model.TipoMovimiento;
import utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MovimientoRepository {

    // Por ahora utilizamos el usuario administrador.
    private static final int ID_USUARIO_POR_DEFECTO = 1;

    public void registrar(Movimiento movimiento) {

        String sql = """
                INSERT INTO movimientos
                (id_producto, id_usuario, tipo_movimiento,
                 cantidad, fecha, observacion)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexion = ConexionBD.conectar()) {

            if (conexion == null) {
                throw new SQLException("No se pudo conectar con MySQL.");
            }

            try (PreparedStatement sentencia =
                         conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                sentencia.setInt(1, movimiento.getProducto().getId());
                sentencia.setInt(2, ID_USUARIO_POR_DEFECTO);
                sentencia.setString(3, movimiento.getTipo().name());
                sentencia.setInt(4, movimiento.getCantidad());
                sentencia.setTimestamp(5, Timestamp.valueOf(movimiento.getFecha()));
                sentencia.setString(6, "");

                sentencia.executeUpdate();

                try (ResultSet claves = sentencia.getGeneratedKeys()) {
                    if (claves.next()) {
                        movimiento.setId(claves.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el movimiento: "
                    + e.getMessage(), e);
        }
    }

    public List<Movimiento> listarPorProducto(Producto producto) {

        List<Movimiento> resultado = new ArrayList<>();

        String sql = """
                SELECT id_movimiento, tipo_movimiento, cantidad, fecha
                FROM movimientos
                WHERE id_producto = ?
                ORDER BY fecha DESC
                """;

        try (Connection conexion = ConexionBD.conectar()) {

            if (conexion == null) {
                throw new SQLException("No se pudo conectar con MySQL.");
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {

                sentencia.setInt(1, producto.getId());

                try (ResultSet rs = sentencia.executeQuery()) {

                    while (rs.next()) {

                        Movimiento movimiento = new Movimiento(
                                rs.getInt("id_movimiento"),
                                producto,
                                TipoMovimiento.valueOf(rs.getString("tipo_movimiento")),
                                rs.getInt("cantidad"),
                                rs.getTimestamp("fecha").toLocalDateTime()
                        );

                        resultado.add(movimiento);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar el historial: "
                    + e.getMessage(), e);
        }

        return resultado;
    }
}