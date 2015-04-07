
import java.util.UUID;

public class PiIdGenerator {

	// Generate a new Id for a station using Java class UUID
	static String generatePiID() {
		return UUID.randomUUID().toString();
	}
}
