package by.itacademy.sologub.services;

import by.itacademy.sologub.Student;
import by.itacademy.sologub.StudentRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class StudentServiceImpl extends AbstractService implements StudentService {
    private static final String PREFIX = "studentRepo";
    private final Map<String, StudentRepo> repoMap;
    private volatile StudentRepo repo;

    @Autowired
    public StudentServiceImpl(Map<String, StudentRepo> repoMap) {
        this.repoMap = repoMap;
    }

    @PostConstruct
    public void init() {
        repo = repoMap.get(PREFIX + StringUtils.capitalize(type) + SUFFIX);
    }

    @Override
    public Set<Student> getStudentsSet() {
        return repo.getStudentsSet();
    }

    @Override
    public Set<Student> getStudentsByGroupId(int groupId) {
        return repo.getStudentsByGroupId(groupId);
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(int id) {
        return repo.getStudentIfExistsOrGetSpecialValue(id);
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(String login) {
        return repo.getStudentIfExistsOrGetSpecialValue(login);
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(String login, String password) {
        return repo.getStudentIfExistsOrGetSpecialValue(login, password);
    }

    @Override
    public boolean putStudentIfNotExists(Student t) {
        return repo.putStudentIfNotExists(t);
    }

    @Override
    public boolean changeStudentParametersIfExists(Student newStudent) {
        return repo.changeStudentParametersIfExists(newStudent);
    }

    @Override
    public boolean deleteStudent(String login) {
        return repo.deleteStudent(login);
    }

    @Override
    public boolean deleteStudent(Student t) {
        return repo.deleteStudent(t);
    }
}