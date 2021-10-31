<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Admin students page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/admin_front_page.css'/>"/>
    <link rel="shortcut icon" href="img/book_favicon.png" type="image/png">
</head>
<body>

<h1>ADMIN - STUDENTS MARKS PAGE!</h1>
<h4>Студенты наших клубов по интересам</h4>
<h3 class="message">
    <c:if test="${not empty requestScope.message}">
        <c:out value="${requestScope.message}"/>
    </c:if>
</h3>

<table id="studentsTable">
    <c:forEach var="student" items="${applicationScope.studentRepo.studentsList}">
        <tr>
            <form action="StudentController" method="post">
                <td>
                    <input type="hidden" name="action" value="put">
                    <input type="hidden" name="id" value=${student.id}>
                    <input type="hidden" name="credentialId" value=${student.credential.id}>
                    Username: ${student.credential.login}<label>
                    <input type="hidden" name="login" value="${student.credential.login}">
                </label>
                </td>
                <td>
                    Password: <label>
                    <input type="text" name="password" value="${student.credential.password}">
                </label>
                </td>
                <td>
                    Firstname: <label>
                    <input type="text" name="firstname" value="${student.firstname}">
                </label>
                </td>
                <td>
                    Lastname: <label>
                    <input type="text" name="lastname" value="${student.lastname}">
                </label>
                </td>
                <td>
                    Patronymic: <label>
                    <input type="text" name="patronymic" value="${student.patronymic}">
                </label>
                </td>
                <td>
                    Date of birth: <label>
                    <input type="date" name="dateOfBirth" value="${student.dateOfBirth}">
                </label>
                </td>
                <td>
                    <button type="submit">Изменить параметры студента</button>
                </td>
            </form>
        </tr>
        <td>
            <form action="StudentController" method="post">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value=${student.id}>
                <input type="hidden" name="credentialId" value=${student.credential.id}>
                <input type="hidden" name="login" value="${student.credential.login}">
                <button type="submit">Удалить студента</button>
            </form>
        </td>

<%--        <td>--%>
<%--            <form action="MarksController" method="post">--%>
<%--                <input name="login" type="hidden" value="${student.credential.login}">--%>
<%--                <button type="submit">На страницу оценок</button>--%>
<%--            </form>--%>
<%--        </td>--%>
    </c:forEach>
</table>
<br>
<br>
Добавить нового студента
<form action="<c:url value="StudentController"/>" method="post">
    Username: <label> <input type="text" name="login"/> </label><br/>
    Password: <label> <input type="text" name="password"/> </label><br/>
    Firstname: <label> <input type="text" name="firstname"/> </label><br/>
    Lastname: <label> <input type="text" name="lastname"/> </label><br/>
    Patronymic: <label> <input type="text" name="patronymic"/> </label><br/>
    Date of birth: <label> <input type="date" name="dateOfBirth"/> </label><br/>
    <button type="submit">Создать студента</button>
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