import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import model.Movimiento;
import model.Producto;
import repository.MovimientoRepository;
import repository.ProductoRepository;
import service.ProductoService;
import service.StockService;

public class Main {

    private static final Scanner teclado = new Scanner(System.in);

    // Main solo arma las piezas y habla con los servicios
    private static final ProductoService productoService =
            new ProductoService(new ProductoRepository());
    private static final StockService stockService =
            new StockService(productoService, new MovimientoRepository());

    public static void main(String[] args) {

        int opcion;

        do {
            System.out.println("\n===== STOCKIA =====");
            System.out.println("1. Registrar producto");
            System.out.println("2. Mostrar productos");
            System.out.println("3. Registrar entrada de stock");
            System.out.println("4. Registrar salida de stock");
            System.out.println("5. Ver historial de un producto");
            System.out.println("0. Salir");
            System.out.print("Elegí una opción: ");

            if (teclado.hasNextInt()) {
                opcion = teclado.nextInt();
                teclado.nextLine();
            } else {
                System.out.println("Error: ingresá una opción numérica.");
                teclado.nextLine();
                opcion = -1;
            }

            switch (opcion) {
                case 1:
                    registrarProducto();
                    break;
                case 2:
                    mostrarProductos();
                    break;
                case 3:
                    registrarEntrada();
                    break;
                case 4:
                    registrarSalida();
                    break;
                case 5:
                    verHistorial();
                    break;
                case 0:
                    System.out.println("Cerrando STOCKIA...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);

        teclado.close();
    }

    private static void registrarProducto() {
        System.out.print("Nombre del producto: ");
        String nombre = teclado.nextLine();

        double precio = leerPrecio();
        int cantidad = leerEntero("Cantidad en stock: ", 0);
        int stockMinimo = leerEntero("Stock mínimo (para la alerta): ", 0);

        System.out.print("Categoría: ");
        String categoria = teclado.nextLine();

        System.out.print("Proveedor: ");
        String proveedor = teclado.nextLine();

        try {
            productoService.registrarProducto(nombre, precio,
                    categoria, proveedor, cantidad, stockMinimo);
            System.out.println("Producto registrado correctamente.");

        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    private static void mostrarProductos() {
        List<Producto> productos = productoService.listarProductos();

        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        System.out.println("\n--- INVENTARIO ---");

        for (Producto producto : productos) {
            System.out.println("Nombre: " + producto.getNombre());
            System.out.println("Categoría: " + producto.getCategoria());
            System.out.println("Proveedor: " + producto.getProveedor());
            System.out.printf("Precio: $%.2f%n", producto.getPrecio());
            System.out.println("Cantidad: " + producto.getCantidad());
            System.out.println("Stock mínimo: " + producto.getStock().getStockMinimo());

            if (producto.getCantidad() == 0) {
                System.out.println("Estado: Sin stock");
            } else if (producto.getStock().estaBajoMinimo()) {
                System.out.println("Estado: Stock bajo");
            } else {
                System.out.println("Estado: Disponible");
            }

            System.out.println("------------------");
        }
    }

    private static void registrarEntrada() {
        System.out.print("Nombre del producto: ");
        String nombre = teclado.nextLine();
        int cantidad = leerEntero("Cantidad: ", 1);

        try {
            Movimiento m = stockService.registrarEntrada(nombre, cantidad);
            System.out.println("Entrada registrada. Stock actual: "
                    + m.getProducto().getCantidad());
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    private static void registrarSalida() {
        System.out.print("Nombre del producto: ");
        String nombre = teclado.nextLine();
        int cantidad = leerEntero("Cantidad: ", 1);

        try {
            Movimiento m = stockService.registrarSalida(nombre, cantidad);
            System.out.println("Salida registrada. Stock actual: "
                    + m.getProducto().getCantidad());
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    private static void verHistorial() {
        System.out.print("Nombre del producto: ");
        String nombre = teclado.nextLine();

        try {
            List<Movimiento> movimientos = stockService.obtenerHistorial(nombre);

            if (movimientos.isEmpty()) {
                System.out.println("Ese producto todavía no tiene movimientos.");
                return;
            }

            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            System.out.println("\n--- HISTORIAL DE " + nombre.trim() + " ---");
            for (Movimiento m : movimientos) {
                System.out.println(m.getFecha().format(formato)
                        + " | " + m.getTipo()
                        + " | " + m.getCantidad());
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // ---- métodos auxiliares para leer datos con validación ----

    private static double leerPrecio() {
        while (true) {
            System.out.print("Precio: ");

            if (teclado.hasNextDouble()) {
                double precio = teclado.nextDouble();
                teclado.nextLine();

                if (precio >= 0 && Double.isFinite(precio)) {
                    return precio;
                }
                System.out.println("El precio debe ser un número válido y no negativo.");
            } else {
                System.out.println("Error: ingresá un número válido.");
                teclado.nextLine();
            }
        }
    }

    private static int leerEntero(String mensaje, int minimo) {
        while (true) {
            System.out.print(mensaje);

            if (teclado.hasNextInt()) {
                int numero = teclado.nextInt();
                teclado.nextLine();

                if (numero >= minimo) {
                    return numero;
                }
                System.out.println("El valor no puede ser menor a " + minimo + ".");
            } else {
                System.out.println("Error: ingresá un número entero válido.");
                teclado.nextLine();
            }
        }
    }
}