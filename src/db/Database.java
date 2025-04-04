package db;
import java.util.ArrayList;
import db.exception.EntityNotFoundException;
public class Database {
    private static final ArrayList<Entity> entities = new ArrayList<>();
    private static int idCounter = 1;

    private Database() {}

    public static void add(Entity e) {
        e.id = idCounter++;
        entities.add(e.copy());
    }

    public static Entity get(int id) {
        for(Entity e : entities){
            if(e.id == id){
                return e.copy();
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) {
        for(int i = 0; i < entities.size(); i++){
            if(entities.get(i).id == id){
                entities.remove(i);
                return;
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void update(Entity e) {
        for(int i = 0; i < entities.size(); i++){
            if(entities.get(i).id == e.id){
                entities.set(i, e.copy());
                return;
            }
        }
        throw new EntityNotFoundException(e.id);
    }
}
