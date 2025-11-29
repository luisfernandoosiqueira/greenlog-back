package app.dto.error;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, String> fieldErrors
) {
    public ErrorResponseDTO(int status, String message, LocalDateTime timestamp) {
        this(timestamp, status, null, message, null);
    }
}
