package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static by.itacademy.sologub.constants.ConstantObject.MARK_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.SUBJECT_NOT_EXISTS;

@Slf4j
public class MarkRepoHardcodedImpl implements MarkRepo {
    static int CURRENT_MAX_MARK_ID = 304;
    private static volatile MarkRepoHardcodedImpl instance;
    private static volatile StudentRepoHardcodedImpl studentRepo;
    private static volatile SubjectRepoHardcodedImpl subjectRepo;
    private final Map<Integer, Mark> repo;

    private MarkRepoHardcodedImpl(StudentRepoHardcodedImpl studentRepo, SubjectRepoHardcodedImpl subjectRepo) {
        repo = new ConcurrentHashMap<>();
        MarkRepoHardcodedImpl.studentRepo = studentRepo;
        MarkRepoHardcodedImpl.subjectRepo = subjectRepo;
    }

    public static MarkRepoHardcodedImpl getInstance(StudentRepoHardcodedImpl studentRepo, SubjectRepoHardcodedImpl subjectRepo) {
        if (instance == null) {
            synchronized (MarkRepoHardcodedImpl.class) {
                if (instance == null) {
                    instance = new MarkRepoHardcodedImpl(studentRepo, subjectRepo);
                }
            }
        }
        return instance;
    }

    @Override
    public Set<Mark> getAllMarksBySubjectAndStudentId(Subject subject, int studentId) {
        Student s = studentRepo.getStudentIfExistsOrGetSpecialValue(studentId);
        if (STUDENT_NOT_EXISTS == s) {
            log.info("Студент по id={} не найден возвращаем пустой сет", studentId);
            return new HashSet<>();
        } else {
            log.info("Возвращаем оценки по {} которые есть у студента с id={}", subject, studentId);
            return s.getMarks().stream()
                    .filter(m -> m.getSubject().getId() == subject.getId())
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public Set<Mark> getAllMarksBySubject(Subject subject) {
        Subject s = subjectRepo.getSubjectIfExistsOrGetSpecialValue(subject.getId());
        if (SUBJECT_NOT_EXISTS == s) {
            log.info("Предмет не найден возвращаем пустой сет");
            return new HashSet<>();
        } else {
            log.info("{} найден возвращаем все оценки", subject);
            return studentRepo.getStudentsSet().stream()
                    .map(Student::getMarks)
                    .flatMap(Collection::stream)
                    .filter(mark -> mark.getSubject().getId() == s.getId())
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public Set<Mark> getAllMarksByStudentId(int studentId) {
        Student s = studentRepo.getStudentIfExistsOrGetSpecialValue(studentId);
        if (STUDENT_NOT_EXISTS == s) {
            log.info("Студент по id={} не найден возвращаем пустой сет", studentId);
            return new HashSet<>();
        } else {
            log.info("Студент найден по id={} возвращаем его оценки", studentId);
            return s.getMarks();
        }
    }

    @Override
    public Mark getMark(int id) {
        Mark m = repo.getOrDefault(id, MARK_NOT_EXISTS);
        log.info("Оценка {} найдена в репозитории по id={}", m, id);
        return m;
    }

    @Override
    public boolean putMarkToStudent(Mark mark, int studentId) {
        Integer key = CURRENT_MAX_MARK_ID++;
        if (repo.containsKey(key)) {
            log.info("Оценка {} не может быть добавлена в репозиторий. Такой id уже существует", mark);
        } else {
            Student s = studentRepo.getStudentIfExistsOrGetSpecialValue(studentId);
            if (STUDENT_NOT_EXISTS == s) {
                log.info("Нельзя добавить оценку студенту Нет студента с таким id={}", studentId);
            } else {
                mark.setId(key);
                repo.put(key, mark);
                s.getMarks().add(mark);
                log.info("Оценка {} добавлена в репозиторий", mark);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean changeMark(Mark newValues) {
        if (repo.containsKey(newValues.getId())) {
            Mark oldValues = repo.get(newValues.getId());
            if (newValues.equals(oldValues)) {
                log.info("Нечего менять. Обьекты оценок имеют эквивалентные значения");
            } else {//Я считаю что предмет у оценки не должен меняться!
                oldValues.withPoint(newValues.getPoint())
                        .withDate(newValues.getDate());
                log.info("Обьект оценки изменён и имеет вид: {}", oldValues);
                return true;
            }
        } else {
            log.info("Нечего менять. Оценка с id={} не существует в репозитории", newValues.getId());
        }
        return false;
    }

    @Override
    public boolean deleteMark(int id) {
        if (repo.containsKey(id)) {
            studentRepo.getStudentsSet().stream()
                    .map(Student::getMarks)
                    .forEach(set -> set.removeIf(mark -> mark.getId() == id));
            repo.remove(id);
            log.info("Оценка по id {} удалена из репозитория", id);
            return true;
        } else {
            log.info("Не удалось удалить оценку по id {} не существует такой оценки в репозитории", id);
        }
        return false;
    }
}
