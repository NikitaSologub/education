package by.itacademy.sologub.services;

import by.itacademy.sologub.TeacherRepo;
import by.itacademy.sologub.model.Teacher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class TeacherServiceImpl extends AbstractService implements TeacherService {
    private static final String PREFIX = "teacherRepo";
    private final Map<String, TeacherRepo> repoMap;
    private TeacherRepo repo = null;

    @PostConstruct
    public void init() {
        repo = repoMap.get(PREFIX + StringUtils.capitalize(type) + SUFFIX);
    }

    @Override
    public Set<Teacher> getTeachersSet() {
        return repo.getTeachersSet();
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(int id) {
        return repo.getTeacherIfExistsOrGetSpecialValue(id);
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login) {
        return repo.getTeacherIfExistsOrGetSpecialValue(login);
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login, String password) {
        return repo.getTeacherIfExistsOrGetSpecialValue(login, password);
    }

    @Override
    public boolean putTeacherIfNotExists(Teacher t) {
        return repo.putTeacherIfNotExists(t);
    }

    @Override
    public boolean changeTeachersParametersIfExists(Teacher newTeacher) {
        return repo.changeTeachersParametersIfExists(newTeacher);
    }

    @Override
    public boolean deleteTeacher(String login) {
        return repo.deleteTeacher(login);
    }

    @Override
    public boolean deleteTeacher(Teacher t) {
        return repo.deleteTeacher(t);
    }
}