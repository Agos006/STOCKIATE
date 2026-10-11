
import java.util.List;
import java.util.Scanner;

import model.Producto;
import repository.ProductoRepository;

public class Main {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        ProductoRepository repositorio = new ProductoRepository();

        int opcion;

        do {
            System.out.println("\n===== STOCKIA =====");
            System.out.println("1. Registrar producto");
            System.out.println("2. Mostrar productos");
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
                    System.out.print("Nombre del producto: ");
                    String nombre = teclado.nextLine();

double precio;

while (true) {
    System.out.print("Precio: ");

    if (teclado.hasNextDouble()) {
        precio = teclado.nextDouble();
        teclado.nextLine();

        if (precio >= 0 && Double.isFinite(precio)) {
            break;
        } else {
            System.out.println(
                "El precio debe ser un número válido y no negativo."
            );
        }
    } else {
        System.out.println("Error: ingresá un número válido.");
        teclado.nextLine();
    }
}
int cantidad;
while (true) {
    System.out.print("Cantidad en stock: ");

    if (teclado.hasNextInt()) {
        cantidad = teclado.nextInt();
        teclado.nextLine();

        if (cantidad >= 0) {
            break;
        } else {
            System.out.println(
                "La cantidad no puede ser negativa."
            );
        }
    } else {
        System.out.println(
            "Error: ingresá una cantidad entera válida."
        );
        teclado.nextLine();
    }
}
                    try {
                        Producto producto =
                            new Producto(nombre, precio, cantidad);

                        repositorio.registrar(producto);

                        System.out.println(
                            "Producto registrado correctamente."
                        );

                    } catch (IllegalArgumentException e) {
                        System.out.println(
                            "No se pudo registrar: " + e.getMessage()
                        );
                    }
                    break;

                case 2:
                    List<Producto> productos =
                        repositorio.listarTodos();

                    if (productos.isEmpty()) {
                        System.out.println("No hay productos registrados.");
                    } else {
                        System.out.println("\n--- INVENTARIO ---");

                        
for (Producto producto : productos) {
    System.out.println("Nombre: " + producto.getNombre());
    System.out.printf("Precio: $%.2f%n", producto.getPrecio());
    System.out.println("Cantidad: " + producto.getCantidad());

    if (producto.getCantidad() > 0) {
        System.out.println("Estado: Disponible");
    } else {
        System.out.println("Estado: Sin stock");
    }

    System.out.println("------------------");
}

                    }
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
}
