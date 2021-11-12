<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Group page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/general_style.css'/>"/>
    <link rel="shortcut icon" href="img/book_favicon.png" type="image/png">
</head>
<body>
<h1>ADMIN GROUP PAGE!</h1>
<jsp:include page="header.jsp"/>
Название группы: ${requestScope.group.title}<br>
Описание группы: ${requestScope.group.description}<br>
ФИО учителя: ${requestScope.group.teacher.lastname} ${requestScope.group.teacher.firstname} ${requestScope.group.teacher.patronymic}<br>
<h6>
    <table border="1" width="100%">
        <tr>
            <td>ID</td>
            <td>ФАМИЛИЯ</td>
            <td>ИМЯ</td>
            <td>ОТЧЕСТВО</td>
            <td>НАЗНАЧИТЬ РУКОВОДИТЕЛЕМ ГРУППЫ</td>
        </tr>
        <c:forEach var="t" items="${requestScope.objectsList}">
            <tr>
                <td><c:out value="${t.id}"/></td>
                <td><c:out value="${t.lastname}"/></td>
                <td><c:out value="${t.firstname}"/></td>
                <td><c:out value="${t.patronymic}"/></td>
                <td>
                    <form action="<c:url value="GroupEditController"/>" method="post">
                        <input type="hidden" name="action" value="put">
                        <input type="hidden" name="groupId" value=${requestScope.group.id}>
                        <input type="hidden" name="teacherId" value=${t.id}>
                        <input type="hidden" name="teacherLogin" value=${t.credential.login}>
                        <button type="submit">Назначить</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</h6>

Изменить Название и/или описание группы
<form action="<c:url value="GroupEditController"/>" method="post">
    <input type="hidden" name="groupId" value=${requestScope.group.id}>
    <input type="hidden" name="teacherId" value=${requestScope.teacher.id}>
    <input type="hidden" name="teacherLogin" value=${requestScope.teacher.credential.login}>
    Введите новое название:<input type="text" name="title" value=${requestScope.group.title}><br/>
    Введите новое описание:<input type="text" name="description" value=${requestScope.group.description}><br/>
    <button type="submit">Изменить параметры группы</button>
</form>

Убрать учителя с должности руководителя группы (Оставить место вакантным)
<form action="<c:url value="GroupEditController"/>" method="post">
    <input type="hidden" name="action" value="delete">
    <input type="hidden" name="groupId" value=${requestScope.group.id}>
    <input type="hidden" name="groupId" value=${requestScope.group.id}>
    <button type="submit">Разжаловать учителя</button>
</form>

<jsp:include page="admin_footer.jsp"/>
</body>
</html>