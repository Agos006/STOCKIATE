package view;

import controller.ProductoController;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import model.Movimiento;
import model.Producto;
import repository.MovimientoRepository;
import repository.ProductoRepository;
import service.ProductoService;
import service.StockService;

public class StockiaWeb {

    private static final ProductoController controlador = crearControlador();

    public static void main(String[] args) {

        Javalin app = Javalin.create();

        app.get("/", ctx -> ctx.html(pagina(
                ctx.queryParam("tipo"),
                ctx.queryParam("msg"),
                ctx.queryParam("historial"))));

        app.post("/productos", ctx -> {
            try {
                Producto p = controlador.registrarProducto(
                        ctx.formParam("nombre"), ctx.formParam("precio"),
                        ctx.formParam("categoria"), ctx.formParam("proveedor"),
                        ctx.formParam("cantidad"), ctx.formParam("minimo"));
                redirigir(ctx, "ok", "Producto registrado: " + p.getNombre());
            } catch (IllegalArgumentException e) {
                redirigir(ctx, "error", e.getMessage());
            }
        });

        app.post("/movimientos", ctx -> {
            try {
                Movimiento m = controlador.registrarMovimiento(
                        ctx.formParam("producto"), ctx.formParam("tipo"),
                        ctx.formParam("cantidad"));
                Producto p = m.getProducto();
                String aviso = "Movimiento registrado. Stock actual de "
                        + p.getNombre() + ": " + p.getCantidad() + ".";

                if (p.getStock().estaBajoMinimo()) {
                    redirigir(ctx, "alerta", "ALERTA: stock bajo de " + p.getNombre()
                            + " (quedan " + p.getCantidad() + ", mínimo "
                            + p.getStock().getStockMinimo() + "). " + aviso);
                } else {
                    redirigir(ctx, "ok", aviso);
                }
            } catch (IllegalArgumentException e) {
                redirigir(ctx, "error", e.getMessage());
            }
        });

        app.start(7070);

        System.out.println("STOCKIA está funcionando en:");
        System.out.println("http://localhost:7070");
    }

    // ---- armado de las piezas y datos de ejemplo ----

    private static ProductoController crearControlador() {
        ProductoService productoService = new ProductoService(new ProductoRepository());
        StockService stockService =
                new StockService(productoService, new MovimientoRepository());

        // Registrar productos de ejemplo solo si la base de datos está vacía
        if (productoService.listarProductos().isEmpty()) {
            productoService.registrarProducto("Coca Cola", 2500, "Bebidas", "Distribuidora Sur", 24, 6);
            productoService.registrarProducto("Alfajor Rasta", 1000, "Golosinas", "Distribuidora Dulce", 30, 10);
            productoService.registrarProducto("Galletas de agua Providencia", 1800, "Galletitas", "Distribuidora Sur", 15, 5);
        }

        return new ProductoController(productoService, stockService);
    }

    private static void redirigir(Context ctx, String tipo, String mensaje) {
        ctx.redirect("/?tipo=" + tipo + "&msg="
                + URLEncoder.encode(mensaje, StandardCharsets.UTF_8));
    }

    // ---- armado de la página ----

