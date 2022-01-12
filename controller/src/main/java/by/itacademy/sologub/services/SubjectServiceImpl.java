package by.itacademy.sologub.services;

import by.itacademy.sologub.Subject;
import by.itacademy.sologub.SubjectRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class SubjectServiceImpl extends AbstractService implements SubjectService {
    private static final String PREFIX = "subjectRepo";
    private final Map<String, SubjectRepo> repoMap;
    private volatile SubjectRepo repo = null;

    @Autowired
    public SubjectServiceImpl(Map<String, SubjectRepo> repoMap) {
        this.repoMap = repoMap;
    }

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