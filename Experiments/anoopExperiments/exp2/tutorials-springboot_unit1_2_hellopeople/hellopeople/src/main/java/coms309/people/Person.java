package coms309.people;


/**
 * Provides the Definition/Structure for the people row
 *
 * @author Vivek Bengre
 */

public class Person {

    private String firstName;

    private String lastName;

    private String address;

    private String telephone;

    private int age; // new field

    private String experienceLevel; // new field

    private String rank; // new field

    public Person(){
        
    }

    public Person(String firstName, String lastName, String address, String telephone){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.telephone = telephone;
        this.age = age;
        this.experienceLevel = experienceLevel;
        this.rank = rank;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age; }

    public String getExperienceLevel() { return experienceLevel; }

    public void setExperienceLevel(String experienceLevel) { this.experienceLevel = experienceLevel; }

    public String getRank() { return rank; }

    public void setRank(String rank) { this.rank = rank; }

    @Override
    public String toString() {
        return firstName + " " 
               + lastName + " "
               + address + " "
               + telephone + " "
                + age + " "
                + experienceLevel + " "
                + rank;


    }


}
