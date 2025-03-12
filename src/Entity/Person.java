package Entity;
public abstract class Person {
    protected int id;
    protected String name;
    protected String phone;
    protected int id_auto = 10000; // Cho id bắt đầu từ 10000 

    public Person(int id, String name, String phone) {  // Phục vụ cho khách hàng vãi lai, sẽ cho id ảo là 10000
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public Person(String name, String phone) {
        this.id = ++id_auto;
        this.name = name;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", name=" + name + ", phone=" + phone + '}';
    }
}
