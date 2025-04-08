package db;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;

public class Database {
    private static final ArrayList<Entity> entities = new ArrayList<>();
    private static int idCounter = 1;
    private static HashMap<Integer, Validator> validators = new HashMap<>();

    private Database() {}

    public static void registerValidator(int entityCode, Validator validator) {
        if(validators.containsKey(entityCode)){
            throw new IllegalArgumentException("Validator exists for this entity code");
        }
        validators.put(entityCode, validator);
    }

    public static void add(Entity e) throws InvalidEntityException {
        Validator validator = validators.get(e.getEntityCode());
        if(validator != null){
            validator.validate(e);
        }
        e.id = idCounter++;
        entities.add(e);
        if (e instanceof Trackable trackableEntity) {
            Date now = new Date();
            trackableEntity.setCreationDate(now);
            trackableEntity.setLastModificationDate(now);
        }

    }

    public static Entity get(int id) {
        for(Entity e : entities){
            if(e.id == id){
                return e;
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void update(Entity e) throws InvalidEntityException {
        Validator validator = validators.get(e.getEntityCode());
        if(validator != null){
            validator.validate(e);
        }
        if (e instanceof Trackable trackableEntity) {
            trackableEntity.setLastModificationDate(new Date());
        }

    }
}
