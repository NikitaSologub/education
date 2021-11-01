package by.itacademy.sologub.filters;

import by.itacademy.sologub.*;
import by.itacademy.sologub.factory.ModelRepoFactory;
import by.itacademy.sologub.factory.ModelRepoFactoryHardcodeImpl;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;

import static by.itacademy.sologub.constants.Constant.*;
import static by.itacademy.sologub.constants.Constants.LOGIN;
import static by.itacademy.sologub.constants.Constants.PASSWORD;

@WebFilter
@Slf4j
public class InitContextFilter implements Filter {
    ResourceBundle resourceBundle = ResourceBundle.getBundle(DB_CONFIG_FILE);

    @SneakyThrows
    @Override        //Тут я буду инициализировать все что можно
    public void init(FilterConfig filterConfig) {
        String databaseType = resourceBundle.getString(TYPE);

        initRepositoryAndSetContext(databaseType, filterConfig);
    }

    private void initRepositoryAndSetContext(String databaseType, FilterConfig filterConfig) throws PropertyVetoException {
        log.info("Приложение должно загружать {} тип баз данных", databaseType);
        switch (databaseType) {
            case ("postgres"):
                loadDatabasePostgres(filterConfig);
                break;
            case ("memory"):
            default:
                loadDatabaseInMemory(filterConfig);
                break;
        }
    }

    private void loadDatabaseInMemory(FilterConfig filterConfig) {
        ModelRepoFactory factory = ModelRepoFactoryHardcodeImpl.getInstance();

        SalaryRepo salaryRepo = factory.getSalariesRepo();
        CredentialRepo credentialRepo = factory.getCredentialRepo();
        TeacherRepo teacherRepo = factory.getTeacherRepo();
        StudentRepo studentRepo = factory.getStudentRepo();

        ServletContext context = filterConfig.getServletContext();

        setStudents(studentRepo);
        setTeachersAndSalaries(teacherRepo, salaryRepo);

        context.setAttribute(SALARY_REPO, salaryRepo);
        context.setAttribute(CREDENTIAL_REPO, credentialRepo);
        context.setAttribute(TEACHER_REPO, teacherRepo);
        context.setAttribute(STUDENT_REPO, studentRepo);
    }

    private void loadDatabasePostgres(FilterConfig filterConfig) throws PropertyVetoException {
        if (loadDriverClass()) {
            ComboPooledDataSource pool = initAndGetPoolConnection();
            CredentialRepoPostgresImpl credentialRepo = CredentialRepoPostgresImpl.getInstance(pool);
            TeacherRepoPostgresImpl teacherRepo = TeacherRepoPostgresImpl.getInstance(pool, credentialRepo);
            StudentRepoPostgresImpl studentRepo = StudentRepoPostgresImpl.getInstance(pool, credentialRepo);
            SalaryRepoPostgresImpl salaryRepo = SalaryRepoPostgresImpl.getInstance(pool);

            ServletContext context = filterConfig.getServletContext();
            context.setAttribute(CREDENTIAL_REPO, credentialRepo);
            context.setAttribute(TEACHER_REPO, teacherRepo);
            context.setAttribute(STUDENT_REPO, studentRepo);
            context.setAttribute(SALARY_REPO, salaryRepo);
        } else {
            loadDatabaseInMemory(filterConfig);
            log.info("Не получилось подключиться к БД. Переходим на HardcoreMemoryImpl Database");
        }
    }

    boolean loadDriverClass() {
        try {
            Class.forName(resourceBundle.getString(DRIVER));
            log.info("Драйвер {} загружен", resourceBundle.getString(DRIVER));
            return true;
        } catch (ClassNotFoundException e) {
            log.error("не удалость подключить драйвер.", e);
            return false;
        }
    }

    private ComboPooledDataSource initAndGetPoolConnection() throws PropertyVetoException {
        ComboPooledDataSource pool = new ComboPooledDataSource();
        pool.setJdbcUrl(resourceBundle.getString(URL));
        pool.setUser(resourceBundle.getString(LOGIN));
        pool.setPassword(resourceBundle.getString(PASSWORD));

        // Optional Settings
        pool.setInitialPoolSize(1);
        pool.setMinPoolSize(1);
        pool.setAcquireIncrement(1);
        pool.setMaxPoolSize(5);
        pool.setMaxStatements(100);
        pool.setDriverClass(resourceBundle.getString(DRIVER));
        return pool;
    }

