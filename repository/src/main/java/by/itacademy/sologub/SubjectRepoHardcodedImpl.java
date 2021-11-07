package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static by.itacademy.sologub.constants.ConstantObject.SUBJECT_NOT_EXISTS;

@Slf4j
public class SubjectRepoHardcodedImpl implements SubjectRepo {
    static int CURRENT_MAX_SUBJECT_ID = 263;
    private static volatile SubjectRepoHardcodedImpl instance;
    private static volatile Map<Integer, Subject> subjects;

    private SubjectRepoHardcodedImpl() {
        subjects = new ConcurrentHashMap<>();
    }

    public static SubjectRepoHardcodedImpl getInstance() {
        if (instance == null) {
            synchronized (SubjectRepoHardcodedImpl.class) {
                if (instance == null) {
                    instance = new SubjectRepoHardcodedImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public List<Subject> getSubjectsList() {
        return new ArrayList<>(subjects.values());
    }

    @Override
    public Subject getSubjectIfExistsOrGetSpecialValue(int id) {
        return subjects.getOrDefault(id, SUBJECT_NOT_EXISTS);
    }

    @Override
    public boolean putSubjectIfNotExists(Subject subject) {
        boolean isExists = subjects.values().stream()
                .map(Subject::getTitle)
                .anyMatch(s -> s.equals(subject.getTitle()));
        if (isExists) {
            log.info("Subject {} не может быть добавлен в репозиторий. Такой id уже существует", subject);
            return false;
        } else {
            Integer key = CURRENT_MAX_SUBJECT_ID++;
            subject.setId(key);
            subjects.put(key, subject);
            log.info("Subject {} добавлен в репозиторий", subject);
            return true;
        }
    }

    @Override
    public boolean changeSubjectsParametersIfExists(Subject newS) {
        Subject oldS = subjects.getOrDefault(newS.getId(), SUBJECT_NOT_EXISTS);
        if (oldS == SUBJECT_NOT_EXISTS) {
            log.info("Нельзя изменить Subject. id={} нет в базе", newS.getId());
        } else {
            boolean isUnique = subjects.values().stream()
                    .map(Subject::getTitle)
                    .noneMatch(s -> s.equals(newS.getTitle()));
            if (isUnique) {
                oldS.setTitle(newS.getTitle());
                log.info("Изменяем Subject.на {}", oldS);
                return true;
            } else {
                log.info("Нельзя изменить Subject. title={} уже есть в базе", newS.getTitle());
            }
        }
        return false;
    }

    @Override
    public boolean deleteSubject(Subject subject) {
        return subjects.remove(subject.getId(), subject);
    }
}