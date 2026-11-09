import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class NewFactTest {

    @Test
    void returnNewFacts() {

        NewFact cat = new NewFact("Cats are heavy sleepers");

        assertEquals("Cats are heavy sleepers", cat.fact());
    }
}
