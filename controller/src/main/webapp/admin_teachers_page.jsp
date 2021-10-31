<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Admin teachers page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/admin_front_page.css'/>"/>
    <link rel="shortcut icon" href="img/book_favicon.png" type="image/png">
</head>
<body>
<h1>ADMIN - TEACHER SALARIES PAGE!</h1>
<h4>Учителя нашего клуба по интересам</h4>
<h3 class="message">
    <c:if test="${not empty requestScope.message}">
        <c:out value="${requestScope.message}"/>
    </c:if>
</h3>
<table id="teachersTable">
<%--    <tr>--%>
<%--        <td>(LOGIN)</td>--%>
<%--        <td>(FIRST NAME)</td>--%>
<%--        <td>(LAST NAME)</td>--%>
<%--        <td>(PATRONYMIC)</td>--%>
<%--        <td>(DATE OF BIRTH)</td>--%>
<%--    </tr>--%>
    <c:forEach var="teacher" items="${applicationScope.teacherRepo.teachersList}">
        <tr>
<%--            <td>${teacher.credential.login}</td>--%>
<%--            <td>${teacher.firstname}</td>--%>
<%--            <td>${teacher.lastname}</td>--%>
<%--            <td>${teacher.patronymic}</td>--%>
<%--            <td>${teacher.dateOfBirth}</td>--%>

        </tr>
        <br>
        <tr>
            <form action="TeacherController" method="post">
                <td>
                    <input type="hidden" name="action" value="put">
                    <input type="hidden" name="id" value=${teacher.id}>
                    <input type="hidden" name="credentialId" value=${teacher.credential.id}>
                    Username: ${teacher.credential.login}<label>
                    <input type="hidden" name="login" value="${teacher.credential.login}">
                </label>
                </td>
                <td>
                    Password: <label>
                    <input type="text" name="password" value="${teacher.credential.password}">
                </label>
                </td>
                <td>
                    Firstname: <label>
                    <input type="text" name="firstname" value="${teacher.firstname}">
                </label>
                </td>
                <td>
                    Lastname: <label>
                    <input type="text" name="lastname" value="${teacher.lastname}">
                </label>
                </td>
                <td>
                    Patronymic: <label>
                    <input type="text" name="patronymic" value="${teacher.patronymic}">
                </label>
                </td>
                <td>
                    Date of birth: <label>
                    <input type="date" name="dateOfBirth" value="${teacher.dateOfBirth}">
                </label>
                </td>
                <td>
                    <button type="submit">Изменить параметры учителя</button>
                </td>
            </form>
        </tr>
        <td>
            <form action="TeacherController" method="post">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value=${teacher.id}>
                <input type="hidden" name="login" value="${teacher.credential.login}">
                <input type="hidden" name="credentialId" value=${teacher.credential.id}>
                <button type="submit">Удалить учителя</button>
            </form>
        </td>

        <td>
            <form action="SalariesController" method="post">
                <input name="login" type="hidden" value="${teacher.credential.login}">
                <button type="submit">На страницу зарплат</button>
            </form>
        </td>
    </c:forEach>
</table>
<br>
<br>

Добавить нового учителя
<form action="<c:url value="TeacherController"/>" method="post">
    Username: <label> <input type="text" name="login"/> </label><br/>
    Password: <label> <input type="text" name="password"/> </label><br/>
    Firstname: <label> <input type="text" name="firstname"/> </label><br/>
    Lastname: <label> <input type="text" name="lastname"/> </label><br/>
    Patronymic: <label> <input type="text" name="patronymic"/> </label><br/>
    Date of birth: <label> <input type="date" name="dateOfBirth"/> </label><br/>
    <button type="submit">Создать учителя</button>
    <input type="reset">
</form>
<br>

<h5>
    <a href="${pageContext.request.contextPath}/admin_front_page.jsp">Go back to admin home page.</a>
</h5>
<br>
<h5>
    <a href="${pageContext.request.contextPath}/LogoutController">Log out.</a>
</h5>
</body>
</html>