ackage com.example.projetfinetude;
public class Users {
    String userid,name,lastName,birthdate,phone,email,password,imageuri;
    public Users(){}

    public Users(String userid, String name, String lastName, String birthdate, String phone, String email, String password, String imageuri) {
        this.userid = userid;
        this.name = name;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.imageuri = imageuri;
    }
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getImageuri() {
        return imageuri;
    }
    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }
}
