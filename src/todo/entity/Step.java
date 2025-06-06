package todo.entity;

import db.*;
import java.util.*;

public class Step extends Entity implements Trackable {
    public enum Status {
        NotStarted, Completed
    }
    public static final int STEP_ENTITY_CODE = 14;
    private String title;
    private Status status;
    private int taskRef;
    private Date creationDate;
    private Date lastModificationDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getTaskRef() {
        return taskRef;
    }

    public void setTaskRef(int taskRef) {
        this.taskRef = taskRef;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    @Override
    public void setLastModificationDate(Date lastModifiedAt) {
        this.lastModificationDate = lastModifiedAt;
    }

    @Override
    public int getEntityCode() {
        return STEP_ENTITY_CODE;
    }

    @Override
    public Entity copy() {
        String titleCopy = new String(this.getTitle());
        Step stepCopy = new Step();
        stepCopy.setStatus(this.getStatus());
        stepCopy.setTitle(titleCopy);
        stepCopy.setTaskRef(this.getTaskRef());
        stepCopy.id = this.id;
        stepCopy.setCreationDate(new Date(creationDate.getTime()));
        if(lastModificationDate != null){
            stepCopy.setLastModificationDate(new Date(lastModificationDate.getTime()));

        }
        return stepCopy;
    }
}
