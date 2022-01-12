<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Admin students page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/general_style.css'/>"/>
    <link rel="shortcut icon" href="../img/book_favicon.png" type="image/png">
</head>
<body>
<h1>ADMIN - STUDENTS MARKS PAGE!</h1>
<h4>Студенты наших клубов по интересам</h4>
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
            <th>К ОЦЕНКАМ</th>
        </tr>
        <c:forEach var="student" items="${requestScope.personsSet}">
            <tr>
                <td><c:out value="${student.credential.login}"/></td>
                <td><c:out value="${student.credential.password}"/></td>
                <td><c:out value="${student.lastname}"/></td>
                <td><c:out value="${student.firstname}"/></td>
                <td><c:out value="${student.patronymic}"/></td>
                <td><c:out value="${student.dateOfBirth}"/></td>
                <td>
                    <form action="StudentController" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value=${student.id}>
                        <input type="hidden" name="login" value="${student.credential.login}">
                        <input type="hidden" name="credentialId" value=${student.credential.id}>
                        <button type="submit">Удалить студента</button>
                    </form>
                </td>
                <td>
                    <form action="StudentController" method="post">
                        <input type="hidden" name="action" value="put">
                        <input type="hidden" name="id" value=${student.id}>
                        <input type="hidden" name="credentialId" value=${student.credential.id}>
                        <input type="hidden" name="login" value="${student.credential.login}">
<%--                        Username: ${student.credential.login}--%>
                        Password: <input type="password" name="password" value="${student.credential.password}">
                        Firstname: <input type="text" name="firstname" value="${student.firstname}">
                        Lastname: <input type="text" name="lastname" value="${student.lastname}">
                        Patronymic: <input type="text" name="patronymic" value="${student.patronymic}">
                        Date of birth: <input type="date" name="dateOfBirth" value="${student.dateOfBirth}">
                        <button type="submit">Изменить параметры студента</button>
                    </form>
                </td>
                <td>
                    <form action="<c:url value="MarkController"/>" method="get">
                        <input type="hidden" name="login" value="${student.credential.login}">
                        <input type="hidden" name="studentId" value="${student.id}">
                        <input type="hidden" name="credentialId" value="${student.credential.id}">
                        <button type="submit">На страницу оценок</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</h6>
Добавить нового студента
<form action="<c:url value="StudentController"/>" method="post">
    Username: <label> <input type="text" name="login"/> </label><br/>
    Password: <label> <input type="password" name="password"/> </label><br/>
    Firstname: <label> <input type="text" name="firstname"/> </label><br/>
    Lastname: <label> <input type="text" name="lastname"/> </label><br/>
    Patronymic: <label> <input type="text" name="patronymic"/> </label><br/>
    Date of birth: <label> <input type="date" name="dateOfBirth"/> </label><br/>
    <button type="submit">Создать студента</button>
    <input type="reset">
</form>
<jsp:include page="admin_footer.jsp"/>
</body>
</html>