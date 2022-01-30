package by.itacademy.sologub.config;

import by.itacademy.sologub.model.Admin;
import by.itacademy.sologub.memory.AdminRepoMemoryImpl;
import by.itacademy.sologub.model.Credential;
import by.itacademy.sologub.model.Group;
import by.itacademy.sologub.memory.GroupRepoMemoryImpl;
import by.itacademy.sologub.model.Mark;
import by.itacademy.sologub.memory.MarkRepoMemoryImpl;
import by.itacademy.sologub.model.Salary;
import by.itacademy.sologub.memory.SalaryRepoMemoryImpl;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.memory.StudentRepoMemoryImpl;
import by.itacademy.sologub.model.Subject;
import by.itacademy.sologub.memory.SubjectRepoMemoryImpl;
import by.itacademy.sologub.model.Teacher;
import by.itacademy.sologub.memory.TeacherRepoMemoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.Month;

import static by.itacademy.sologub.constants.Constant.MEMORY_TYPE;

@PropertySource("classpath:db_config.properties")
@Slf4j
@Component
public class MemoryContentInitializer {
    private final AdminRepoMemoryImpl adminRepo;
    private final TeacherRepoMemoryImpl teacherRepo;
    private final StudentRepoMemoryImpl studentRepo;
    private final GroupRepoMemoryImpl groupRepo;
    private final SalaryRepoMemoryImpl salaryRepo;
    private final MarkRepoMemoryImpl markRepo;
    private final SubjectRepoMemoryImpl subjectRepo;
    private @Value("${type}")
    String type;

    @Autowired
    private MemoryContentInitializer(AdminRepoMemoryImpl adminRepoMemory, TeacherRepoMemoryImpl teacherRepoMemory,
                                     StudentRepoMemoryImpl studentRepoMemory, GroupRepoMemoryImpl groupRepoMemory,
                                     SalaryRepoMemoryImpl salaryRepoMemory, MarkRepoMemoryImpl markRepoMemory,
                                     SubjectRepoMemoryImpl subjectRepoMemory) {
        this.adminRepo = adminRepoMemory;
        this.teacherRepo = teacherRepoMemory;
        this.studentRepo = studentRepoMemory;
        this.groupRepo = groupRepoMemory;
        this.salaryRepo = salaryRepoMemory;
        this.markRepo = markRepoMemory;
        this.subjectRepo = subjectRepoMemory;
    }

    @PostConstruct
    private void setContextIfMemory() {
        if (MEMORY_TYPE.equalsIgnoreCase(type)) {
            setContent();
            log.info("выбранный тип БД=memory. Заполнили содержимым базу данных");
        } else {
            log.info("выбранный тип БД={}.Нет смысла заполнять базу данных memory db содержимым", type);
        }
    }

