package repositories;

import model.Student;
import java.util.List;

public interface StudentsRepository extends CrudRepository<Student> {
    List<Student> findAllByAge(int age);
}
