import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FactDatabaseTest {

    @TempDir
    Path tempDir;

    @Test
    void getAllFactsInOrder() throws Exception {

        Path db = tempDir.resolve("test.db");
        String url = "jdbc:sqlite:" + db;

        try (Connection c = DriverManager.getConnection(url)) {
            Statement s = c.createStatement();
            s.executeUpdate("CREATE TABLE facts (id INTEGER PRIMARY KEY AUTOINCREMENT, fact TEXT NOT NULL)");

            String[] facts = {"Cats purr", "Cats, oddly, cannot taste sweetness"};

            for (int i = 0; i < facts.length; i++) {
                String sql = "INSERT INTO facts (id, fact) VALUES (" + (i + 1) + ", '" + facts[i] + "')";
                s.executeUpdate(sql);
            }

            s.close();
        }

        FactDatabase database = new FactDatabase(url);
        List<CatFact> results = database.getAllFacts();

        assertEquals(2, results.size());
        assertEquals(1, results.get(0).id());
        assertEquals("Cats, oddly, cannot taste sweetness", results.get(1).fact());
    }

    @Test
    void testingFindFactByIdExists() throws Exception {

        Path db = tempDir.resolve("found.db");
        String url = "jdbc:sqlite:" + db;

        try (Connection c = DriverManager.getConnection(url);
             Statement s = c.createStatement()) {

            s.executeUpdate("CREATE TABLE facts (id INTEGER PRIMARY KEY AUTOINCREMENT, fact TEXT NOT NULL)");
            s.executeUpdate("INSERT INTO facts (id, fact) VALUES (2, 'Cats sleep a lot')");
        }

        FactDatabase database = new FactDatabase(url);
        Optional<CatFact> found = database.findFactById(2);

        assertTrue(found.isPresent());
        assertEquals(2, found.get().id());
        assertEquals("Cats sleep a lot", found.get().fact());
    }

    @Test
    void testingFindFactByIdMissing() throws Exception {

        Path db = tempDir.resolve("missing.db");
        String url = "jdbc:sqlite:" + db;

        try (Connection c = DriverManager.getConnection(url)) {
            try (Statement s = c.createStatement()) {

                String sql = "CREATE TABLE facts (id INTEGER PRIMARY KEY AUTOINCREMENT, fact TEXT NOT NULL)";
                s.executeUpdate(sql);

                String insert = "INSERT INTO facts (id, fact) VALUES (2, 'Cats sleep a lot')";
                s.executeUpdate(insert);
            }
        }

        FactDatabase database = new FactDatabase(url);
        Optional<CatFact> found = database.findFactById(99);

        assertTrue(found.isEmpty());
    }
}