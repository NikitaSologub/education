package by.itacademy.sologub.constants;

public final class SqlQuery {
    //credential postgres sql
    public static final String GET_CREDENTIAL_BY_LOGIN = "SELECT id, login, password FROM credential WHERE login=?";
    public static final String SET_CREDENTIAL_IF_NOT_EXISTS =
            "INSERT INTO credential(login, password) VALUES(?,?) ON CONFLICT (login) DO NOTHING;";
    public static final String UPDATE_CREDENTIAL = "UPDATE credential SET password=? WHERE login=?;";
    public static final String UPDATE_CREDENTIAL_BY_ID = "UPDATE credential SET login=?, password=? WHERE id=?;";
    public static final String DELETE_CREDENTIAL_BY_LOGIN = "DELETE FROM credential WHERE login=?;";
    public static final String DELETE_CREDENTIAL_BY_ID = "DELETE FROM credential WHERE id=?;";
    public static final String SET_CREDENTIAL_AND_GET_ID =
            "INSERT INTO credential(login, password) VALUES(?,?) ON CONFLICT (login) DO NOTHING RETURNING id;";

    //user postgres sql
    public static final String UPDATE_USER_BY_CREDENTIAL_ID = "UPDATE person SET firstname=?,lastname=?,patronymic=?," +
            "date_of_birth=? WHERE credential_id=? AND role_id=(SELECT id FROM \"role\" WHERE role.\"name\"=?);";
    public static final String INSERT_USER = "INSERT INTO  person (firstname, lastname, patronymic, date_of_birth," +
            " credential_id, role_id) VALUES (?,?,?,?,?, (SELECT id FROM \"role\" WHERE role.\"name\" = ?))\n" +
            "ON CONFLICT (credential_id) DO NOTHING;";
    public static final String GET_USER_BY_LOGIN = "SELECT p.id, p.firstname, p.lastname, p.patronymic," +
            " p.date_of_birth, p.credential_id, c.id, c.login, c.\"password\" FROM person p JOIN credential c " +
            "ON c.id = p.credential_id JOIN role r ON r.id = p.role_id WHERE c.login=? and r.\"name\"=?;";
    public static final String GET_USER_BY_CREDENTIAL_ID = "SELECT p.id, p.firstname, p.lastname, p.patronymic," +
            " p.date_of_birth, p.credential_id, c.id, c.login, c.\"password\" FROM person p JOIN credential c " +
            "ON c.id = p.credential_id JOIN role r ON r.id = p.role_id WHERE c.id=? and r.\"name\"=?;";
    public static final String GET_USER_BY_ID = "SELECT p.id, p.firstname, p.lastname, p.patronymic," +
            " p.date_of_birth, p.credential_id, c.id, c.login, c.\"password\" FROM person p JOIN credential c " +
            "ON c.id = p.credential_id JOIN role r ON r.id = p.role_id WHERE p.id=? and r.\"name\"=?;";
    public static final String GET_USERS_LIST = "SELECT p.id, p.firstname, p.lastname, p.patronymic, p.date_of_birth," +
            "p.credential_id, c.login, c.\"password\", r.\"name\" FROM person p JOIN credential c ON c.id = p.credential_id \n" +
            "JOIN role r ON r.id = p.role_id WHERE r.\"name\" = ?;";
    public static final String DELETE_USER_BY_CREDENTIAL_ID =
            "DELETE FROM person p USING role r WHERE p.role_id = r.id and r.\"name\"=? and p.credential_id=?;";

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

    //subject postgres sql
    public static final String DELETE_SUBJECT_BY_ID = "DELETE FROM subject WHERE id=?;";
    public static final String GET_SUBJECT_BY_ID = "SELECT id, title FROM subject WHERE id=?;";
    public static final String UPDATE_SUBJECT_BY_ID = "UPDATE subject SET title=? WHERE id=?;";
    public static final String SET_SUBJECT_BY_ID = "INSERT INTO subject (title) VALUES(?);";
    public static final String GET_SUBJECTS_LIST = "SELECT id, title FROM subject;";
}