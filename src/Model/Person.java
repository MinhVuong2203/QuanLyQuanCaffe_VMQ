package Model;
public abstract class Person {
    protected int id;
    protected String name;
    protected String phone;
    protected String image;

    public Person() {
    }

    public Person(int id, String name, String phone, String image) {  
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.image = image;
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

    public void getImage(String image) {
        this.image = phone;
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

    public void setImage(String image) {
        this.image = image;
    }

   

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", name=" + name + ", phone=" + phone + ", image = " + image + '}';
    }
    
    
}