    private static String pagina(String tipo, String mensaje, String historial) {
        List<Producto> productos = controlador.listarProductos();
        StringBuilder h = new StringBuilder();

        h.append("<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\">")
         .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
         .append("<title>STOCKIA | Gestión de stock</title><style>").append(ESTILOS)
         .append("</style></head><body>")
         .append("<header><h1>STOCKIA</h1><p>Gestión inteligente de inventario</p></header><main>");

        if (mensaje != null && !mensaje.isBlank()) {
            String clase = "alerta".equals(tipo) ? "alerta" : "error".equals(tipo) ? "error" : "ok";
            h.append("<div class=\"aviso ").append(clase).append("\">")
             .append(esc(mensaje)).append("</div>");
        }

        // inventario
        h.append("<section class=\"panel\"><h2>Inventario</h2>");
        if (productos.isEmpty()) {
            h.append("<p>No hay productos registrados.</p>");
        } else {
            h.append("<div class=\"tabla\"><table><tr><th>Producto</th><th>Categoría</th>")
             .append("<th>Proveedor</th><th>Precio</th><th>Cantidad</th><th>Mínimo</th><th>Estado</th></tr>");
            for (Producto p : productos) {
                String estado;
                String clase;
                if (p.getCantidad() == 0) {
                    estado = "Sin stock"; clase = "sin";
                } else if (p.getStock().estaBajoMinimo()) {
                    estado = "Stock bajo"; clase = "bajo";
                } else {
                    estado = "Disponible"; clase = "ok";
                }
                h.append("<tr><td>").append(esc(p.getNombre())).append("</td><td>")
                 .append(esc(String.valueOf(p.getCategoria()))).append("</td><td>")
                        .append(esc(p.getProveedor() == null ? "Sin proveedor" : p.getProveedor().toString())).append("</td><td>")
                 .append(String.format("$%.2f", p.getPrecio())).append("</td><td>")
                 .append(p.getCantidad()).append("</td><td>")
                 .append(p.getStock().getStockMinimo()).append("</td><td><span class=\"estado ")
                 .append(clase).append("\">").append(estado).append("</span></td></tr>");
            }
            h.append("</table></div>");
        }
        h.append("</section>");

        // movimiento de stock
        h.append("<section class=\"panel\"><h2>Registrar entrada o salida</h2>")
         .append("<form method=\"post\" action=\"/movimientos\"><div class=\"fila\">")
         .append("<label>Producto<select name=\"producto\">").append(opciones(productos, null))
         .append("</select></label>")
         .append("<label>Tipo<select name=\"tipo\"><option value=\"ENTRADA\">Entrada</option>")
         .append("<option value=\"SALIDA\">Salida</option></select></label>")
         .append("<label>Cantidad<input type=\"number\" name=\"cantidad\" min=\"1\" required></label>")
         .append("</div><button type=\"submit\">Registrar movimiento</button></form></section>");

        // alta de producto
        h.append("<section class=\"panel\"><h2>Registrar producto nuevo</h2>")
         .append("<form method=\"post\" action=\"/productos\"><div class=\"fila\">")
         .append("<label>Nombre<input type=\"text\" name=\"nombre\" required></label>")
         .append("<label>Precio<input type=\"text\" name=\"precio\" required></label>")
         .append("<label>Categoría<input type=\"text\" name=\"categoria\" required></label>")
         .append("<label>Proveedor<input type=\"text\" name=\"proveedor\" required></label>")
         .append("<label>Cantidad inicial<input type=\"number\" name=\"cantidad\" min=\"0\" required></label>")
         .append("<label>Stock mínimo<input type=\"number\" name=\"minimo\" min=\"0\" required></label>")
         .append("</div><button type=\"submit\">Registrar producto</button></form></section>");

        // historial
        h.append("<section class=\"panel\"><h2>Historial de movimientos</h2>")
         .append("<form method=\"get\" action=\"/\"><div class=\"fila\"><label>Producto<select name=\"historial\">")
         .append(opciones(productos, historial))
         .append("</select></label></div><button type=\"submit\">Ver historial</button></form>");

        if (historial != null && !historial.isBlank()) {
            try {
                List<Movimiento> movimientos = controlador.obtenerHistorial(historial);
                if (movimientos.isEmpty()) {
                    h.append("<p>Ese producto todavía no tiene movimientos.</p>");
                } else {
                    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    h.append("<div class=\"tabla\"><table><tr><th>Fecha</th><th>Tipo</th><th>Cantidad</th></tr>");
                    for (Movimiento m : movimientos) {
                        h.append("<tr><td>").append(m.getFecha().format(formato)).append("</td><td>")
                         .append(m.getTipo()).append("</td><td>")
                         .append(m.getCantidad()).append("</td></tr>");
                    }
                    h.append("</table></div>");
                }
            } catch (IllegalArgumentException e) {
                h.append("<p>").append(esc(e.getMessage())).append("</p>");
            }
        }
        h.append("</section>");

        h.append("</main></body></html>");
        return h.toString();
    }

    private static String opciones(List<Producto> productos, String seleccionado) {
        StringBuilder o = new StringBuilder();
        for (Producto p : productos) {
            o.append("<option value=\"").append(esc(p.getNombre())).append("\"");
            if (p.getNombre().equals(seleccionado)) {
                o.append(" selected");
            }
            o.append(">").append(esc(p.getNombre())).append("</option>");
        }
        return o.toString();
    }

    // evita que el texto escrito por el usuario rompa el HTML
    private static String esc(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }

    private static final String ESTILOS = """
        body { margin: 0; font-family: Arial, sans-serif; background-color: rgb(236, 222, 192); color: #2d1552; }
        header { background-color: #6F4A8E; color: white; padding: 22px 8%; }
        header h1 { margin: 0; font-size: 28px; letter-spacing: 2px; }
        header p { margin: 4px 0 0; }
        main { max-width: 1000px; margin: 32px auto; padding: 0 24px; }
        .panel { background-color: white; padding: 24px 28px; border-radius: 16px;
                 box-shadow: 0 6px 24px #352A4510; margin-bottom: 24px; }
        h2 { color: #6F4A8E; margin-top: 0; }
        .tabla { overflow-x: auto; }
        table { width: 100%; border-collapse: collapse; }
        th, td { text-align: left; padding: 10px 12px; border-bottom: 1px solid #E8DDF2; }
        th { color: #6F4A8E; }
        .estado { padding: 4px 10px; border-radius: 20px; font-size: 13px; }
        .estado.ok { background-color: #DDF2E3; color: #1D6B34; }
        .estado.bajo { background-color: #FFF0C7; color: #8A5A00; }
        .estado.sin { background-color: #F7D9D9; color: #9B1C1C; }
        .fila { display: flex; flex-wrap: wrap; gap: 16px; margin-bottom: 16px; }
        label { display: flex; flex-direction: column; font-size: 14px; gap: 4px; min-width: 160px; }
        input, select { padding: 8px 10px; border: 1px solid #CDBBE0; border-radius: 8px; font-size: 15px; }
        button { background-color: #6F4A8E; color: white; border: 0; padding: 10px 18px;
                 border-radius: 8px; font-size: 15px; cursor: pointer; }
        button:hover { background-color: #5A3A75; }
        .aviso { padding: 14px 18px; border-radius: 12px; margin-bottom: 24px; font-weight: bold; }
        .aviso.ok { background-color: #DDF2E3; color: #1D6B34; }
        .aviso.alerta { background-color: #FFF0C7; color: #8A5A00; }
        .aviso.error { background-color: #F7D9D9; color: #9B1C1C; }
        """;
}
