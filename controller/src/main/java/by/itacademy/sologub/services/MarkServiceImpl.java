package by.itacademy.sologub.services;

import by.itacademy.sologub.model.Mark;
import by.itacademy.sologub.MarkRepo;
import by.itacademy.sologub.model.Subject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class MarkServiceImpl extends AbstractService implements MarkService {
    private static final String PREFIX = "markRepo";
    private final Map<String, MarkRepo> repoMap;
    private volatile MarkRepo repo;

    @Autowired
    public MarkServiceImpl(Map<String, MarkRepo> repoMap) {
        this.repoMap = repoMap;
    }

    @PostConstruct
    public void init() {
        repo = repoMap.get(PREFIX + StringUtils.capitalize(type) + SUFFIX);
    }

    @Override
    public Set<Mark> getAllMarksBySubjectAndStudentId(Subject subject, int studentId) {
        return repo.getAllMarksBySubjectAndStudentId(subject, studentId);
    }

    @Override
    public Set<Mark> getAllMarksBySubject(Subject subject) {
        return repo.getAllMarksBySubject(subject);
    }

    @Override
    public Set<Mark> getAllMarksByStudentId(int studentId) {
        return repo.getAllMarksByStudentId(studentId);
    }

    @Override
    public Mark getMark(int id) {
        return repo.getMark(id);
    }

    @Override
    public boolean putMarkToStudent(Mark mark, int studentId) {
        return repo.putMarkToStudent(mark, studentId);
    }

    @Override
    public boolean changeMark(Mark newValues) {
        return repo.changeMark(newValues);
    }

    @Override
    public boolean deleteMark(int id) {
        return repo.deleteMark(id);
    }
}