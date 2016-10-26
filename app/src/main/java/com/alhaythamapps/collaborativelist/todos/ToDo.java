package com.alhaythamapps.collaborativelist.todos;

/**
 * @author Alhaytham Alfeel on 19/10/2016.
 */
public class ToDo {
    private String name;
    private String email;
    private boolean completed;

//    public ToDo(String name, String email) {
//        this.name = name;
//        this.email = email;
//    }

    @Override
    public String toString() {
        return "Name: " + name + ", Email: " + email + ", Done: " + String.valueOf(completed);
    }

    public String getName() {
        return name;
    }

    public ToDo setName(String name) {
        this.name = name;

        return this;
    }

    public String getEmail() {
        return email;
    }

    public ToDo setEmail(String email) {
        this.email = email;

        return this;
    }

    public boolean isCompleted() {
        return completed;
    }

    public ToDo setCompleted(boolean completed) {
        this.completed = completed;

        return this;
    }
}
