package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static by.itacademy.sologub.constants.ConstantObject.GROUP_NOT_EXISTS;

@Slf4j
@Repository
public class GroupRepoMemoryImpl implements GroupRepo {
    private static int CURRENT_MAX_GROUP_ID = 17;
    private static volatile Map<Integer, Group> groups;

    @Autowired
    public GroupRepoMemoryImpl() {
        groups = new ConcurrentHashMap<>();
    }

    @Override
    public List<Group> getGroups() {
        log.debug("Возвращаем все группы в бд");
        return new ArrayList<>(groups.values());
    }

    @Override
    public List<Group> getGroupsByTeacher(Teacher teacher) {
        int teacherId = teacher.getId();
        log.debug("Возвращаем все группы по teacherId={}", teacher.getId());
        return groups.values().stream()
                .filter(g -> g.getTeacher().getId() == teacherId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Group> getGroupsByStudent(Student student) {
        int studentId = student.getId();
        log.debug("Возвращаем все группы по studentId={}", studentId);
        return groups.values().stream()
                .filter(g -> g.getStudents().stream()
                        .anyMatch(st -> st.getId() == studentId))
                .collect(Collectors.toList());
    }

    @Override
    public Group getGroupById(int id) {
        Group gr = groups.getOrDefault(id, GROUP_NOT_EXISTS);
        log.debug("По id={} нашли группу {}", id, gr);
        return gr;
    }

    @Override
    public boolean putGroupIfNotExists(Group group) {
        log.debug("Пытаемся проверить, есть ли {} в репозитории", group);
        if (isTitleExists(group)) {
            log.debug("Не удалось добавить {} в репозиторий. Группа с таким title уже существует", group);
            return false;
        } else {
            int groupId = CURRENT_MAX_GROUP_ID++;
            group.setId(groupId);
            log.debug("Пытаемся добавить {} в репозиторий", group);
            groups.put(groupId, group);
            return true;
        }
    }

    private boolean isTitleExists(Group group) {
        return groups.values().stream().anyMatch(g -> g.getTitle().equals(group.getTitle()));
    }

    @Override
    public boolean changeGroupsParametersIfExists(Group newGr) {
        Group oldGr = groups.getOrDefault(newGr.getId(), GROUP_NOT_EXISTS);
        if (GROUP_NOT_EXISTS == oldGr) {
            log.debug("Такой группы не существует. Нечего изменять");
            return false;
        } else {
            boolean isTitleSame = oldGr.getTitle().equals(newGr.getTitle());
            if (!isTitleSame) {
                if (isTitleExists(newGr)) {
                    log.debug("Нельзя менять title {}.Такой title уже есть у другой Group", newGr.getTitle());
                    return false;
                } else {
                    log.debug("Меняем title на {}", newGr.getTitle());
                    oldGr.setTitle(newGr.getTitle());
                }
            }
            log.debug("Задаём значение для Teacher = {}", newGr.getTeacher());
            oldGr.setTeacher(newGr.getTeacher());
            log.debug("Задаём значение для Description = {}", newGr.getDescription());
            oldGr.setDescription(newGr.getDescription());
            log.debug("Group {} изменила свои значения", oldGr);
            return true;
        }
    }

    @Override
    public boolean addStudentInGroup(Group group, Student student) {
        log.debug("пытаемся добавить студента в группу");
        boolean isAdded = group.getStudents().add(student);
        if (isAdded) {
            log.debug("Студент {} не добавлен. Возможно он уже есть в группе", student);
        } else {
            log.debug("Студент {} добавлен в группу", student);
        }
        return isAdded;
    }

    @Override
    public boolean removeStudentFromGroup(Group group, Student student) {
        log.debug("пытаемся удалить студента из группы");
        boolean isRemoved = group.getStudents().remove(student);
        if (isRemoved) {
            log.debug("Студент {} не удалён. Возможно его не было в группе", student);
        } else {
            log.debug("Студент {} удалён из группы", student);
        }
        return isRemoved;
    }

    @Override
    public boolean addSubjectInGroup(Group group, Subject subject) {
        log.debug("пытаемся добавить предмет в группу");
        boolean isAdded = group.getSubjects().add(subject);
        if (isAdded) {
            log.debug("Предмет {} не добавлен. Возможно он уже есть в группе", subject);
        } else {
            log.debug("Предмет {} добавлен в группу", subject);
        }
        return isAdded;
    }

    @Override
    public boolean removeSubjectFromGroup(Group group, Subject subject) {
        log.debug("пытаемся удалить предмет из группы");
        boolean isRemoved = group.getSubjects().remove(subject);
        if (isRemoved) {
            log.debug("Предмет {} не удалён. Возможно его не было в группе", subject);
        } else {
            log.debug("Предмет {} удалён из группы", subject);
        }
        return isRemoved;
    }

    @Override
    public boolean deleteGroupIfExists(int groupId) {
        Group gr = getGroupById(groupId);
        log.debug("Пытаемся удалить группу с id={} из репозитория", groupId);

        if (GROUP_NOT_EXISTS == gr) {
            log.info("Объекта с id={} нет в базе. Нечего удалять", groupId);
        } else {
            groups.remove(gr.getId());
            log.info("Объект group удалён из репозитория");
            return true;
        }
        return false;
    }
}