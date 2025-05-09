package Model;
public abstract class Person {
    protected int id;
    protected String name;
    protected String phone;

    public Person() {
    }

    public Person(int id, String name, String phone) {  
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOnlyName(){
        String [] nameParts = name.split(" ");
        return nameParts[nameParts.length - 1];
    }

    public String getPhone() {
        return phone;
    }

    public void setId(int id) {
        this.id = id;
    }   

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", name=" + name + ", phone=" + phone + "}";
    }
    
    
}
