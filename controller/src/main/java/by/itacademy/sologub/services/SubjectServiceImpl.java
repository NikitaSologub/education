package by.itacademy.sologub.services;

import by.itacademy.sologub.CredentialRepoHardcodeImpl;
import by.itacademy.sologub.Subject;
import by.itacademy.sologub.SubjectRepo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class SubjectServiceImpl implements SubjectService {
    private static SubjectServiceImpl StudentService;
    private final SubjectRepo repo;

    private SubjectServiceImpl(SubjectRepo subjectRepo) {
        this.repo = subjectRepo;
    }

    public static SubjectServiceImpl getInstance(SubjectRepo subjectRepo) {
        if (StudentService == null) {
            synchronized (SubjectServiceImpl.class) {
                if (StudentService == null) {
                    StudentService = new SubjectServiceImpl(subjectRepo);
                }
            }
        }
        return StudentService;
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