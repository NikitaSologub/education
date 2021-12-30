package by.itacademy.sologub.services;

import by.itacademy.sologub.CredentialRepoHardcodeImpl;
import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.TeacherRepo;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class TeacherServiceImpl implements TeacherService {
    private static TeacherServiceImpl teacherService;
    private final TeacherRepo repo;

    private TeacherServiceImpl(TeacherRepo teacherRepo) {
        this.repo = teacherRepo;
    }

    public static TeacherServiceImpl getInstance(TeacherRepo teacherRepo) {
        if (teacherService == null) {
            synchronized (TeacherServiceImpl.class) {
                if (teacherService == null) {
                    teacherService = new TeacherServiceImpl(teacherRepo);
                }
            }
        }
        return teacherService;
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