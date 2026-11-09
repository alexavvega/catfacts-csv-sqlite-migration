import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class CatFactTest {

    @Test
    void returnsCatFactValues() {

        CatFact cat = new CatFact(5, "Cats have super hearing");

        assertEquals(5, cat.id());
        assertEquals("Cats have super hearing", cat.fact());
    }

    @Test
    void catFactsSameValues() {

        CatFact cat1 = new CatFact(5, "Cats have super hearing");
        CatFact cat2 = new CatFact(5, "Cats have super hearing");

        assertEquals(cat1, cat2);
    }

    @Test
    void catFactsNotEqual() {

        CatFact cat1 = new CatFact(5, "Cats have super hearing");
        CatFact cat2 = new CatFact(5, "Cats have night vision");

        assertNotEquals(cat1, cat2);
    }

    @Test
    void catFactByValues() {

        List<CatFact> cats = new ArrayList<>();
        cats.add(new CatFact(1, "Cats have super hearing"));

        assertTrue(cats.contains(new CatFact(1, "Cats have super hearing")));
    }

    @Test
    void catFactHoldingEmptyText() {

        CatFact cat = new CatFact(5, "");

        assertEquals("", cat.fact());
    }
}
