package repositories;

import model.Mentor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MentorRepositoryJdbcImpl implements MentorRepository {


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

}
