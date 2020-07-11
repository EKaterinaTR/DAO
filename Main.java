

import jdbc.SimpleDataSource;
import model.Student;
import repositories.StudentsRepository;
import repositories.StudentsRepositoryJdbcImpl;

import java.sql.*;

public class Main {

    private static final String URL = "jdbc:postgresql://localhost:5432/University";
    private static final String USER = "postgres";
    private static final String PASSWORD = "pos3ril10";


    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        StudentsRepository studentsRepository = new StudentsRepositoryJdbcImpl(connection);
        Student s = studentsRepository.findById(2L);
        System.out.println(s);
        s.setFirstName("Роберт");
        s.setAge(20);
        studentsRepository.update(s);
        Student student = new Student(null,"М","Барсиков",18,111);
        studentsRepository.save(student);
        System.out.println(student.getId());
        connection.close();

    }
}
