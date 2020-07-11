package repositories;

import model.Mentor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MentorRepositoryJdbcImpl implements MentorRepository {

    private static final String SQL_SELECT_BY_STUDENT = "select * from mentor where student_id = ";
    private Connection connection;

    public MentorRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Mentor> findAll() {
        return null;
    }

    @Override
    public Mentor findById(Long id) {
        return null;
    }

    @Override
    public void save(Mentor entity) {

    }

    @Override
    public void update(Mentor entity) {

    }

    @Override
    public  List<Mentor> findByStudentId(long id) {
        Statement statement = null;
        ResultSet result = null;
        List<Mentor> mentors = null;


        try {
            statement = connection.createStatement();
            result = statement.executeQuery(SQL_SELECT_BY_STUDENT + id);
            while (result.next()) {
                mentors = new ArrayList<>();
                mentors.add(new Mentor(result.getLong("id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        null));

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
        return mentors;
    }

    @Override
    public void updateMentors(List<Mentor> mentors) {
        
    }
}
