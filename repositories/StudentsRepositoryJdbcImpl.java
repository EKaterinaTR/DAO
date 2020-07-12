package repositories;

import model.Mentor;
import model.Student;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
    private static final String SQL_SELECT_BY_ID = "select student.id as stud_id,student.first_name as stud_fn,student.last_name as stud_ln,age,group_number,\n" +
            "       mentor.id,mentor.first_name,mentor.last_name\n" +
            "from student left join mentor on student.id = student_id\n" +
            "where student.id = ";
    private static final String SQL_SELECT_BY_AGE = "select student.id as stud_id,student.first_name as stud_fn,student.last_name as stud_ln,age,group_number,\n" +
            "       mentor.id,mentor.first_name,mentor.last_name\n" +
            "from student left join mentor on student.id = student_id\n" +
            "where age = ";
    private static final String SQL_SELECT_ALL = "select student.id as stud_id,student.first_name as stud_fn," +
            "student.last_name as stud_ln,age,group_number,mentor.id,mentor.first_name,mentor.last_name" +
            " from student left join mentor on student.id = student_id " +
            "order by student.id";
    private static final String SQL_UPDATE_START = "update student set ";
    private static final String SQL_UPDATE_FIRST_NAME="first_name = ";
    private static final String SQL_UPDATE_LAST_NAME =", last_name = ";
    private static final String SQL_UPDATE_AGE =", age = ";
    private static final String SQL_UPDATE_GROUP_NUMBER =", group_number = ";
    private static final String SQL_UPDATE_END = "where id = ";
    private static final String SQL_INSERT_START = "insert into student(first_name,last_name,age,group_number) VALUES(";
    private static final String SQL_INSERT_END = ") returning id";
    private static final String SQL_DELETE = "delete from mentor where student_id = ";

    private Connection connection;

    public StudentsRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }
    //+
    @Override
    public List<Student> findAllByAge(int age) {
        Statement statement = null;
        ResultSet result = null;
        List<Student>list = new ArrayList<>();
        Student s = null;

        try {
            statement = connection.createStatement();
            result = statement.executeQuery(SQL_SELECT_BY_AGE + age);
            while (result.next()) {
                long id = result.getLong("stud_id");
                if(s == null || id != s.getId()){
                    if(s !=null ) list.add(s);
                    s = new Student(
                            id,
                            result.getString("stud_fn"),
                            result.getString("stud_ln"),
                            result.getInt("age"),
                            result.getInt("group_number")
                    );
                }
                if(result.getLong("id") != 0) {
                    s.getMentors().add(new Mentor(
                            result.getLong("id"),
                            result.getString("first_name"),
                            result.getString("last_name"),
                            s
                    ));
                }
            }
            if(s != null) {
                list.add(s);
            }
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
        return list;
    }

    //+
    @Override
    public List<Student> findAll() {
        Statement statement = null;
        ResultSet result = null;
        List<Student> list = new ArrayList<>();
        Student s = null;

        try {
            statement = connection.createStatement();
            result = statement.executeQuery(SQL_SELECT_ALL);
            while (result.next()) {
                long id = result.getLong("stud_id");
                if(s == null || id != s.getId()){
                    if(s !=null ) list.add(s);
                    s = new Student(
                            id,
                            result.getString("stud_fn"),
                            result.getString("stud_ln"),
                            result.getInt("age"),
                            result.getInt("group_number")
                    );
                }
                if(result.getLong("id") !=0 ) {
                    s.getMentors().add(new Mentor(
                            result.getLong("id"),
                            result.getString("first_name"),
                            result.getString("last_name"),
                            s
                    ));
                }
            }
            if(s != null) {
                list.add(s);
            }
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
        return list;
    }

    //+
    @Override
    public Student findById(Long id) {
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = connection.createStatement();
            result = statement.executeQuery(SQL_SELECT_BY_ID + id);
            if (result.next()) {
                Student s = new Student(
                        result.getLong("stud_id"),
                        result.getString("stud_fn"),
                        result.getString("stud_ln"),
                        result.getInt("age"),
                        result.getInt("group_number")
                );
                if(result.getLong("id" ) != 0){
                    s.getMentors().add(new Mentor(
                            result.getLong("id"),
                            result.getString("first_name"),
                            result.getString("last_name"),
                            s
                    ));
                }
                while (result.next()) {
                    s.getMentors().add(new Mentor(
                            result.getLong("id"),
                            result.getString("first_name"),
                            result.getString("last_name"),
                            s
                    ));
                }
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


    //+
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
                    + SQL_INSERT_END
            );
            if (resultSet.next()) {
                entity.setId(resultSet.getLong(1));
            }


            List<Mentor> mentors = entity.getMentors();

            String SQL_SAVE_MENTORS = "insert into mentor(first_name,last_name,student_id) values";
            Mentor last = mentors.get(mentors.size()-1);
            for (Mentor m : mentors) {
                SQL_SAVE_MENTORS+= "("+ ((m.getFirstName() == null)?null:('\''+ m.getFirstName()+'\'')) +
                        ',' + ((m.getLastName() == null)?null:('\''+ m.getLastName()+'\'')) +
                        ',' + entity.getId()+')';
                SQL_SAVE_MENTORS+=(m.equals(last))?"returning id;":',';
            }

            resultSet = statement.executeQuery(SQL_SAVE_MENTORS);
            int i = 0;
            while (resultSet.next()){
                mentors.get(i).setId(resultSet.getLong(1));
                i++;
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


    //+
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
            statement.executeUpdate(SQL_DELETE + entity.getId());

            List<Mentor> mentors = entity.getMentors();

            String SQL_SAVE_MENTORS = "insert into mentor(id,first_name,last_name,student_id) values";
            Mentor last = mentors.get(mentors.size()-1);
            for (Mentor m : mentors) {
                SQL_SAVE_MENTORS+= "("+ m.getId() +
                        ',' + ((m.getFirstName() == null)?null:('\''+ m.getFirstName()+'\'')) +
                        ',' + ((m.getLastName() == null)?null:('\''+ m.getLastName()+'\'')) +
                        ',' + entity.getId()+')';
                SQL_SAVE_MENTORS+=(m.equals(last))?';':',';
            }

            statement.executeUpdate(SQL_SAVE_MENTORS);

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

