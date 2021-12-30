package by.itacademy.sologub.services;

import by.itacademy.sologub.Admin;
import by.itacademy.sologub.Group;
import by.itacademy.sologub.Mark;
import by.itacademy.sologub.Salary;
import by.itacademy.sologub.Student;
import by.itacademy.sologub.Subject;
import by.itacademy.sologub.Teacher;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
public class FacadeService implements GroupService, StudentService, TeacherService, MarkService, SalaryService,
        SubjectService, AdminService, AverageSalaryService {
    private static FacadeService facade;
    private final GroupService groupService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final AdminService adminService;
    private final MarkService markService;
    private final SalaryService salaryService;
    private final SubjectService subjectService;

    private FacadeService(GroupService groupS, StudentService studentS, TeacherService teacherS, AdminService adminS,
                          MarkService markS, SalaryService salaryS, SubjectService subjectS) {
        this.groupService = groupS;
        this.studentService = studentS;
        this.teacherService = teacherS;
        this.adminService = adminS;
        this.markService = markS;
        this.salaryService = salaryS;
        this.subjectService = subjectS;
    }

    public static FacadeService getInstance(GroupService groupS, StudentService studentS, TeacherService teacherS,
                                            AdminService adminS, MarkService markS, SalaryService salaryS,
                                            SubjectService subjectS) {
        if (facade == null) {
            synchronized (FacadeService.class) {
                if (facade == null) {
                    facade = new FacadeService(groupS, studentS, teacherS, adminS, markS, salaryS, subjectS);
                }
            }
        }
        return facade;
    }

    @Override
    public List<Group> getGroups() {
        return groupService.getGroups();
    }

    @Override
    public List<Group> getGroupsByTeacher(Teacher t) {
        return groupService.getGroupsByTeacher(t);
    }

    @Override
    public List<Group> getGroupsByStudent(Student s) {
        return groupService.getGroupsByStudent(s);
    }

    @Override
    public Group getGroupById(int id) {
        return groupService.getGroupById(id);
    }

    @Override
    public boolean putGroupIfNotExists(Group g) {
        return groupService.putGroupIfNotExists(g);
    }

    @Override
    public boolean changeGroupsParametersIfExists(Group g) {
        return groupService.changeGroupsParametersIfExists(g);
    }

    @Override
    public boolean addStudentInGroup(Group group, Student student) {
        return groupService.addStudentInGroup(group, student);
    }

    @Override
    public boolean removeStudentFromGroup(Group group, Student student) {
        return groupService.removeStudentFromGroup(group, student);
    }

    @Override
    public boolean addSubjectInGroup(Group group, Subject subject) {
        return groupService.addSubjectInGroup(group, subject);
    }

    @Override
    public boolean removeSubjectFromGroup(Group group, Subject subject) {
        return groupService.removeSubjectFromGroup(group, subject);
    }

    @Override
    public boolean deleteGroupIfExists(int groupId) {
        return groupService.deleteGroupIfExists(groupId);
    }

    @Override
    public Set<Student> getStudentsSet() {
        return studentService.getStudentsSet();
    }

    @Override
    public Set<Student> getStudentsByGroupId(int groupId) {
        return studentService.getStudentsByGroupId(groupId);
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(int id) {
        return studentService.getStudentIfExistsOrGetSpecialValue(id);
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(String login) {
        return studentService.getStudentIfExistsOrGetSpecialValue(login);
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(String login, String password) {
        return studentService.getStudentIfExistsOrGetSpecialValue(login, password);
    }

    @Override
    public boolean putStudentIfNotExists(Student s) {
        return studentService.putStudentIfNotExists(s);
    }

    @Override
    public boolean changeStudentParametersIfExists(Student newStudent) {
        return studentService.changeStudentParametersIfExists(newStudent);
    }

    @Override
    public boolean deleteStudent(String login) {
        return studentService.deleteStudent(login);
    }

    @Override
    public boolean deleteStudent(Student s) {
        return studentService.deleteStudent(s);
    }

    @Override
    public Set<Teacher> getTeachersSet() {
        return teacherService.getTeachersSet();
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(int id) {
        return teacherService.getTeacherIfExistsOrGetSpecialValue(id);
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login) {
        return teacherService.getTeacherIfExistsOrGetSpecialValue(login);
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login, String password) {
        return teacherService.getTeacherIfExistsOrGetSpecialValue(login, password);
    }

    @Override
    public boolean putTeacherIfNotExists(Teacher t) {
        return teacherService.putTeacherIfNotExists(t);
    }

    @Override
    public boolean changeTeachersParametersIfExists(Teacher newTeacher) {
        return teacherService.changeTeachersParametersIfExists(newTeacher);
    }

    @Override
    public boolean deleteTeacher(String login) {
        return teacherService.deleteTeacher(login);
    }

    @Override
    public boolean deleteTeacher(Teacher t) {
        return teacherService.deleteTeacher(t);
    }

    @Override
    public Set<Mark> getAllMarksBySubjectAndStudentId(Subject subject, int studentId) {
        return markService.getAllMarksBySubjectAndStudentId(subject, studentId);
    }

    @Override
    public Set<Mark> getAllMarksBySubject(Subject s) {
        return markService.getAllMarksBySubject(s);
    }

    @Override
    public Set<Mark> getAllMarksByStudentId(int studentId) {
        return markService.getAllMarksByStudentId(studentId);
    }

    @Override
    public Mark getMark(int id) {
        return markService.getMark(id);
    }

    @Override
    public boolean putMarkToStudent(Mark mark, int studentId) {
        return markService.putMarkToStudent(mark, studentId);
    }

    @Override
    public boolean changeMark(Mark newValues) {
        return markService.changeMark(newValues);
    }

    @Override
    public boolean deleteMark(int id) {
        return markService.deleteMark(id);
    }

    @Override
    public Set<Salary> getAllSalariesByTeacherId(int teacherId) {
        return salaryService.getAllSalariesByTeacherId(teacherId);
    }

    @Override
    public Set<Salary> getAllSalariesByTeacherIdAfterDate(int teacherId, LocalDate date) {
        return salaryService.getAllSalariesByTeacherIdAfterDate(teacherId, date);
    }

    @Override
    public Salary getSalary(int id) {
        return salaryService.getSalary(id);
    }

    @Override
    public boolean putSalaryToTeacher(Salary salary, int teacherId) {
        return salaryService.putSalaryToTeacher(salary, teacherId);
    }

    @Override
    public boolean changeSalary(Salary newValues) {
        return salaryService.changeSalary(newValues);
    }

    @Override
    public boolean deleteSalary(int id) {
        return salaryService.deleteSalary(id);
    }

    @Override
    public List<Subject> getSubjectsList() {
        return subjectService.getSubjectsList();
    }

    @Override
    public Set<Subject> getSubjectsByGroupId(int groupId) {
        return subjectService.getSubjectsByGroupId(groupId);
    }

    @Override
    public Subject getSubjectIfExistsOrGetSpecialValue(int id) {
        return subjectService.getSubjectIfExistsOrGetSpecialValue(id);
    }

    @Override
    public boolean putSubjectIfNotExists(Subject s) {
        return subjectService.putSubjectIfNotExists(s);
    }

    @Override
    public boolean changeSubjectsParametersIfExists(Subject s) {
        return subjectService.changeSubjectsParametersIfExists(s);
    }

    @Override
    public boolean deleteSubject(Subject s) {
        return subjectService.deleteSubject(s);
    }

    @Override
    public Set<Admin> getAdminsList() {
        return adminService.getAdminsList();
    }

    @Override
    public Admin getAdminIfExistsOrGetSpecialValue(String login) {
        return adminService.getAdminIfExistsOrGetSpecialValue(login);
    }

    @Override
    public Admin getAdminIfExistsOrGetSpecialValue(String login, String password) {
        return adminService.getAdminIfExistsOrGetSpecialValue(login, password);
    }

    @Override
    public boolean putAdminIfNotExists(Admin a) {
        return adminService.putAdminIfNotExists(a);
    }

    @Override
    public boolean changeAdminParametersIfExists(Admin newAdmin) {
        return adminService.changeAdminParametersIfExists(newAdmin);
    }

    @Override
    public boolean deleteAdmin(String login) {
        return adminService.deleteAdmin(login);
    }

    @Override
    public boolean deleteAdmin(Admin a) {
        return adminService.deleteAdmin(a);
    }

    @Override
    public String getAverageSalary(int teacherId) {
        double average = salaryService.getAllSalariesByTeacherId(teacherId).stream()
                .mapToInt(Salary::getCoins)
                .average().orElse(0.0);
        return round(average);
    }

    private String round(double val) {
        return String.format("%.2f", val);
    }
}