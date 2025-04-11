package db;
import db.exception.*;

import java.io.*;
import java.util.*;

public class Database {
    private static final ArrayList<Entity> entities = new ArrayList<>();
    private static int idCounter = 1;
    private static final HashMap<Integer, Validator> validators = new HashMap<>();
    private static final HashMap<Integer, Serializer> serializers = new HashMap<>();

    private Database() {}

    public static void registerSerializer(int entityCode, Serializer serializer) {
        serializers.put(entityCode, serializer);
    }

    public static void registerValidator(int entityCode, Validator validator) {
        if (validators.containsKey(entityCode)) {
            throw new IllegalArgumentException("Validator already registered for this entity code");
        } else {
            validators.put(entityCode, validator);
        }
    }

    public static void add(Entity e) throws InvalidEntityException {
        Validator validator = validators.get(e.getEntityCode());
        if (validator != null) {
            validator.validate(e);
        }

        e.id = idCounter++;

        if (e instanceof Trackable) {
            Date now = new Date();
            Trackable trackable = (Trackable) e;
            trackable.setCreationDate(now);
            trackable.setLastModificationDate(now);
        }

        entities.add(e.copy());
    }

    public static Entity get(int id) throws EntityNotFoundException {
        for (Entity e : entities) {
            if (e.id == id) {
                return e.copy();
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) {
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == id) {
                entities.remove(i);
                return;
            }
        }
    }

    public static void update(Entity e) throws InvalidEntityException {
        Validator validator = validators.get(e.getEntityCode());
        if (validator != null) {
            validator.validate(e);
        }

        if (e instanceof Trackable) {
            ((Trackable) e).setLastModificationDate(new Date());
        }

        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == e.id) {
                entities.set(i, e.copy());
                return;
            }
        }
        throw new EntityNotFoundException(e.id);
    }

    public static ArrayList<Entity> getAll(int entityCode) {
        ArrayList<Entity> result = new ArrayList<>();
        for (Entity e : entities) {
            if (e.getEntityCode() == entityCode) {
                result.add(e.copy());
            }
        }
        return result;
    }

    public static void save() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("db.txt"))) {
            for (Entity entity : entities) {
                Serializer serializer = serializers.get(entity.getEntityCode());
                if (serializer != null) {
                    String serializedEntity = serializer.serialize(entity);
                    writer.write(entity.getEntityCode() + "|" + serializedEntity);
                    writer.newLine();
                }
            }
            System.out.println("Database saved successfully.");
        } catch (IOException e) {
            System.err.println("Error while saving database: " + e.getMessage());
            throw e;
        }
    }

    public static void load() throws IOException, ClassNotFoundException {
        File file = new File("db.txt");
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length != 2) continue;

                int entityCode = Integer.parseInt(parts[0]);
                String data = parts[1];

                Serializer serializer = serializers.get(entityCode);
                if (serializer != null) {
                    Entity entity = serializer.deserialize(data);
                    if (entity != null) {
                        entities.add(entity.copy());
                    }
                }
            }
            updateIdCounter();
            System.out.println("Database loaded successfully.");
        } catch (IOException | NumberFormatException | ClassNotFoundException e) {
            System.err.println("Error while loading database: " + e.getMessage());
            throw e;
        }
    }

    private static void updateIdCounter() {
        for (Entity e : entities) {
            if (e.id >= idCounter) {
                idCounter = e.id + 1;
            }
        }
    }
}