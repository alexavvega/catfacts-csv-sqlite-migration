import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Cat Facts API, backed by a CSV file.
 *
 *   GET  /facts        every fact
 *   GET  /facts/{id}   one fact, or 404
 *   POST /facts        add a fact; body is {"fact": "..."}
 */
public class App {

    static final int PORT = 8080;
    static final String DB_URL = "jdbc:sqlite:catfacts.db";

    public static void main(String[] args) throws IOException {
        System.out.println("Using SQLite database: " + DB_URL);

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/facts", new FactsHandler());
        server.start();

        System.out.println("Cat Facts API running on http://localhost:" + PORT + "/facts");
    }

    static class FactsHandler implements HttpHandler {

        FactDatabase database = new FactDatabase(DB_URL);
        FactsRouter router = new FactsRouter(database);

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

                Response response = router.route(method, path, body);

                if (response.location() != null) {
                    exchange.getResponseHeaders().set("Location", response.location());
                }

                sendJson(exchange, response.status(), response.body());

            } catch (Exception e) {
                // Print the real problem in the server terminal, send a clean 500 to the client.
                e.printStackTrace();
                sendJson(exchange, 500, new ErrorBody("internal server error"));
            }
        }

        // ---- Helper ------------------------------------------------------

        /** Converts any object to JSON with Gson and sends it with the given status. */
        void sendJson(HttpExchange exchange, int status, Object body) throws IOException {
            byte[] bytes = Json.gson.toJson(body).getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(status, bytes.length);

            try (OutputStream out = exchange.getResponseBody()) {
                out.write(bytes);
            }
        }
    }
}
