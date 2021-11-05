package by.itacademy.sologub.factory;

import by.itacademy.sologub.*;

public interface ModelRepoFactory {
    CredentialRepo getCredentialRepo();

    TeacherRepo getTeacherRepo();

    StudentRepo getStudentRepo();

    SalaryRepo getSalariesRepo();

    SubjectRepo getSubjectRepo();
}