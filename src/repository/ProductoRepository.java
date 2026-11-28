package repository;

import model.Categoria;
import model.Producto;
import model.Proveedor;
import utils.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepository {

    public void registrar(Producto producto) {

        String sql = """
                INSERT INTO productos
                (nombre, descripcion, precio, stock_actual,
                 stock_minimo, activo, id_categoria, id_proveedor)
                VALUES (?, '', ?, ?, ?, 1, ?, ?)
                """;

        try (Connection conexion = ConexionBD.conectar()) {

            if (conexion == null) {
                throw new SQLException("No se pudo conectar con MySQL.");
            }

            conexion.setAutoCommit(false);

            try {
                int idCategoria = obtenerCategoriaId(
                        conexion, producto.getCategoria().getNombre());

                int idProveedor = obtenerProveedorId(
                        conexion, producto.getProveedor().getNombre());

                try (PreparedStatement ps = conexion.prepareStatement(
                        sql, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setString(1, producto.getNombre());
                    ps.setDouble(2, producto.getPrecio());
                    ps.setInt(3, producto.getCantidad());
                    ps.setInt(4, producto.getStock().getStockMinimo());
                    ps.setInt(5, idCategoria);
                    ps.setInt(6, idProveedor);

                    ps.executeUpdate();

                    try (ResultSet resultado = ps.getGeneratedKeys()) {
                        if (resultado.next()) {
                            producto.setId(resultado.getInt(1));
                        }
                    }
                }

                conexion.commit();

            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al registrar el producto: " + e.getMessage(), e);
        }
    }

    private int obtenerCategoriaId(Connection conexion, String nombre)
            throws SQLException {

        String consulta = "SELECT id_categoria FROM categorias WHERE nombre = ?";

        try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_categoria");
                }
            }
        }

        String insertar = "INSERT INTO categorias (nombre) VALUES (?)";

        try (PreparedStatement ps = conexion.prepareStatement(
                insertar, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombre);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new SQLException("No se pudo guardar la categoría.");
    }

    private int obtenerProveedorId(Connection conexion, String nombre)
            throws SQLException {

        String consulta = "SELECT id_proveedor FROM proveedores WHERE nombre = ?";

        try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_proveedor");
                }
            }
        }

        String insertar = "INSERT INTO proveedores (nombre) VALUES (?)";

        try (PreparedStatement ps = conexion.prepareStatement(
                insertar, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombre);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new SQLException("No se pudo guardar el proveedor.");
    }

    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();

        String sql = """
        SELECT p.id_producto, p.nombre, p.precio,
               p.stock_actual, p.stock_minimo,
               c.id_categoria, c.nombre AS categoria,
               pr.id_proveedor, pr.nombre AS proveedor,
               pr.contacto
        FROM productos p
        LEFT JOIN categorias c ON p.id_categoria = c.id_categoria
        LEFT JOIN proveedores pr ON p.id_proveedor = pr.id_proveedor
        WHERE p.activo = 1
        """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar productos.", e);
        }

        return productos;
    }

    public Producto buscarPorId(int id) {
        return buscar("p.id_producto = ?", id);
    }

    public Producto buscarPorNombre(String nombre) {
        return buscar("p.nombre = ?", nombre);
    }

    private Producto buscar(String condicion, Object valor) {

        String sql = """
                SELECT p.id_producto, p.nombre, p.precio,
                       p.stock_actual, p.stock_minimo,
                       c.id_categoria, c.nombre AS categoria,
                       pr.id_proveedor, pr.nombre AS proveedor,
                       pr.contacto
                FROM productos p
                LEFT JOIN categorias c ON p.id_categoria = c.id_categoria
                LEFT JOIN proveedores pr ON p.id_proveedor = pr.id_proveedor
                WHERE p.activo = 1 AND
                """ + condicion;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setObject(1, valor);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el producto.", e);
        }

        return null;
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {

        Categoria categoria = new Categoria(
                rs.getInt("id_categoria"),
                rs.getString("categoria"));

        Proveedor proveedor = null;

        int idProveedor = rs.getInt("id_proveedor");

        if (!rs.wasNull()) {
            proveedor = new Proveedor(
                    idProveedor,
                    rs.getString("proveedor"),
                    rs.getString("contacto"));
        }

        return new Producto(
                rs.getInt("id_producto"),
                rs.getString("nombre"),
                rs.getDouble("precio"),
                categoria,
                proveedor,
                rs.getInt("stock_actual"),
                rs.getInt("stock_minimo"));
    }

    // Actualiza el stock del producto directamente en MySQL.
    public void actualizarStock(int idProducto, int nuevoStock) {

        String sql = "UPDATE productos SET stock_actual = ? WHERE id_producto = ?";

        try (Connection conexion = ConexionBD.conectar()) {

            if (conexion == null) {
                throw new SQLException("No se pudo conectar con MySQL.");
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {

                sentencia.setInt(1, nuevoStock);
                sentencia.setInt(2, idProducto);

                int filasActualizadas = sentencia.executeUpdate();

                if (filasActualizadas == 0) {
                    throw new SQLException("No se encontró el producto.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al actualizar el stock: " + e.getMessage(), e);
        }
    }
}
