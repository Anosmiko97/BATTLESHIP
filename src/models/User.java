package models;

public class User {
    private String name;
    private String flag;
    private int id;

    public User(String name) {
        this.name = name;
    }

    /* Getters y setters */
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return this.flag;
    }
    public void setFlag(String route) {
        this.flag = route;
    }

    public String toString() {
        return "Id: " + id + "\nName: " + name + "\nFlag: " + flag;
    }
}
