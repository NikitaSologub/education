package by.itacademy.sologub.services;

import by.itacademy.sologub.SubjectRepo;
import by.itacademy.sologub.model.Subject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class SubjectServiceImpl extends AbstractService implements SubjectService {
    private static final String PREFIX = "subjectRepo";
    private final Map<String, SubjectRepo> repoMap;
    private volatile SubjectRepo repo = null;

    @PostConstruct
    public void init() {
        repo = repoMap.get(PREFIX + StringUtils.capitalize(type) + SUFFIX);
    }

    @Override
    public List<Subject> getSubjectsList() {
        return repo.getSubjectsList();
    }

    @Override
    public Set<Subject> getSubjectsByGroupId(int groupId) {
        return repo.getSubjectsByGroupId(groupId);
    }

    @Override
    public Subject getSubjectIfExistsOrGetSpecialValue(int id) {
        return repo.getSubjectIfExistsOrGetSpecialValue(id);
    }

    @Override
    public boolean putSubjectIfNotExists(Subject s) {
        return repo.putSubjectIfNotExists(s);
    }

    @Override
    public boolean changeSubjectsParametersIfExists(Subject s) {
        return repo.changeSubjectsParametersIfExists(s);
    }

    @Override
    public boolean deleteSubject(Subject s) {
        return repo.deleteSubject(s);
    }
}