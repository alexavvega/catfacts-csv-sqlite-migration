
import java.io.IOException;
import java.net.InetSocketAddress;

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

        FactDatabase database = new FactDatabase(DB_URL);

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/facts", new FactsHandler(database));
        server.start();

        System.out.println("Cat Facts API running on http://localhost:" + PORT + "/facts");
    }
}
