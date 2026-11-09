import java.sql.SQLException;
import java.util.Optional;

import com.google.gson.JsonParseException;

class FactsRouter {

    FactDatabase database;

    FactsRouter(FactDatabase database) {
        this.database = database;
    }

    Response route(String method, String path, String body) throws SQLException {

        if (path.equals("/facts") || path.equals("/facts/")) {

            if (method.equals("GET")) {
                return new Response(200, database.getAllFacts(), null);

            } else if (method.equals("POST")) {

                NewFact input;

                try {
                    input = Json.gson.fromJson(body, NewFact.class);
                } catch (JsonParseException e) {
                    return new Response(400, new ErrorBody("body must be JSON like {\"fact\": \"...\"}"), null);
                }

                if (input == null || input.fact() == null || input.fact().isBlank()) {
                    return new Response(400, new ErrorBody("\"fact\" is required and cannot be blank"), null);
                } else {
                    String text = input.fact().trim();
                    CatFact created = database.createFact(text);

                    return new Response(201, created, "/facts/" + created.id());
                }

            } else {
                return new Response(405, new ErrorBody("method not allowed"), null);
            }

        } else if (!path.startsWith("/facts/")) {
            return new Response(404, new ErrorBody("not found"), null);

        } else {

            String idText = path.substring("/facts/".length());
            int id;

            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException e) {
                return new Response(400, new ErrorBody("id must be a whole number"), null);
            }

            if (method.equals("GET")) {

                Optional<CatFact> found = database.findFactById(id);

                if (found.isPresent()) {
                    return new Response(200, found.get(), null);
                } else {
                    return new Response(404, new ErrorBody("no fact with id " + id), null);
                }

            } else {
                return new Response(405, new ErrorBody("method not allowed"), null);
            }
        }
    }
}