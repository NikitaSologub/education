package by.itacademy.sologub.constants;

public final class SqlQuery {
    //credential postgres sql
    public static final String GET_CREDENTIAL_BY_LOGIN =
            "SELECT id, login, password FROM credential c WHERE c.login=?";
    public static final String SET_CREDENTIAL_IF_NOT_EXISTS =
            "INSERT INTO credential(login, password) VALUES(?,?) ON CONFLICT (login) DO NOTHING;";
    public static final String UPDATE_CREDENTIAL = "UPDATE credential SET password=? WHERE login=?;";
    public static final String UPDATE_CREDENTIAL_BY_ID = "UPDATE credential SET login=?, password=? WHERE id=?;";
    public static final String DELETE_CREDENTIAL_BY_LOGIN = "DELETE FROM credential WHERE login=?;";
    public static final String DELETE_CREDENTIAL_BY_ID = "DELETE FROM credential WHERE id=?;";
    public static final String SET_CREDENTIAL_AND_GET_ID =
            "INSERT INTO credential(login, password) VALUES(?,?) ON CONFLICT (login) DO NOTHING RETURNING id;";

    //teacher postgres sql
    public static final String GET_TEACHERS_LIST = "SELECT t.id, t.firstname, t.lastname, t.patronymic, t.date_of_birth," +
            " t.credential_id, c.id, c.login, c.password FROM teacher t JOIN credential c ON c.id = t.credential_id;";
    public static final String GET_TEACHER_BY_LOGIN = "SELECT t.id, t.firstname, t.lastname, t.patronymic," +
            " t.date_of_birth, t.credential_id, c.id, c.login, c.password FROM teacher t JOIN credential c " +
            "ON c.id = t.credential_id WHERE c.login = ?;";
    public static final String INSERT_TEACHER =
            "INSERT INTO teacher (firstname, lastname, patronymic, date_of_birth, credential_id) VALUES (?,?,?,?,?)";
    public static final String DELETE_TEACHER_CASCADE_BY_CREDENTIAL_ID = "DELETE FROM teacher WHERE credential_id=?";
    public static final String DELETE_TEACHER_BY_CREDENTIAL_ID = "DELETE FROM teacher WHERE credential_id=?";
    public static final String UPDATE_TEACHER_BY_CREDENTIAL_ID =
            "UPDATE teacher SET firstname=?, lastname=?, patronymic=?, date_of_birth=? WHERE credential_id=?;";

    //student postgres sql
    public static final String GET_STUDENTS_LIST = "SELECT s.id, s.firstname, s.lastname, s.patronymic, s.date_of_birth," +
            " s.credential_id, c.id, c.login, c.password FROM student S JOIN credential c ON c.id = s.credential_id;";
    public static final String INSERT_STUDENT =
            "INSERT INTO student (firstname, lastname, patronymic, date_of_birth, credential_id) VALUES (?,?,?,?,?)";
    public static final String GET_STUDENT_BY_LOGIN = "SELECT s.id, s.firstname, s.lastname, s.patronymic," +
            " s.date_of_birth, s.credential_id, c.id, c.login, c.password FROM student s JOIN credential c " +
            "ON c.id = s.credential_id WHERE c.login =?;";
    public static final String DELETE_STUDENT_BY_CREDENTIAL_ID = "DELETE FROM student WHERE credential_id=?";
    public static final String UPDATE_STUDENT_BY_CREDENTIAL_ID =
            "UPDATE student SET firstname=?, lastname=?, patronymic=?, date_of_birth=? WHERE credential_id=?;";

    //salary postgres sql
    public static final String GET_SALARIES_LIST_BY_TEACHER_ID = "SELECT id, coins_amount, date, teacher_id " +
            "FROM salary WHERE teacher_id=?;";
    public static final String GET_SALARIES_LIST_AFTER_DATE_BY_TEACHER_ID = "SELECT id, coins_amount, date, teacher_id" +
            " FROM salary WHERE teacher_id=? AND date>?;";
    public static final String GET_SALARY_BY_ID = "SELECT id, coins_amount, date, teacher_id FROM salary WHERE id=?;";
    public static final String SET_SALARY_BY_TEACHER_ID_RETURNING_ID =
            "INSERT INTO salary (coins_amount,date,teacher_id) VALUES(?,?,?) WHERE teacher_id=? RETURNING id;";
    public static final String SET_SALARY_BY_TEACHER_ID =
            "INSERT INTO salary (coins_amount,date,teacher_id) VALUES(?,?,?);";
    public static final String DELETE_SALARY_BY_ID = "DELETE FROM salary WHERE id=?;";
    public static final String UPDATE_SALARY_BY_ID = "UPDATE salary SET coins_amount=?, date=?, teacher_id=? WHERE id=?;";
}