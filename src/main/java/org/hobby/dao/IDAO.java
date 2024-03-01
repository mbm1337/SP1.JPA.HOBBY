package org.hobby.dao;

import org.hobby.model.Person;

public interface IDAO <T,V> {

    public void create(T t);
    public T read(V id);

    public void update(T t);
    public void delete(T t);
}
