package model;

public class Mentor {
    private Long id;
    private String firstName;
    private String lastName;
    private Student student;

    public Mentor(Long id,String firstName,String lastName,Student student){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.student = student;
    }


}
