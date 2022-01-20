package by.itacademy.sologub.postgres;

import by.itacademy.sologub.Salary;
import by.itacademy.sologub.SalaryRepo;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.COINS_AMOUNT_DB_FIELD;
import static by.itacademy.sologub.constants.Attributes.DATE;
import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.SALARY;
import static by.itacademy.sologub.constants.ConstantObject.SALARY_NOT_EXISTS;
import static by.itacademy.sologub.postgres.queries.SqlQuery.DELETE_SALARY_BY_ID;
import static by.itacademy.sologub.postgres.queries.SqlQuery.GET_SALARIES_LIST_AFTER_DATE_BY_TEACHER_ID;
import static by.itacademy.sologub.postgres.queries.SqlQuery.GET_SALARIES_LIST_BY_TEACHER_ID;
import static by.itacademy.sologub.postgres.queries.SqlQuery.GET_SALARY_BY_ID;
import static by.itacademy.sologub.postgres.queries.SqlQuery.SET_SALARY_BY_TEACHER_ID;
import static by.itacademy.sologub.postgres.queries.SqlQuery.UPDATE_SALARY_BY_ID;

@Slf4j
@Repository
public class SalaryRepoPostgresImpl extends AbstractPostgresRepo<Salary> implements SalaryRepo {
    @Autowired
    public SalaryRepoPostgresImpl(ComboPooledDataSource pool) {
        super(pool);
    }

    @Override
    protected Salary extractObject(ResultSet rs) throws SQLException {
        return new Salary()
                .withId(rs.getInt(ID))
                .withCoins(rs.getInt(COINS_AMOUNT_DB_FIELD))
                .withDate(rs.getDate(DATE).toLocalDate());
    }

    @Override
    public Set<Salary> getAllSalariesByTeacherId(int teacherId) {
        Set<Salary> salaries = new HashSet<>();
        ResultSet rs = null;
        try (Connection con = pool.getConnection();
             PreparedStatement st = con.prepareStatement(GET_SALARIES_LIST_BY_TEACHER_ID)) {
            st.setInt(1, teacherId);

            rs = st.executeQuery();
            salaries = extractSalaries(rs);
            log.info("Извлекли все Salaries из БД по teacher_id= {}", teacherId);
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию извлечения зарплат по id учителя", e);
        } finally {
            closeResource(rs);
        }
        return salaries;
    }

    @Override
    public Set<Salary> getAllSalariesByTeacherIdAfterDate(int teacherId, LocalDate date) {
        Set<Salary> salaries = new HashSet<>();
        ResultSet rs = null;
        try (Connection con = pool.getConnection();
             PreparedStatement st = con.prepareStatement(GET_SALARIES_LIST_AFTER_DATE_BY_TEACHER_ID)) {
            st.setInt(1, teacherId);
            st.setDate(2, Date.valueOf(date));

            rs = st.executeQuery();

            salaries = extractSalaries(rs);
            log.info("Извлекли все Salaries из БД по teacher_id= {} где date < {}", teacherId, date);
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию извлечения зарплат по id учителя", e);
        } finally {
            closeResource(rs);
        }
        return salaries;
    }

    private Set<Salary> extractSalaries(ResultSet rs) throws SQLException {
        Set<Salary> salaries = new HashSet<>();
        while (rs.next()) {
            salaries.add(extractObject(rs));
        }
        return salaries;
    }

    @Override
    public Salary getSalary(int id) {
        return get(id, GET_SALARY_BY_ID, SALARY, SALARY_NOT_EXISTS);
    }

    @Override
    public boolean putSalaryToTeacher(Salary salary, int teacherId) {
        try (Connection con = pool.getConnection(); PreparedStatement st = con.prepareStatement(SET_SALARY_BY_TEACHER_ID)) {
            st.setInt(1, salary.getCoins());
            st.setDate(2, Date.valueOf(salary.getDate()));
            st.setInt(3, teacherId);

            if (st.executeUpdate() > 0) {
                log.info("Добавили Salary в БД по teacher_id={}", teacherId);
                return true;
            } else {
                log.info("Не удалось добавить Salary в БД по teacher_id={}, такого id нет в базе", teacherId);
            }
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию добавления зарплаты по id учителя", e);
        }
        return false;
    }

    @Override
    public boolean changeSalary(Salary newS) {
        try (Connection con = pool.getConnection(); PreparedStatement st = con.prepareStatement(UPDATE_SALARY_BY_ID)) {
            st.setInt(1, newS.getCoins());
            st.setDate(2, Date.valueOf(newS.getDate()));
            st.setInt(3, newS.getId());

            if (st.executeUpdate() > 0) {
                log.info("Обновили Salary в БД по id={}", newS.getId());
                return true;
            } else {
                log.info("Не удалось обновить Salary в БД по id={}, такого id нет в базе", newS.getId());
            }
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию обновления зарплаты по id", e);
        }
        return false;
    }

    @Override
    public boolean deleteSalary(int id) {
        return delete(id, DELETE_SALARY_BY_ID, SALARY);
    }
}