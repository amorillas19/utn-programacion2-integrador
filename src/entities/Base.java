package entities;

import java.time.LocalDateTime;

public abstract class Base {
    private long id;
    private boolean eliminado;
    private LocalDateTime createdAt;

    public Base() {
        this.eliminado = false;
        this.createdAt = LocalDateTime.now();
    }

    public Base(Long id, boolean eliminado, LocalDateTime createdAt) {
        this.id = id;
        this.eliminado = eliminado;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Base [id=" + id + ", eliminado=" + eliminado + ", createdAt=" + createdAt + "]";
    }
    
}


