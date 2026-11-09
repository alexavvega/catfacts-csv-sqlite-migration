import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class FactDatabase {

    private final String dbUrl;

    FactDatabase(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    List<CatFact> getAllFacts() throws SQLException {
        String sql = "SELECT id, fact FROM facts ORDER BY id";
        List<CatFact> results = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String fact = rs.getString("fact");
                        results.add(new CatFact(id, fact));
                    }
                }
            }
        }

        return results;
    }

    Optional<CatFact> findFactById(int id) throws SQLException {
        String sql = "SELECT id, fact FROM facts WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int factId = rs.getInt("id");
                        String factText = rs.getString("fact");

                        CatFact fact = new CatFact(factId, factText);
                        return Optional.of(fact);
                    }
                }
            }
        }

        return Optional.empty();
    }

    CatFact createFact(String text) throws SQLException {
        String sql = "INSERT INTO facts (fact) VALUES (?) RETURNING id";
        int newId;

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, text);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("No ID returned");
                    }
                    newId = rs.getInt("id");
                }
            }
        }

        return new CatFact(newId, text);
    }
}