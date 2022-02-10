package by.itacademy.sologub.spring_data;

import by.itacademy.sologub.SubjectRepo;
import by.itacademy.sologub.model.Group;
import by.itacademy.sologub.model.Mark;
import by.itacademy.sologub.model.Subject;
import by.itacademy.sologub.spring_data.crud.GroupDataAccess;
import by.itacademy.sologub.spring_data.crud.MarkDataAccess;
import by.itacademy.sologub.spring_data.crud.SubjectDataAccess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static by.itacademy.sologub.constants.ConstantObject.GROUP_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.SUBJECT_NOT_EXISTS;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Repository
public class SubjectRepoSpringDataImpl implements SubjectRepo {
    private final SubjectDataAccess subjectDao;
    private final GroupDataAccess groupDao;
    private final MarkDataAccess markDao;

    @Override
    @Transactional(readOnly = true)
    public List<Subject> getSubjectsList() {
        return subjectDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Subject> getSubjectsByGroupId(int groupId) {
        Group group = groupDao.findById(groupId).orElse(GROUP_NOT_EXISTS);
        if (GROUP_NOT_EXISTS != group) {
            return group.getSubjects();
        }
        return new HashSet<>();
    }

    @Override
    @Transactional(readOnly = true)
    public Subject getSubjectIfExistsOrGetSpecialValue(int id) {
        return subjectDao.findById(id).orElse(SUBJECT_NOT_EXISTS);
    }

    @Override
    public boolean putSubjectIfNotExists(Subject subject) {
        Subject subjectInDb = subjectDao.findByTitle(subject.getTitle()).orElse(SUBJECT_NOT_EXISTS);
        if (SUBJECT_NOT_EXISTS == subjectInDb && subject.getId() == 0) {
            return subjectDao.save(subject).getId() != 0;
        }
        return false;
    }

    @Override
    public boolean changeSubjectsParametersIfExists(Subject subject) {
        Subject subjectInDb = subjectDao.findById(subject.getId()).orElse(SUBJECT_NOT_EXISTS);
        if (SUBJECT_NOT_EXISTS != subjectInDb) {
            Subject updatedSubject = subjectDao.save(subject);
            return updatedSubject.equals(subjectInDb);
        }
        return false;
    }

    @Override
    public boolean deleteSubject(Subject subject) {
        Set<Mark> marksToDelete = markDao.findAllBySubject(subject);
        markDao.deleteAll(marksToDelete);

        List<Group> groupsToExcludeSubject = groupDao.findAll();
        groupsToExcludeSubject.forEach(group -> group.getSubjects().remove(subject));
        groupDao.saveAll(groupsToExcludeSubject);

        subjectDao.delete(subject);
        return !subjectDao.existsById(subject.getId());
    }
}