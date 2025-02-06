import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AccessControlSystem {
    private static Map<String, AccessCard> accessCards = new HashMap<>();

    public static void addAccessCard(String cardId, String facadeId) {
        AccessCard card = new AccessCard(cardId, facadeId);
        accessCards.put(cardId, card);
    }

    public static AccessCard getAccessCard(String cardId) {
        return accessCards.get(cardId);
    }

    public static void main(String[] args) {
        addAccessCard("12345", "Facade_A");
        AccessCard card = getAccessCard("12345");
        System.out.println("Card ID: " + card.getCardId());
        System.out.println("Facade ID: " + card.getFacadeId());
        System.out.println("Encrypted Time: " + card.getEncryptedTime());
    }
}