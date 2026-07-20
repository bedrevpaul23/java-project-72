package hexlet.code.model;

import java.time.LocalDateTime;

public final class Url {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public Url(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long newId) {
        id = newId;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime newCreatedAt) {
        createdAt = newCreatedAt;
    }
}