    private void setContent() {
        Subject sub1 = new Subject().withTitle("География");
        Subject sub2 = new Subject().withTitle("Экономика");
        Subject sub3 = new Subject().withTitle("Информатика");
        Subject sub4 = new Subject().withTitle("Русский");
        Subject sub5 = new Subject().withTitle("Белорусский");
        Subject sub6 = new Subject().withTitle("Природоведение");

        subjectRepo.putSubjectIfNotExists(sub1);
        subjectRepo.putSubjectIfNotExists(sub2);
        subjectRepo.putSubjectIfNotExists(sub3);
        subjectRepo.putSubjectIfNotExists(sub4);
        subjectRepo.putSubjectIfNotExists(sub5);
        subjectRepo.putSubjectIfNotExists(sub6);

        adminRepo.putAdminIfNotExists(new Admin()
                .withId(323)
                .withCredential(new Credential()
                        .withId(0)
                        .withLogin("ADMIN")
                        .withPassword("234"))
                .withFirstname("Никита")
                .withLastname("Сологуб")
                .withPatronymic("Олегович")
                .withDateOfBirth(LocalDate.of(1992, Month.APRIL, 23)));

        Student stud1 = new Student()
                .withCredential(new Credential()
                        .withLogin("AXEL23")
                        .withPassword("123"))
                .withLastname("Ярец")
                .withFirstname("Илья")
                .withPatronymic("Викторович")
                .withDateOfBirth(LocalDate.of(1995, Month.JULY, 22));

        Student stud2 = new Student()
                .withCredential(new Credential()
                        .withLogin("STOLYAR55")
                        .withPassword("st678"))
                .withLastname("Столярчук")
                .withFirstname("Анастасия")
                .withPatronymic("Ивановна")
                .withDateOfBirth(LocalDate.of(1995, Month.AUGUST, 17));

        Student stud3 = new Student()
                .withCredential(new Credential()
                        .withLogin("BabkaVKedah")
                        .withPassword("worldoftanks"))
                .withLastname("Татур")
                .withFirstname("Егор")
                .withPatronymic("Евгеньевич")
                .withDateOfBirth(LocalDate.of(1995, Month.NOVEMBER, 27));

        Student stud4 = new Student()
                .withCredential(new Credential()
                        .withLogin("Smartdyika")
                        .withPassword("books34"))
                .withLastname("Полошавец")
                .withFirstname("Ксения")
                .withPatronymic("Антоновна")
                .withDateOfBirth(LocalDate.of(1995, Month.APRIL, 10));

        Student stud5 = new Student()
                .withCredential(new Credential()
                        .withLogin("azazello666")
                        .withPassword("tech1"))
                .withLastname("Анпилов")
                .withFirstname("Андрей")
                .withPatronymic("Сергеевич")
                .withDateOfBirth(LocalDate.of(1991, Month.DECEMBER, 11));

        Student stud6 = new Student()
                .withCredential(new Credential()
                        .withLogin("Shabalina_Anzhela")
                        .withPassword("flower4"))
                .withLastname("Шабалина")
                .withFirstname("Анжелина")
                .withPatronymic("Игоревна")
                .withDateOfBirth(LocalDate.of(1995, Month.MARCH, 28));

        studentRepo.putStudentIfNotExists(stud1);
        studentRepo.putStudentIfNotExists(stud2);
        studentRepo.putStudentIfNotExists(stud3);
        studentRepo.putStudentIfNotExists(stud4);
        studentRepo.putStudentIfNotExists(stud5);
        studentRepo.putStudentIfNotExists(stud6);

        Teacher t1 = new Teacher().
                withCredential(new Credential()
                        .withLogin("DETSUK59")
                        .withPassword("ekology"))
                .withLastname("Дэцук")
                .withFirstname("Валерия")
                .withPatronymic("Сергеевна")
                .withDateOfBirth(LocalDate.of(1974, Month.SEPTEMBER, 21));

        Teacher t2 = new Teacher().
                withCredential(new Credential()
                        .withLogin("VIK_k")
                        .withPassword("teach12"))
                .withLastname("Грузинова")
                .withFirstname("Валерия")
                .withPatronymic("Леонидовна")
                .withDateOfBirth(LocalDate.of(1959, Month.OCTOBER, 12));

        Teacher t3 = new Teacher().
                withCredential(new Credential()
                        .withLogin("arti")
                        .withPassword("soprofan"))
                .withLastname("Путято")
                .withFirstname("Артур")
                .withPatronymic("Владимирович")
                .withDateOfBirth(LocalDate.of(1976, Month.FEBRUARY, 7));

        Teacher t4 = new Teacher().
                withCredential(new Credential()
                        .withLogin("Tvorog57")
                        .withPassword("great123"))
                .withLastname("Творогов")
                .withFirstname("Сергей")
                .withPatronymic("Петрович")
                .withDateOfBirth(LocalDate.of(1957, Month.DECEMBER, 11));

        Teacher t5 = new Teacher().
                withCredential(new Credential()
                        .withLogin("Med1")
                        .withPassword("123"))
                .withLastname("Мединский")
                .withFirstname("Ян")
                .withPatronymic("Станиславович")
                .withDateOfBirth(LocalDate.of(1971, Month.NOVEMBER, 21));

        teacherRepo.putTeacherIfNotExists(t1);
        teacherRepo.putTeacherIfNotExists(t2);
        teacherRepo.putTeacherIfNotExists(t3);
        teacherRepo.putTeacherIfNotExists(t4);
        teacherRepo.putTeacherIfNotExists(t5);

        Salary s1 = new Salary()
                .withDate(LocalDate.parse("2021-01-12"))
                .withCoins(64140);
        Salary s2 = new Salary()
                .withDate(LocalDate.parse("2021-02-11"))
                .withCoins(63716);
        Salary s3 = new Salary()
                .withDate(LocalDate.parse("2021-03-13"))
                .withCoins(61898);

        Salary s4 = new Salary()
                .withDate(LocalDate.parse("2021-04-12"))
                .withCoins(64140);
        Salary s5 = new Salary()
                .withDate(LocalDate.parse("2021-05-11"))
                .withCoins(63716);
        Salary s6 = new Salary()
                .withDate(LocalDate.parse("2021-06-21"))
                .withCoins(61898);

        Salary s7 = new Salary()
                .withDate(LocalDate.parse("2021-07-16"))
                .withCoins(64140);
        Salary s8 = new Salary()
                .withDate(LocalDate.parse("2021-08-14"))
                .withCoins(63716);
        Salary s9 = new Salary()
                .withDate(LocalDate.parse("2021-09-11"))
                .withCoins(61898);

        Salary s10 = new Salary()
                .withDate(LocalDate.parse("2021-10-14"))
                .withCoins(64140);
        Salary s11 = new Salary()
                .withDate(LocalDate.parse("2021-11-16"))
                .withCoins(63716);
        Salary s12 = new Salary()
                .withDate(LocalDate.parse("2021-12-15"))
                .withCoins(61898);

        Salary s13 = new Salary()
                .withDate(LocalDate.parse("2022-01-17"))
                .withCoins(65085);

        salaryRepo.putSalaryToTeacher(s1, t1.getId());
        salaryRepo.putSalaryToTeacher(s2, t1.getId());
        salaryRepo.putSalaryToTeacher(s3, t1.getId());
        salaryRepo.putSalaryToTeacher(s4, t2.getId());
        salaryRepo.putSalaryToTeacher(s5, t2.getId());
        salaryRepo.putSalaryToTeacher(s6, t2.getId());
        salaryRepo.putSalaryToTeacher(s7, t3.getId());
        salaryRepo.putSalaryToTeacher(s8, t3.getId());
        salaryRepo.putSalaryToTeacher(s9, t3.getId());
        salaryRepo.putSalaryToTeacher(s10, t4.getId());
        salaryRepo.putSalaryToTeacher(s11, t4.getId());
        salaryRepo.putSalaryToTeacher(s12, t4.getId());
        salaryRepo.putSalaryToTeacher(s13, t5.getId());

        Mark m1 = new Mark()
                .withSubject(sub1)
                .withPoint(90)
                .withDate(LocalDate.parse("2021-09-11"));
        Mark m2 = new Mark()
                .withSubject(sub2)
                .withPoint(55)
                .withDate(LocalDate.parse("2021-10-21"));
        Mark m3 = new Mark()
                .withSubject(sub3)
                .withPoint(80)
                .withDate(LocalDate.parse("2021-11-04"));
        Mark m4 = new Mark()
                .withSubject(sub4)
                .withPoint(98)
                .withDate(LocalDate.parse("2021-12-07"));
        Mark m5 = new Mark()
                .withSubject(sub5)
                .withPoint(12)
                .withDate(LocalDate.parse("2021-05-16"));
        Mark m6 = new Mark()
                .withSubject(sub6)
                .withPoint(43)
                .withDate(LocalDate.parse("2021-06-18"));
        Mark m7 = new Mark()
                .withSubject(sub1)
                .withPoint(56)
                .withDate(LocalDate.parse("2021-07-12"));
        Mark m8 = new Mark()
                .withSubject(sub2)
                .withPoint(57)
                .withDate(LocalDate.parse("2021-04-14"));
        Mark m9 = new Mark()
                .withSubject(sub3)
                .withPoint(14)
                .withDate(LocalDate.parse("2021-03-13"));
        Mark m10 = new Mark()
                .withSubject(sub4)
                .withPoint(51)
                .withDate(LocalDate.parse("2021-06-18"));
        Mark m11 = new Mark()
                .withSubject(sub5)
                .withPoint(76)
                .withDate(LocalDate.parse("2021-08-28"));
        Mark m12 = new Mark()
                .withSubject(sub6)
                .withPoint(22)
                .withDate(LocalDate.parse("2021-02-26"));

        markRepo.putMarkToStudent(m1, stud1.getId());
        markRepo.putMarkToStudent(m2, stud1.getId());
        markRepo.putMarkToStudent(m3, stud2.getId());
        markRepo.putMarkToStudent(m4, stud2.getId());
        markRepo.putMarkToStudent(m5, stud3.getId());
        markRepo.putMarkToStudent(m6, stud3.getId());
        markRepo.putMarkToStudent(m7, stud4.getId());
        markRepo.putMarkToStudent(m8, stud4.getId());
        markRepo.putMarkToStudent(m9, stud5.getId());
        markRepo.putMarkToStudent(m10, stud5.getId());
        markRepo.putMarkToStudent(m11, stud6.getId());
        markRepo.putMarkToStudent(m12, stud6.getId());

        Group gr1 = new Group()
                .withTitle("Смешарики")
                .withTeacher(t1)
                .withDescription("Группа для весёлого изучения любых дисциплин")
                .withStudents(stud1, stud2, stud3, stud4, stud5, stud6)
                .withSubjects(sub5, sub4);

        Group gr2 = new Group()
                .withTitle("Болванчики")
                .withTeacher(t2)
                .withDescription("Группа для хулиганов (из СПТУ)")
                .withStudents(stud4, stud5)
                .withSubjects(sub5, sub4);

        Group gr3 = new Group()
                .withTitle("Умники")
                .withTeacher(t3)
                .withDescription("Группа для самых умных")
                .withStudents(stud1, stud2, stud3, stud6)
                .withSubjects(sub5, sub4);

        Group gr4 = new Group()
                .withTitle("Тяп Ляп");

        groupRepo.putGroupIfNotExists(gr1);
        groupRepo.putGroupIfNotExists(gr2);
        groupRepo.putGroupIfNotExists(gr3);
        groupRepo.putGroupIfNotExists(gr4);
    }
}