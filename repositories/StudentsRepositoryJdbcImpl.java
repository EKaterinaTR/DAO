package repositories;

import model.Student;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 10.07.2020
 * 01. Database
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
public class StudentsRepositoryJdbcImpl implements StudentsRepository {

    //language=SQL
    private static final String SQL_SELECT_BY_ID = "select * from student where id = ";
    private static final String SQL_UPDATE_START = "update student set ";
    private static final String SQL_UPDATE_FIRST_NAME="first_name = ";
    private static final String SQL_UPDATE_LAST_NAME =", last_name = ";
    private static final String SQL_UPDATE_AGE =", age = ";
    private static final String SQL_UPDATE_GROUP_NUMBER =", group_number = ";
    private static final String SQL_UPDATE_END = "where id = ";
    private static final String SQL_INSERT_START = "insert into student(first_name,last_name,age,group_number) VALUES(";
    private static final String SQL_INSERT_END = ") returning id";

    private Connection connection;

    public StudentsRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Student> findAllByAge(int age) {
        return null;
    }

    // Необходимо вытащить список всех студентов, при этом у каждого студента должен быть проставлен список менторов
    // у менторов в свою очередь ничего проставлять (кроме имени, фамилии, id не надо)
    // student1(id, firstName, ..., mentors = [{id, firstName, lastName, null}, {}, ), student2, student3
    // все сделать одним запросом
    @Override
    public List<Student> findAll() {
        return null;
    }

    // менторы отдельным запросом
    @Override
    public Student findById(Long id) {
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = connection.createStatement();
            result = statement.executeQuery(SQL_SELECT_BY_ID + id);
            if (result.next()) {
                Student s = new Student(
                        result.getLong("id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getInt("age"),
                        result.getInt("group_number")
                );
                s.setMentors((new MentorRepositoryJdbcImpl(connection)).findByStudentId(s.getId()));
                return s;
            } else return null;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }


    //+без менторов
    @Override
    public void save(Student entity) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_INSERT_START
                    + ((entity.getFirstName() == null)?null:('\''+ entity.getFirstName()+'\''))
                    + ','
                    + ((entity.getLastName() == null)?null:('\''+ entity.getLastName()+'\''))
                    + ','
                    + entity.getAge()
                    + ','
                    + entity.getGroupNumber()
                    + ") returning id"
            );
            if (resultSet.next()) {
                entity.setId(resultSet.getLong(1));
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }


    }


    //+ без меторов
    @Override
    public void update(Student entity) {
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(SQL_UPDATE_START
                    + SQL_UPDATE_FIRST_NAME
                    +((entity.getFirstName() == null)?null:('\''+ entity.getFirstName()+'\''))
                    + SQL_UPDATE_LAST_NAME
                    + ((entity.getLastName() == null)?null:('\''+ entity.getLastName()+'\''))
                    + SQL_UPDATE_AGE
                    + entity.getAge()
                    + SQL_UPDATE_GROUP_NUMBER
                    + entity.getGroupNumber()
                    + SQL_UPDATE_END + entity.getId());
            //без обновления менторов

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }

}

