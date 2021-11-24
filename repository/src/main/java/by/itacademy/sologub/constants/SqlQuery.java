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
            "date_of_birth=? WHERE credential_id=? AND role=?;";
    public static final String INSERT_USER = "INSERT INTO  person (firstname, lastname, patronymic, date_of_birth," +
            " credential_id, role) VALUES (?,?,?,?,?,?) ON CONFLICT (credential_id) DO NOTHING;";
    public static final String GET_USER_BY_LOGIN = "SELECT p.id, p.firstname, p.lastname, p.patronymic," +
            " p.date_of_birth, p.credential_id, c.id, c.login, c.\"password\" FROM person p JOIN credential c " +
            "ON c.id = p.credential_id WHERE c.login=? and p.role=?;";
    public static final String GET_USER_BY_CREDENTIAL_ID = "SELECT p.id, p.firstname, p.lastname, p.patronymic," +
            " p.date_of_birth, p.credential_id, c.id, c.login, c.\"password\" FROM person p JOIN credential c " +
            "ON c.id = p.credential_id WHERE c.id=? and p.role=?;";
    public static final String GET_USER_BY_ID = "SELECT p.id, p.firstname, p.lastname, p.patronymic," +
            " p.date_of_birth, p.credential_id, c.id, c.login, c.\"password\" FROM person p JOIN credential c " +
            "ON c.id = p.credential_id WHERE p.id=? and p.role=?;";
    public static final String GET_USERS_LIST = "SELECT p.id, p.firstname, p.lastname, p.patronymic, p.date_of_birth," +
            "p.credential_id, c.login, c.\"password\", p.role FROM person p JOIN credential c ON c.id = p.credential_id " +
            "WHERE p.role=?;";
    public static final String DELETE_USER_BY_CREDENTIAL_ID = "DELETE FROM person p WHERE p.role=? AND p.credential_id=?;";

    //student postgres sql
    public static final String GET_STUDENT_SET_BY_GROUP_ID = "select p.id, p.firstname, p.lastname, p.patronymic, " +
            "p.date_of_birth, p.credential_id, c.login, c.password FROM person p JOIN credential c ON p.credential_id=c.id" +
            " JOIN group_student gs ON p.id=gs.student_id WHERE gs.group_id=?";

    //teacher postgres sql
    public static final String EXCLUDE_TEACHER_FROM_ALL_GROUPS_BY_TEACHER_ID =
            "UPDATE public.group SET teacher_id=NULL WHERE teacher_id=?;";

    //salary postgres sql
    public static final String GET_SALARIES_LIST_BY_TEACHER_ID = "SELECT id,coins_amount,date FROM salary WHERE teacher_id=?;";
    public static final String GET_SALARIES_LIST_AFTER_DATE_BY_TEACHER_ID =
            "SELECT id, coins_amount, date FROM salary WHERE teacher_id=? AND date>?;";
    public static final String GET_SALARY_BY_ID = "SELECT id, coins_amount, date FROM salary WHERE id=?;";
    public static final String SET_SALARY_BY_TEACHER_ID_RETURNING_ID =
            "INSERT INTO salary (coins_amount,date,teacher_id) VALUES(?,?,?) WHERE teacher_id=? RETURNING id;";
    public static final String SET_SALARY_BY_TEACHER_ID = "INSERT INTO salary (coins_amount,date,teacher_id) VALUES(?,?,?);";
    public static final String DELETE_SALARY_BY_ID = "DELETE FROM salary WHERE id=?;";
    public static final String DELETE_ALL_SALARIES_BY_TEACHER_ID = "DELETE FROM salary WHERE teacher_id=?;";
    public static final String UPDATE_SALARY_BY_ID = "UPDATE salary SET coins_amount=?,date=? WHERE id=?;";

    //subject postgres sql
    public static final String DELETE_SUBJECT_BY_ID = "DELETE FROM subject WHERE id=?;";
    public static final String GET_SUBJECT_BY_ID = "SELECT id, title FROM subject WHERE id=?;";
    public static final String UPDATE_SUBJECT_BY_ID = "UPDATE subject SET title=? WHERE id=?;";
    public static final String SET_SUBJECT = "INSERT INTO subject (title) VALUES(?);";
    public static final String GET_SUBJECTS_LIST = "SELECT id, title FROM subject;";
    public static final String GET_SUBJECTS_LIST_BY_GROUP_ID =
            "select s.id, s.title from subject s join group_subject gs on gs.subject_id=s.id where gs.group_id=?;";
    public static final String EXCLUDE_SUBJECT_FROM_ALL_GROUPS_BY_SUBJECT_ID = "DELETE FROM group_subject WHERE subject_id=?;";

    //groups postgres sql
    public static final String GET_GROUPS_LIST_WITH_TEACHERS = "SELECT g.id, g.title, g.teacher_id, g.description, p.id," +
            " p.firstname, p.lastname, p.patronymic, p.date_of_birth, p.credential_id, c.login, c.password " +
            "FROM public.group g left outer JOIN person p ON g.teacher_id=p.id left outer JOIN credential c ON " +
            "p.credential_id=c.id WHERE p.role='TEACHER' or p.role is null and teacher_id IS null or teacher_id is not null;";
    public static final String GET_GROUPS_LIST_BY_TEACHER_ID = "SELECT g.id, g.title, g.teacher_id, g.description, p.id," +
            " p.firstname, p.lastname, p.patronymic, p.date_of_birth, p.credential_id, c.login, c.password FROM public.group g " +
            "JOIN person p ON g.teacher_id=p.id JOIN credential c ON p.credential_id=c.id WHERE p.role='TEACHER' and g.teacher_id=?;";
    public static final String GET_GROUPS_LIST_BY_STUDENT_ID = "select g.id, g.title, g.teacher_id, g.description, p.id," +
            " p.firstname, p.lastname, p.patronymic, p.date_of_birth, p.credential_id, c.login, c.password FROM public.group g" +
            " left outer JOIN person p ON g.teacher_id=p.id left outer JOIN credential c ON p.credential_id=c.id JOIN " +
            "group_student gs ON g.id=gs.group_id WHERE gs.student_id=? and (g.teacher_id is not null or g.teacher_id is null);";
    public static final String GET_GROUP_BY_ID = "SELECT g.id, g.title, g.teacher_id, g.description, p.firstname, p.lastname," +
            " p.patronymic, p.date_of_birth, p.credential_id, c.login, c.password FROM public.group g left outer JOIN " +
            "person p ON g.teacher_id=p.id left outer JOIN credential c ON c.id=p.credential_id where g.id=? and " +
            "(g.teacher_id is not null or g.teacher_id is null);";
    public static final String SET_GROUP_RETURNING =
            "INSERT INTO public.group(title, description) VALUES(?,?) ON CONFLICT (title) DO NOTHING RETURNING id";
    public static final String DELETE_GROUP_BY_ID = "DELETE FROM public.group WHERE id=?;";
    public static final String CHANGE_GROUP_BY_ID = "UPDATE public.group SET title=?,teacher_id=?,description=? WHERE id=?;";
    public static final String EXCLUDE_ALL_STUDENTS_FROM_GROUP_BY_ID = "DELETE FROM group_student WHERE group_id=?;";
    public static final String EXCLUDE_ALL_SUBJECTS_FROM_GROUP_BY_ID = "DELETE FROM group_subject WHERE group_id=?;";
    public static final String INCLUDE_SUBJECT_IN_GROUP_BY_IDS = "INSERT INTO group_subject (group_id,subject_id) VALUES(?,?);";
    public static final String EXCLUDE_SUBJECT_FROM_GROUP_BY_IDS = "DELETE FROM group_subject WHERE group_id=? AND subject_id=?;";
    public static final String INCLUDE_STUDENT_IN_GROUP_BY_IDS = "INSERT INTO group_student (group_id,student_id) VALUES(?,?);";
    public static final String EXCLUDE_STUDENT_FROM_GROUP_BY_IDS = "DELETE FROM group_student WHERE group_id=? AND student_id=?;";
}