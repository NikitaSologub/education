<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Admin teachers page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/general_style.css'/>"/>
    <link rel="shortcut icon" href="../img/book_favicon.png" type="image/png">
</head>
<body>
<h1>ADMIN - TEACHER SALARIES PAGE!</h1>
<h4>Учителя нашего клуба по интересам</h4>
<jsp:include page="header.jsp"/>
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
        <c:forEach var="teacher" items="${requestScope.personsSet}">
            <tr>
                <td><c:out value="${teacher.credential.login}"/></td>
                <td><c:out value="${teacher.credential.password}"/></td>
                <td><c:out value="${teacher.lastname}"/></td>
                <td><c:out value="${teacher.firstname}"/></td>
                <td><c:out value="${teacher.patronymic}"/></td>
                <td><c:out value="${teacher.dateOfBirth}"/></td>
                <td>
                    <form action="<c:url value="/teachers/${teacher.id}"/>" method="post">
                        <input type="hidden" name="hidden_method" value="delete">
                        <input type="hidden" name="login" value="${teacher.credential.login}">
                        <input type="hidden" name="credentialId" value=${teacher.credential.id}>
                        <button type="submit">Удалить учителя</button>
                    </form>
                </td>
                <td>
                    <form action="<c:url value="/teachers/${teacher.id}"/>" method="post">
                        <input type="hidden" name="hidden_method" value="put">
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
                    <form action="<c:url value="/teachers/${teacher.id}/salaries"/>" method="get">
                        <input type="hidden" name="login" value="${teacher.credential.login}">
                        <button type="submit">На страницу зарплат</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</h6>
Добавить нового учителя
<form action="<c:url value="/teachers"/>" method="post">
    Username: <label> <input type="text" name="login"/> </label><br/>
    Password: <label> <input type="password" name="password"/> </label><br/>
    Firstname: <label> <input type="text" name="firstname"/> </label><br/>
    Lastname: <label> <input type="text" name="lastname"/> </label><br/>
    Patronymic: <label> <input type="text" name="patronymic"/> </label><br/>
    Date of birth: <label> <input type="date" name="dateOfBirth"/> </label><br/>
    <button type="submit">Создать учителя</button>
    <input type="reset">
</form>
<jsp:include page="admin_footer.jsp"/>
</body>
</html>