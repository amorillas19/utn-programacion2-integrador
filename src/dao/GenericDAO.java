package dao;

import java.util.List;

public interface GenericDAO<T> {
    void    create(T entity);
    void    update(T entity);
    void    delete(Long id);
    T       findById(Long id);
    List<T> findAll();
}
