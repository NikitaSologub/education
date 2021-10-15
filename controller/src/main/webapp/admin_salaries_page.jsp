<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Admin salaries page</title>
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
    <tr>
        <td>(LOGIN)</td>
        <td>(FIRST NAME)</td>
        <td>(LAST NAME)</td>
        <td>(PATRONYMIC)</td>
        <td>(DATE OF BIRTH)</td>
    </tr>
    <c:forEach var="teacher" items="${applicationScope.teacherRepo.teachersList}">
        <tr>
            <td>${teacher.credential.login}</td>
            <td>${teacher.firstname}</td>
            <td>${teacher.lastname}</td>
            <td>${teacher.patronymic}</td>
            <td>${teacher.dateOfBirth}</td>
            <td>
                <form action="TeacherController" method="post">
                    <input type="hidden" name="action" value="delete"/>
                    <input name="teacherLogin" type="hidden" value="${teacher.credential.login}">
                    <button type="submit">Удалить учителя</button>
                </form>
            </td>
        </tr>
        <br>
        <tr>
            <form action="TeacherController" method="post">
                <td>

                    <input type="hidden" name="action" value="put"/>
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
                    <button type="submit">Change some teacher's parameters</button>
                </td>
            </form>
        </tr>
    </c:forEach>
</table>
<br>
<br>


Добавить нового учителя
<form action="<c:url value="TeacherController"/>" method="post">
    Username: <label>
    <input type="text" name="login"/>
</label><br/>
    Password: <label>
    <input type="text" name="password"/>
</label><br/>
    Firstname: <label>
    <input type="text" name="firstname"/>
</label><br/>
    Lastname: <label>
    <input type="text" name="lastname"/>
</label><br/>
    Patronymic: <label>
    <input type="text" name="patronymic"/>
</label><br/>
    Date of birth: <label>
    <input type="date" name="dateOfBirth"/>
</label><br/>
    <button type="submit">Создать учителя</button>
    <input type="reset">
</form>
<br>
<br>


<c:forEach var="teacher" items="${applicationScope.teacherRepo.teachersList}">
    <div>
        <div>
            <p>LOGIN | FIRST NAME | LAST NAME | PATRONYMIC | DATE OF BIRTH</p>
            <p>${teacher.credential.login} | ${teacher.firstname} | ${teacher.lastname} | ${teacher.patronymic}
                | ${teacher.dateOfBirth}</p>
            <form action="/web-app/salary" method="post">
                <input name="id" type="hidden" value="${teacher.credential.login}">
                <input name="name" type="hidden" value="${teacher.firstname}">
                <input name="age" type="hidden" value="${teacher.lastname}">
                <input name="salary" type="hidden" value="${teacher.patronymic}">
                <button type="submit">Average salary</button>
            </form>
        </div>
    </div>
</c:forEach>

<h5>
    <a href="${pageContext.request.contextPath}/admin_front_page.jsp">Go back to admin home page.</a>
</h5>
<br>
<h5>
    <a href="${pageContext.request.contextPath}/LogoutController">Log out.</a>
</h5>
</body>
</html>