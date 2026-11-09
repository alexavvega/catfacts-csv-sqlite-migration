import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

class MigrateCsvToDbTest {

    @Test
    void readEveryRow() throws IOException {

        MigrateCsvToDb.facts.clear();
        MigrateCsvToDb.loadFromCsv();

        assertEquals(10, MigrateCsvToDb.facts.size());
    }

    @Test
    void commaStoredWhole() throws IOException {

        MigrateCsvToDb.loadFromCsv();

        for (CatFact fact : MigrateCsvToDb.facts) {
            if (fact.id() == 9) {
                assertEquals("Cats, like people, tend to favor one paw over the other", fact.fact());
                return;
            }
        }

        fail("Fact 9 was not found");
    }

    @Test
    void idsComesFromFile() throws IOException {

        MigrateCsvToDb.loadFromCsv();

        assertEquals(1, MigrateCsvToDb.facts.get(0).id());
    }

    @Test
    void highestID() throws IOException {

        MigrateCsvToDb.loadFromCsv();

        assertEquals(11, MigrateCsvToDb.nextId);
    }

    @Test
    void notmultipleFacts() throws IOException {

        MigrateCsvToDb.facts.clear();
        MigrateCsvToDb.loadFromCsv();
        MigrateCsvToDb.loadFromCsv();

        assertEquals(10, MigrateCsvToDb.facts.size());
    }
}
