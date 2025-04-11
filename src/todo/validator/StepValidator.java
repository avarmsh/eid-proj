package todo.validator;

import db.*;
import db.exception.*;
import todo.entity.*;

public class StepValidator implements Validator {

    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if(entity instanceof Step){
            try{
                Database.get(((Step) entity).getTaskRef());
            }catch(EntityNotFoundException e){
                throw new InvalidEntityException("TaskRef is incorrect.");
            }
        }else{
            throw new IllegalArgumentException("Entity must be a Step.");
        }
    }
}
