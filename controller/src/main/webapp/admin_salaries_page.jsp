<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Salaries page</title>
</head>
<body>
<h1>SALARIES PAGE</h1>
LOGIN:   ${requestScope.teacher.credential.login}<br>
${requestScope.teacher.lastname} | ${requestScope.teacher.firstname} | ${requestScope.teacher.patronymic} | ${requestScope.teacher.dateOfBirth}
<br>
Список всех зарплат<br>
<c:set var="totalSalaryCoins" value="${0}"/>
<c:set var="salaryCount" value="${0}"/>
<c:forEach var="salary" items="${requestScope.teacher.salaries}">
    <table>

        <tr>${salary.id} индекс | ${salary.coins/100} сумма (рублей) | ${salary.date} дата начисления</tr>
        <c:set var="totalSalaryCoins" value="${totalSalaryCoins + salary.coins}"/>
        <c:set var="salaryCount" value="${salaryCount + 1}"/>
    </table>
</c:forEach>

<br>
Сумма заработанных денег ${totalSalaryCoins/100} (рублей)
<br>
Сумма средней заработной платы ${totalSalaryCoins / salaryCount /100} (рублей)
<h5>
    <a href="${pageContext.request.contextPath}/admin_teachers_page.jsp">Go back to teachers page.</a>
</h5>
</body>
</html>
