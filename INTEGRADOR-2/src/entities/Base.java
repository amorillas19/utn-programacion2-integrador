package entities;

import java.time.LocalDateTime;

public abstract class Base {
    private long id;
    private boolean eliminado;
    private LocalDateTime createdAt;

    public Base() {
    }

    public Base(long id, boolean eliminado, LocalDateTime createdAt) {
        this.id = id;
        this.eliminado = eliminado;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Base [id=" + id + ", eliminado=" + eliminado + ", createdAt=" + createdAt + "]";
    }

    
}


