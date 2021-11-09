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
<h6>
    <table border="1" width="100%">
        <tr>
            <th>LOGIN</th>
            <th>PASSWORD</th>
            <th>ФАМИЛИЯ</th>
            <th>ИМЯ</th>
            <th>ОТЧЕСТВО</th>
            <th>ДАТА РОЖДЕНИЯ</th>
            <th>УДАЛИТЬ</th>
            <th>ИЗМЕНИТЬ</th>
            <th>К ЗАРПЛАТАМ</th>
        </tr>
        <c:forEach var="teacher" items="${applicationScope.teacherRepo.teachersList}">
            <tr>
                <td><c:out value="${teacher.credential.login}"/></td>
                <td><c:out value="${teacher.credential.password}"/></td>
                <td><c:out value="${teacher.lastname}"/></td>
                <td><c:out value="${teacher.firstname}"/></td>
                <td><c:out value="${teacher.patronymic}"/></td>
                <td><c:out value="${teacher.dateOfBirth}"/></td>
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
                    <form action="TeacherController" method="post">
                        <input type="hidden" name="action" value="put">
                        <input type="hidden" name="id" value=${teacher.id}>
                        <input type="hidden" name="credentialId" value=${teacher.credential.id}>
                        <input type="hidden" name="login" value="${teacher.credential.login}">
                        Password: <input type="password" name="password" value="${teacher.credential.password}">
                        Firstname: <input type="text" name="firstname" value="${teacher.firstname}">
                        Lastname: <input type="text" name="lastname" value="${teacher.lastname}">
                        Patronymic: <input type="text" name="patronymic" value="${teacher.patronymic}">
                        Date of birth: <input type="date" name="dateOfBirth" value="${teacher.dateOfBirth}">
                        <button type="submit">Изменить параметры учителя</button>
                    </form>
                </td>
                <td>
                    <form action="SalaryController" method="get">
                        <input name="login" type="hidden" value="${teacher.credential.login}">
                        <button type="submit">На страницу зарплат</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</h6>
Добавить нового учителя
<form action="<c:url value="TeacherController"/>" method="post">
    Username: <label> <input type="text" name="login"/> </label><br/>
    Password: <label> <input type="password" name="password"/> </label><br/>
    Firstname: <label> <input type="text" name="firstname"/> </label><br/>
    Lastname: <label> <input type="text" name="lastname"/> </label><br/>
    Patronymic: <label> <input type="text" name="patronymic"/> </label><br/>
    Date of birth: <label> <input type="date" name="dateOfBirth"/> </label><br/>
    <button type="submit">Создать учителя</button>
    <input type="reset">
</form>
<h5> <a href="${pageContext.request.contextPath}/admin_front_page.jsp">Go back to admin home page.</a> </h5>
<h5> <a href="${pageContext.request.contextPath}/LogoutController">Log out.</a> </h5>
</body>
</html>