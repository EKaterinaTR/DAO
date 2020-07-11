package repositories;

import model.Mentor;

import java.util.List;

public interface MentorRepository extends CrudRepository<Mentor> {
    List<Mentor> findByStudentId(long id);
    void updateMentors(List<Mentor> mentors);
}
