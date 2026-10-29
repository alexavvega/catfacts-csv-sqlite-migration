
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;


// Calling loadFromCsv() twice adds duplicate facts and we have 20 facts
// because the list was not cleared. I would change the code to clear the list before loading the CSV again.
class AppTest {

    @Test
    void returnsCatFactValues() {

        // Initialize what you need
        App.CatFact cat = new App.CatFact(5, "Cats have super hearing");

        // Assert for what needs to be tested
        assertEquals(5, cat.id());
        assertEquals("Cats have super hearing", cat.fact());
    }

    @Test
    void catFactsSameValues() {

        App.CatFact cat1 = new App.CatFact(5, "Cats have super hearing");
        App.CatFact cat2 = new App.CatFact(5, "Cats have super hearing");

        assertEquals(cat1, cat2);
    }

    @Test
    void catFactsNotEqual() {

        App.CatFact cat1 = new App.CatFact(5, "Cats have super hearing");
        App.CatFact cat2 = new App.CatFact(5, "Cats have night vision");

        assertNotEquals(cat1, cat2);
    }

    @Test
    void catFactByValues() {

        List<App.CatFact> cats = new ArrayList<>();
        cats.add(new App.CatFact(1, "Cats have super hearing"));

        assertTrue(cats.contains(new App.CatFact(1, "Cats have super hearing")));
    }

    @Test
    void returnNewFacts() {

        App.NewFact cat = new App.NewFact("Cats are heavy sleepers");

        assertEquals("Cats are heavy sleepers", cat.fact());
    }

    @Test
    void errorBodyMessage() {

        App.ErrorBody error = new App.ErrorBody("Fact not found");

        assertEquals("Fact not found", error.error());
    }

    // What this tell us is that it allows empty text because it is going to be handled by the API
    @Test
    void catFactHoldingEmptyText() {

        App.CatFact cat = new App.CatFact(5, "");

        assertEquals("", cat.fact());
    }

    //9
    @Test
    void produceCatFactJson() {

        App.CatFact cat = new App.CatFact(5, "Cats have super hearing");
        String json = App.gson.toJson(cat);

        assertTrue(json.contains("\"id\": 5"));
        assertTrue(json.contains("Cats have super hearing"));
    }

    @Test
    void jsonDeserializes() {

        String json = "{\"fact\": \"Cats have super hearing\"}";
        App.NewFact cat = App.gson.fromJson(json, App.NewFact.class);

        assertEquals("Cats have super hearing", cat.fact());
    }

    @Test
    void gsonApostrophe() {

        App.CatFact cat = new App.CatFact(5, "Cats don't like water");
        String json = App.gson.toJson(cat);

        assertTrue(json.contains("don't"));
        assertFalse(json.contains("\\u0027"));
    }

    @Test
    void factCommasSurvive() {

        App.CatFact cat = new App.CatFact(9, "Cats, unlike dogs, cannot taste sweetness");
        String json = App.gson.toJson(cat);
        App.CatFact result = App.gson.fromJson(json, App.CatFact.class);

        assertEquals(cat, result);
    }

    @Test
    void readEveryRow() throws IOException {

        App.facts.clear();
        App.loadFromCsv();

        assertEquals(10, App.facts.size());
    }

    @Test
    void commaStoredWhole() throws IOException {

        App.loadFromCsv();

        for (App.CatFact fact : App.facts) {
            if (fact.id() == 9) {
                assertEquals("Cats, like people, tend to favor one paw over the other", fact.fact());
                return;
            }
        }

        fail("Fact 9 was not found");
    }

    @Test
    void idsComesFromFile() throws IOException {

        App.loadFromCsv();

        assertEquals(1, App.facts.get(0).id());
    }

    @Test
    void highestID() throws IOException {

        App.loadFromCsv();

        assertEquals(11, App.nextId);
    }

    // What I found was that loading the CSV twice duplicates the factst and
    // what I would change is clearing the list before reading the CSV again.
    @Test
    void notmultipleFacts() throws IOException {

        App.facts.clear();
        App.loadFromCsv();
        App.loadFromCsv();

        assertEquals(10, App.facts.size());
    }

}