    void setTeachersAndSalaries(TeacherRepo teacherRepo, SalaryRepo salaryRepo) {
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
                .withTeacherId(t1.getId())
                .withDate(LocalDate.parse("2021-01-12"))
                .withCoins(64140);
        Salary s2 = new Salary()
                .withTeacherId(t1.getId())
                .withDate(LocalDate.parse("2021-02-11"))
                .withCoins(63716);
        Salary s3 = new Salary()
                .withTeacherId(t1.getId())
                .withDate(LocalDate.parse("2021-03-13"))
                .withCoins(61898);

        Salary s4 = new Salary()
                .withTeacherId(t2.getId())
                .withDate(LocalDate.parse("2021-04-12"))
                .withCoins(64140);
        Salary s5 = new Salary()
                .withTeacherId(t2.getId())
                .withDate(LocalDate.parse("2021-05-11"))
                .withCoins(63716);
        Salary s6 = new Salary()
                .withTeacherId(t2.getId())
                .withDate(LocalDate.parse("2021-06-21"))
                .withCoins(61898);

        Salary s7 = new Salary()
                .withTeacherId(t3.getId())
                .withDate(LocalDate.parse("2021-07-16"))
                .withCoins(64140);
        Salary s8 = new Salary()
                .withTeacherId(t3.getId())
                .withDate(LocalDate.parse("2021-08-14"))
                .withCoins(63716);
        Salary s9 = new Salary()
                .withTeacherId(t3.getId())
                .withDate(LocalDate.parse("2021-09-11"))
                .withCoins(61898);

        Salary s10 = new Salary()
                .withTeacherId(t4.getId())
                .withDate(LocalDate.parse("2021-10-14"))
                .withCoins(64140);
        Salary s11 = new Salary()
                .withTeacherId(t4.getId())
                .withDate(LocalDate.parse("2021-11-16"))
                .withCoins(63716);
        Salary s12 = new Salary()
                .withTeacherId(t4.getId())
                .withDate(LocalDate.parse("2021-12-15"))
                .withCoins(61898);

        Salary s13 = new Salary()
                .withTeacherId(t5.getId())
                .withDate(LocalDate.parse("2022-01-17"))
                .withCoins(65085);

        salaryRepo.putSalary(s1);
        salaryRepo.putSalary(s2);
        salaryRepo.putSalary(s3);
        salaryRepo.putSalary(s4);
        salaryRepo.putSalary(s5);
        salaryRepo.putSalary(s6);
        salaryRepo.putSalary(s7);
        salaryRepo.putSalary(s8);
        salaryRepo.putSalary(s9);
        salaryRepo.putSalary(s10);
        salaryRepo.putSalary(s11);
        salaryRepo.putSalary(s12);
        salaryRepo.putSalary(s13);
    }

    void setStudents(StudentRepo repo) {
        Student s1 = new Student()
                .withCredential(new Credential()
                        .withLogin("AXEL23")
                        .withPassword("123"))
                .withLastname("Ярец")
                .withFirstname("Илья")
                .withPatronymic("Викторович")
                .withDateOfBirth(LocalDate.of(1995, Month.JULY, 22));

        Student s2 = new Student()
                .withCredential(new Credential()
                        .withLogin("STOLYAR55")
                        .withPassword("st678"))
                .withLastname("Столярчук")
                .withFirstname("Анастасия")
                .withPatronymic("Ивановна")
                .withDateOfBirth(LocalDate.of(1995, Month.AUGUST, 17));

        Student s3 = new Student()
                .withCredential(new Credential()
                        .withLogin("BabkaVKedah")
                        .withPassword("worldoftanks"))
                .withLastname("Татур")
                .withFirstname("Егор")
                .withPatronymic("Евгеньевич")
                .withDateOfBirth(LocalDate.of(1995, Month.NOVEMBER, 27));

        Student s4 = new Student()
                .withCredential(new Credential()
                        .withLogin("Smartdyika")
                        .withPassword("books34"))
                .withLastname("Полошавец")
                .withFirstname("Ксения")
                .withPatronymic("Антоновна")
                .withDateOfBirth(LocalDate.of(1995, Month.APRIL, 10));

        Student s5 = new Student()
                .withCredential(new Credential()
                        .withLogin("azazello666")
                        .withPassword("tech1"))
                .withLastname("Анпилов")
                .withFirstname("Андрей")
                .withPatronymic("Сергеевич")
                .withDateOfBirth(LocalDate.of(1991, Month.DECEMBER, 11));

        Student s6 = new Student()
                .withCredential(new Credential()
                        .withLogin("Shabalina_Anzhela")
                        .withPassword("flower4"))
                .withLastname("Шабалина")
                .withFirstname("Анжелина")
                .withPatronymic("Игоревна")
                .withDateOfBirth(LocalDate.of(1995, Month.MARCH, 28));

        repo.putStudentIfNotExists(s1);
        repo.putStudentIfNotExists(s2);
        repo.putStudentIfNotExists(s3);
        repo.putStudentIfNotExists(s4);
        repo.putStudentIfNotExists(s5);
        repo.putStudentIfNotExists(s6);
    }

    @Override
    public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain c) throws ServletException, IOException {
        c.doFilter(rq, rs);
    }
}