package by.itacademy.sologub.constants;

public final class Constant {
    //controllers urls
    public static final String ALL_URL = "/*";
    public static final String LOGIN_CONTROLLER = "/login";
    public static final String LOGOUT_CONTROLLER = "/logout";
    public static final String TEACHER_CONTROLLER = "/teachers/*";
    public static final String STUDENT_CONTROLLER = "/students/*";
    public static final String SALARY_CONTROLLER = "/salaries/*";
    public static final String GROUP_CONTROLLER = "/groups/*";
    public static final String SUBJECT_CONTROLLER = "/subjects/*";
    public static final String MARK_CONTROLLER = "/marks/*";

    //jsp urls
    public static final String LOGIN_PAGE = "/view/login_page.jsp";
    public static final String REGISTRATION_PAGE = "/view/registration_page.jsp";
    public static final String ADMIN_FRONT_PAGE = "/view/admin_front_page.jsp";
    public static final String ADMIN_TEACHERS_PAGE = "/view/admin_teachers_page.jsp";
    public static final String ADMIN_STUDENTS_PAGE = "/view/admin_students_page.jsp";
    public static final String ADMIN_SALARIES_PAGE = "/view/admin_salaries_page.jsp";
    public static final String ADMIN_GROUPS_PAGE = "/view/admin_groups_page.jsp";
    public static final String ADMIN_GROUP_EDIT_PAGE = "/view/admin_group_edit_page.jsp";
    public static final String ADMIN_GROUP_STUDENTS_PAGE = "/view/admin_group_students_page.jsp";
    public static final String ADMIN_GROUP_SUBJECTS_PAGE = "/view/admin_group_subjects_page.jsp";
    public static final String ADMIN_SUBJECTS_PAGE = "/view/admin_subjects_page.jsp";
    public static final String ADMIN_STUDENTS_MARKS_PAGE = "/view/admin_students_marks_page.jsp";
    public static final String STUDENT_FRONT_PAGE = "/view/student_front_page.jsp";
    public static final String TEACHER_FRONT_PAGE = "/view/teacher_front_page.jsp";

    //ModelAndView short names
    public static final String LOGIN_VIEW = "login_page";
    public static final String REGISTRATION_VIEW = "registration_page";
    public static final String ADMIN_FRONT_VIEW = "admin_front_page";
    public static final String ADMIN_TEACHERS_VIEW = "admin_teachers_page";
    public static final String ADMIN_STUDENTS_VIEW = "admin_students_page";
    public static final String ADMIN_SALARIES_VIEW = "admin_salaries_page";
    public static final String ADMIN_GROUPS_VIEW = "admin_groups_page";
    public static final String ADMIN_GROUP_EDIT_VIEW = "admin_group_edit_page";
    public static final String ADMIN_GROUP_STUDENTS_VIEW = "admin_group_students_page";
    public static final String ADMIN_GROUP_SUBJECTS_VIEW = "admin_group_subjects_page";
    public static final String ADMIN_SUBJECTS_VIEW = "admin_subjects_page";
    public static final String ADMIN_STUDENTS_MARKS_VIEW = "admin_students_marks_page";
    public static final String STUDENT_FRONT_VIEW = "student_front_page";
    public static final String TEACHER_FRONT_VIEW = "teacher_front_page";

    //attributes
    public static final String SESSION_ENTITY = "sessionEntity";
    public static final String HIDDEN_METHOD_PARAMETER = "hidden_method";
    public static final String MESSAGE = "message";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String REQUEST_ENCODING = "requestEncoding";
    public static final String UTF_8 = "UTF-8";
    public static final String CONTENT_TYPE = "text/html;charset=UTF-8";
    public static final String MEMORY_TYPE = "memory";

    //bean attributes
    public static final String OBJECTS_SET = "objectsSet";
    public static final String SUBJECTS_SET = "subjectsSet";
    public static final String PERSONS_SET = "personsSet";
    public static final String CURRENT_GROUP_OBJECTS_SET = "currentGroupObjectsSet";
    public static final String GROUP_LIST = "groupList";
    public static final String AVERAGE_SALARY = "average";

    //request parameters
    public static final String GROUP_ID = "groupId";
    public static final String SUBJECT_ID = "subjectId";
    public static final String TEACHER_LOGIN = "teacherLogin";
    public static final String TEACHER_ID = "teacherId";
    public static final String STUDENT_ID = "studentId";
    public static final String MARK_ID = "markId";
    public static final String SALARY_ID = "salaryId";
    public static final String STUDENT_LOGIN = "studentLogin";
    public static final String CREDENTIAL_ID = "credentialId";
    public static final String DATE_OF_BIRTH = "dateOfBirth";
}