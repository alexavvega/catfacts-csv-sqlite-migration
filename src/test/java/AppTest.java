import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

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



}