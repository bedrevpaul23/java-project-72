package hexlet.code.model;

import java.time.LocalDateTime;

public final class UrlCheck {
    private static final int MAX_DISPLAY_LENGTH = 200;
    private static final int TRUNCATED_TEXT_LENGTH = 197;

    private Long id;
    private Long urlId;
    private int statusCode;
    private String h1;
    private String title;
    private String description;
    private LocalDateTime createdAt;

    public UrlCheck(Long newUrlId, int newStatusCode, String newH1, String newTitle, String newDescription) {
        urlId = newUrlId;
        statusCode = newStatusCode;
        h1 = newH1;
        title = newTitle;
        description = newDescription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long newId) {
        id = newId;
    }

    public Long getUrlId() {
        return urlId;
    }

    public void setUrlId(Long newUrlId) {
        urlId = newUrlId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int newStatusCode) {
        statusCode = newStatusCode;
    }

    public String getH1() {
        return h1;
    }

    public void setH1(String newH1) {
        h1 = newH1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String newTitle) {
        title = newTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String newDescription) {
        description = newDescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime newCreatedAt) {
        createdAt = newCreatedAt;
    }

    public String getCreatedAtAsDate() {
        return createdAt.toLocalDate().toString();
    }

    public String getH1ForDisplay() {
        return truncate(h1);
    }

    public String getTitleForDisplay() {
        return truncate(title);
    }

    public String getDescriptionForDisplay() {
        return truncate(description);
    }

    private static String truncate(String value) {
        if (value == null) {
            return "";
        }

        return value.length() > MAX_DISPLAY_LENGTH
                ? value.substring(0, TRUNCATED_TEXT_LENGTH) + "..."
                : value;
    }
}
