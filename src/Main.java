import db.*;
import todo.entity.*;
import todo.service.*;
import todo.validator.*;
import todo.Serializer.*;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Database.registerValidator(Task.TASK_ENTITY_ID, new TaskValidator());
        Database.registerValidator(Step.STEP_ENTITY_CODE, new StepValidator());
        Database.registerSerializer(Task.TASK_ENTITY_ID, new todo.Serializer.TaskSerializer());
        Database.registerSerializer(Step.STEP_ENTITY_CODE, new todo.Serializer.StepSerializer());
        Scanner inp = new Scanner(System.in);

        try {
            Database.load();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        while (true) {
            System.out.print("What do you want to do? ");
            String command = inp.nextLine().toLowerCase();

            if (command.equals("add task")) {
                System.out.print("Title: ");
                String title = inp.nextLine();
                System.out.print("Description: ");
                String description = inp.nextLine();
                System.out.print("Date(yyyy-mm-dd format): ");
                String dateStr = inp.nextLine();
                TaskService.addTask(title, description, dateStr);
                System.out.println("----------");
            } else if (command.equals("add step")) {
                System.out.print("TaskID: ");
                int taskRef = Integer.parseInt(inp.nextLine());
                System.out.print("Title: ");
                String title = inp.nextLine();
                StepService.addStep(taskRef, title);
                System.out.println("----------");
            } else if (command.equals("delete")) {
                System.out.print("ID: ");
                int id = Integer.parseInt(inp.nextLine());
                TaskService.delete(id);
                System.out.println("----------");
            } else if (command.equals("update task")) {
                System.out.print("Task ID: ");
                int id = Integer.parseInt(inp.nextLine());
                System.out.print("Field: ");
                String field = inp.nextLine().toLowerCase();

                if (field.equals("title")) {
                    System.out.print("New Value: ");
                    String newValue = inp.nextLine();
                    TaskService.update(id, field, newValue, true, false);
                } else if (field.equals("description") || field.equals("content")) {
                    System.out.print("New Value: ");
                    String newValue = inp.nextLine();
                    TaskService.update(id, field, newValue, false, false);
                } else if (field.equals("date") || field.equals("due date")) {
                    System.out.print("New Value: ");
                    String newValue = inp.nextLine();
                    TaskService.update(id, field, newValue, false, true);
                } else if (field.equals("status")) {
                    System.out.println("\nWhat is the status?\n\n1.Completed\n2.In progress\n3.Not started");
                    System.out.print("Your option: ");
                    int option = Integer.parseInt(inp.nextLine());
                    TaskService.updateStatus(id, option);
                } else {
                    System.out.println("invalid field.");
                }
                System.out.println("----------");
            } else if (command.equals("update step")) {
                System.out.print("Step ID: ");
                int id = Integer.parseInt(inp.nextLine());
                System.out.print("Field: ");
                String field = inp.nextLine().toLowerCase();

                if (field.equals("title")) {
                    System.out.print("New Value: ");
                    String newValue = inp.nextLine();
                    StepService.update(id, field, newValue, false);
                } else if (field.equals("task ref") || field.equals("taskref")) {
                    System.out.print("New TaskID: ");
                    String newValue = inp.nextLine();
                    StepService.update(id, field, newValue, true);
                } else if (field.equals("status")) {
                    System.out.println("\nWhat is the status?\n\n1.Completed\n2.Not started");
                    System.out.print("Your option: ");
                    int option = Integer.parseInt(inp.nextLine());
                    StepService.updateStatus(id, option);
                } else {
                    System.out.println("invalid field.");
                }
                System.out.println("----------");
            } else if (command.equals("delete step")) {
                System.out.print("Step ID: ");
                int id = Integer.parseInt(inp.nextLine());
                StepService.delete(id);
                System.out.println("----------");
            } else if (command.equals("get task-by-id") || command.equals("get task") || command.equals("get task by id")) {
                System.out.print("Task ID: ");
                int id = Integer.parseInt(inp.nextLine());
                TaskService.getTaskByID(id);
                System.out.println("----------");
            } else if (command.equals("get incomplete tasks") || command.equals("get incomplete") || command.equals("get incomplete-tasks")) {
                TaskService.getTaskIncomplete();
                System.out.println("----------");
            } else if (command.equals("get tasks") || command.equals("get all tasks") || command.equals("get all-tasks")) {
                TaskService.getTaskAll();
                System.out.println("----------");
            } else if (command.equals("exit")) {
                System.out.println("exiting program...");
                System.exit(0);
            }
            else if (command.equals("save")) {
                try {
                    Database.save();
                } catch (IOException e) {
                    System.err.println("Error while saving database: " + e.getMessage());
                }
            } else {
                System.out.println("invalid command, try again.");
                System.out.println("----------");
            }
        }
    }
}
