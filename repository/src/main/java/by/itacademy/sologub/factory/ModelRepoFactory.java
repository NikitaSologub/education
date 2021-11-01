package by.itacademy.sologub.factory;

import by.itacademy.sologub.CredentialRepo;
import by.itacademy.sologub.SalaryRepo;
import by.itacademy.sologub.StudentRepo;
import by.itacademy.sologub.TeacherRepo;

public interface ModelRepoFactory {
    CredentialRepo getCredentialRepo();

    TeacherRepo getTeacherRepo();

    StudentRepo getStudentRepo();

    SalaryRepo getSalariesRepo();
}