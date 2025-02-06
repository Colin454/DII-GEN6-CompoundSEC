import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

class AccessCard {
    private String cardId;
    private String facadeId;
    private String encryptedTime;

    public AccessCard(String cardId, String facadeId) {
        this.cardId = cardId;
        this.facadeId = facadeId;
        this.encryptedTime = encryptTime();
    }

    private String encryptTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return now.format(formatter);  // Simple encryption using timestamp
    }

    public String getCardId() {
        return cardId;
    }

    public String getFacadeId() {
        return facadeId;
    }

    public String getEncryptedTime() {
        return encryptedTime;
    }
}