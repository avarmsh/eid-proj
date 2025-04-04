package db;

import example.Human;

public abstract class Entity {
    public int id;
    public abstract int getEntityCode();

    public abstract Human copy();
}
