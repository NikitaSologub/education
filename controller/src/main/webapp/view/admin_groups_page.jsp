<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Group page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/general_style.css'/>"/>
    <link rel="shortcut icon" href="../img/book_favicon.png" type="image/png">
</head>
<body>
<h1>ADMIN GROUP PAGE!</h1>
<jsp:include page="header.jsp"/>

<h6>
    <table border="1" width="100%">
        <tr>
            <td>ID</td>
            <td>TITLE</td>
            <td>TEACHER</td>
            <td>DESCRIPTION</td>
            <td>ДЕЙСТВИЯ</td>
        </tr>
        <c:forEach var="group" items="${requestScope.groupList}">
            <tr>
                <td><c:out value="${group.id}"/></td>
                <td><c:out value="${group.title}"/></td>
                <td><c:out value="${group.teacher.lastname} ${group.teacher.firstname} ${group.teacher.patronymic}"/></td>
                <td><c:out value="${group.description}"/></td>
                <td>
                    <form action="<c:url value="GroupController"/>" method="post">
                        <input type="hidden" name="hidden_method" value="delete">
                        <input type="hidden" name="id" value=${group.id}>
                        <button type="submit">Удалить</button>
                    </form>
                    <form action="<c:url value="GroupEditController"/>" method="get">
                        <input type="hidden" name="groupId" value="${group.id}">
                        <input type="hidden" name="teacherId" value="${group.teacher.id}">
                        <input type="hidden" name="teacherLogin" value="${group.teacher.credential.login}">
                        <button type="submit">Редактировать</button>
                    </form>
                    <form action="<c:url value="GroupSubjectsController"/>" method="get">
                        <input type="hidden" name="groupId" value="${group.id}">
                        <button type="submit">К предметам</button>
                    </form>
                    <form action="<c:url value="GroupStudentsController"/>" method="get">
                        <input type="hidden" name="groupId" value="${group.id}">
                        <button type="submit">К студентам</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</h6>
<br>
Добавить новую группу без учителя:
<form action="<c:url value="GroupController"/>" method="post">
    Введите название группы: <label> <input type="text" name="title"/> </label><br/>
    Введите описание группы: <label> <input type="text" name="description"/> </label><br/>
    <input type="reset">
    <button type="submit">Создать</button>
</form>
<br>
<jsp:include page="admin_footer.jsp"/>
</body>
</html>