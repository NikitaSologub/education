<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Salaries page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/admin_front_page.css'/>"/>
    <link rel="shortcut icon" href="img/book_favicon.png" type="image/png">
</head>
<body>
<h1>ADMIN SALARIES PAGE!</h1>
<h3 class="message">
    <c:if test="${not empty requestScope.message}">
        <c:out value="${requestScope.message}"/>
    </c:if>
</h3>
${requestScope.teacher.credential.login} | ${requestScope.teacher.lastname} | ${requestScope.teacher.firstname} |
${requestScope.teacher.patronymic} | ${requestScope.teacher.dateOfBirth}

<c:set var="totalSalaryCoins" value="${0}"/>
<c:set var="salaryCount" value="${0}"/>
<c:forEach var="salary" items="${requestScope.teacher.salaries}">
    <table>
        <c:set var="totalSalaryCoins" value="${totalSalaryCoins + salary.coins}"/>
        <c:set var="salaryCount" value="${salaryCount + 1}"/>
    </table>
</c:forEach>
<br> Сумма заработанных денег ${totalSalaryCoins/100} (рублей)
<br> Сумма средней заработной платы ${totalSalaryCoins/salaryCount/100} (рублей)
<h6>
    <table border="1" width="100%">
        <tr>
            <td>ID</td>
            <td>COINS</td>
            <td>DATE</td>
            <td>TEACHER ID</td>
            <td>УДАЛИТЬ</td>
            <td>ИЗМЕНИТЬ</td>
        </tr>
        <c:forEach var="salary" items="${requestScope.teacher.salaries}">
            <tr>
                <td><c:out value="${salary.id}"/></td>
                <td><c:out value="${salary.coins}"/></td>
                <td><c:out value="${salary.date}"/></td>
                <td><c:out value="${salary.teacherId}"/></td>
                <td>
                    <form action="SalaryController" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value=${salary.id}>
                        <input name="login" type="hidden" value="${requestScope.teacher.credential.login}">
                        <button type="submit">Удалить зарплату</button>
                    </form>
                </td>
                <td>
                    <form action="SalaryController" method="post">
                        <input type="hidden" name="action" value="put">
                        <input type="hidden" name="id" value=${salary.id}>
                        <input type="hidden" name="teacherId" value=${salary.teacherId}>
                        <input name="login" type="hidden" value="${requestScope.teacher.credential.login}">
                        Salary amount in coins:<input type="text" name="coins" value=${salary.coins}><br/>
                        Salary date:<input type="date" name="date" value=${salary.date}><br/>
                        <button type="submit">Изменить зарплату учителю</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</h6>
Добавить зарплату для учителя:
<form action="<c:url value="SalaryController"/>" method="post">
    <input name="teacherId" type="hidden" value="${requestScope.teacher.id}">
    <input name="login" type="hidden" value="${requestScope.teacher.credential.login}">
    Введите зарплату в копейках: <label> <input type="number" name="coins" value="0"/> </label><br/>
    Введите дату начисления: <label> <input type="date" name="date" value="2021-01-01"/> </label><br/>
    <input type="reset">
    <button type="submit">Создать зарплату и добавить учителю ${requestScope.teacher.credential.login}</button>
</form>
<h5><a href="${pageContext.request.contextPath}/admin_teachers_page.jsp">Go back to teachers page.</a></h5>
</body>
</html>