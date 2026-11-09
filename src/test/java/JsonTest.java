
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class JsonTest {

    @Test
    void produceCatFactJson() {

        CatFact cat = new CatFact(5, "Cats have super hearing");
        String json = Json.gson.toJson(cat);

        assertTrue(json.contains("\"id\": 5"));
        assertTrue(json.contains("Cats have super hearing"));
    }

    @Test
    void jsonDeserializes() {

        String json = "{\"fact\": \"Cats have super hearing\"}";
        NewFact cat = Json.gson.fromJson(json, NewFact.class);

        assertEquals("Cats have super hearing", cat.fact());
    }

    @Test
    void gsonApostrophe() {

        CatFact cat = new CatFact(5, "Cats don't like water");
        String json = Json.gson.toJson(cat);

        assertTrue(json.contains("don't"));
        assertFalse(json.contains("\\u0027"));
    }

    @Test
    void factCommasSurvive() {

        CatFact cat = new CatFact(9, "Cats, unlike dogs, cannot taste sweetness");
        String json = Json.gson.toJson(cat);
        CatFact result = Json.gson.fromJson(json, CatFact.class);

        assertEquals(cat, result);
    }
}
