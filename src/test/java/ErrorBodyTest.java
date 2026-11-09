import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ErrorBodyTest {

    @Test
    void errorBodyMessage() {

        ErrorBody error = new ErrorBody("Fact not found");

        assertEquals("Fact not found", error.error());
    }
}
