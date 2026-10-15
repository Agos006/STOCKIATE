
import io.javalin.Javalin;

public class StockiaWeb {

    public static void main(String[] args) {

        Javalin app = Javalin.create();

        app.get("/", ctx -> ctx.html("""
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport"
                      content="width=device-width, initial-scale=1.0">
                <title>STOCKIA | Gestión de stock</title>

                <style>
                    body {
                        margin: 0;
                        font-family: Arial, sans-serif;
                        background-color: rgb(236, 222, 192);
                        color: #2d1552;
                    }

                    header {
                        background-color: #6F4A8E;
                        color: white;
                        padding: 22px 8%;
                    }

                    header h1 {
                        margin: 0;
                        font-size: 28px;
                        letter-spacing: 2px;
                    }

                    main {
                        max-width: 900px;
                        margin: 60px auto;
                        padding: 0 24px;
                    }

                    .panel {
                        background-color: white;
                        padding: 32px;
                        border-radius: 16px;
                        box-shadow: 0 6px 24px #352A4510;
                    }

                    h2 {
                        color: #6F4A8E;
                    }

                    .estado {
                        display: inline-block;
                        background-color: #E8DDF2;
                        color: #6F4A8E;
                        padding: 8px 12px;
                        border-radius: 20px;
                        font-size: 14px;
                    }
                </style>
            </head>

            <body>
                <header>
                    <h1>STOCKIA</h1>
                    <p>Gestión inteligente de inventario</p>
                </header>

                <main>
                    <section class="panel">
                        <span class="estado">Aplicación web iniciada</span>

                        <h2>¡Bienvenido a STOCKIA!</h2>

                        <p>
                            Gestioná tus productos de manera sencilla
                            y mantené tu inventario organizado.
                        </p>

                        <p>
                            Este es el comienzo de tu sistema de stock.
                        </p>
                    </section>
                </main>
            </body>
            </html>
            """));

        app.start(7070);

        System.out.println("STOCKIA está funcionando en:");
        System.out.println("http://localhost:7070");
    }
}

