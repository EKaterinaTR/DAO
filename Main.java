

import jdbc.SimpleDataSource;
import model.Mentor;
import model.Student;
import repositories.StudentsRepository;
import repositories.StudentsRepositoryJdbcImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String URL = "jdbc:postgresql://localhost:5432/University";
    private static final String USER = "postgres";
    private static final String PASSWORD = "";


    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        StudentsRepository studentsRepository = new StudentsRepositoryJdbcImpl(connection);
        /*Student s = studentsRepository.findById(2L);
        System.out.println(s);
        s.setFirstName("Роберт");
        s.setAge(20);
        studentsRepository.update(s);
        Student student = new Student(null,"ли","Барсиков",18,111);
        List<Mentor> mentors = new ArrayList<>();
        mentors.add(new Mentor(null,"J","Семенов",student));
        mentors.add(new Mentor(null,"Дж","Семенов",student));
        student.setMentors(mentors);
        studentsRepository.save(student);
        System.out.println(student.toString());*/
        List<Student> students = studentsRepository.findAll();
        for(Student s : students){
            System.out.println(s.toString());
        }

        connection.close();

    }
}
