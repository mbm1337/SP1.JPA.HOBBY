package org.hobby.dao;

public interface IDAO <T> {

    public void create(T t);

    public Object read(int id);
    public void update(T t);
    public void delete(T t);
}
