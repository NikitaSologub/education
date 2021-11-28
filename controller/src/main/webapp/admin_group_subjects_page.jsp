<<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Group subjects page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/general_style.css'/>"/>
    <link rel="shortcut icon" href="img/book_favicon.png" type="image/png">
</head>
<body>
<h1>ADMIN GROUP SUBJECTS PAGE!</h1>
<jsp:include page="header.jsp"/>
Название группы: ${requestScope.group.title}<br>
Описание группы: ${requestScope.group.description}<br>
ФИО учителя: ${requestScope.group.teacher.lastname} ${requestScope.group.teacher.firstname} ${requestScope.group.teacher.patronymic}<br>

<h5>Таблица для удаления предметов из группы ${requestScope.group.title}</h5>
<h6>
    <table border="1" width="100%">
        <tr>
            <td>ID</td>
            <td>НАЗВАНИЕ ПРЕДМЕТА</td>
            <td>УБРАТЬ ИЗ ГРУППЫ</td>
        </tr>
        <c:forEach var="s" items="${requestScope.currentGroupObjectsSet}">
            <tr>
                <td><c:out value="${s.id}"/></td>
                <td><c:out value="${s.title}"/></td>
                <td>
                    <form action="<c:url value="GroupSubjectsController"/>" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="groupId" value=${requestScope.group.id}>
                        <input type="hidden" name="subjectId" value=${s.id}>
                        <button type="submit">Удалить</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</h6>
<br>
<h5>Таблица для добавления новых предметов в группу ${requestScope.group.title}</h5>
<h6>
    <table border="1" width="100%">
        <tr>
            <td>ID</td>
            <td>НАЗВАНИЕ ПРЕДМЕТА</td>
            <td>ДОБАВИТЬ В ГРУППУ</td>
        </tr>
        <c:forEach var="s" items="${requestScope.objectsSet}">
            <tr>
                <td><c:out value="${s.id}"/></td>
                <td><c:out value="${s.title}"/></td>
                <td>
                    <form action="<c:url value="GroupSubjectsController"/>" method="post">
                        <input type="hidden" name="groupId" value=${requestScope.group.id}>
                        <input type="hidden" name="subjectId" value=${s.id}>
                        <button type="submit">Добавить</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</h6>
<jsp:include page="admin_footer.jsp"/>
</body>
</html>