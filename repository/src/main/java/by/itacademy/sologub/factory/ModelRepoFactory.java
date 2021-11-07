package by.itacademy.sologub.factory;

import by.itacademy.sologub.AdminRepo;
import by.itacademy.sologub.CredentialRepo;
import by.itacademy.sologub.SalaryRepo;
import by.itacademy.sologub.StudentRepo;
import by.itacademy.sologub.SubjectRepo;
import by.itacademy.sologub.TeacherRepo;

public interface ModelRepoFactory {
    CredentialRepo getCredentialRepo();

    TeacherRepo getTeacherRepo();

    StudentRepo getStudentRepo();

    AdminRepo getAdminRepo();

    SalaryRepo getSalariesRepo();

    SubjectRepo getSubjectRepo();
}