
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

class FactsHandler implements HttpHandler {

    FactDatabase database;
    FactsRouter router;

    FactsHandler(FactDatabase database) {
        this.database = database;
        this.router = new FactsRouter(database);
    }

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
            e.printStackTrace();
            sendJson(exchange, 500, new ErrorBody("internal server error"));
        }
    }
    
    void sendJson(HttpExchange exchange, int status, Object body) throws IOException {
        byte[] bytes = Json.gson.toJson(body).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);

        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }
}
