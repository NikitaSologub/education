<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Subjects page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/admin_front_page.css'/>"/>
    <link rel="shortcut icon" href="img/book_favicon.png" type="image/png">
</head>
<body>
<h1>ADMIN SUBJECTS PAGE!</h1>
<jsp:include page="header.jsp"/>
<h6>
    <table border="1" width="100%">
        <tr>
            <td>ID</td>
            <td>TITLE</td>
            <td>УДАЛИТЬ</td>
            <td>ИЗМЕНИТЬ</td>
        </tr>
        <c:forEach var="subject" items="${requestScope.subjects}">
            <tr>
                <td><c:out value="${subject.id}"/></td>
                <td><c:out value="${subject.title}"/></td>
                <td>
                    <form action="<c:url value="SubjectController"/>" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value=${subject.id}>
                        <input type="hidden" name="title" value=${subject.title}>
                        <button type="submit">Удалить предмет</button>
                    </form>
                </td>
                <td>
                    <form action="<c:url value="SubjectController"/>" method="post">
                        <input type="hidden" name="action" value="put">
                        <input type="hidden" name="id" value=${subject.id}>
                        Введите новое название:<input type="text" name="title" value=${subject.title}><br/>
                        <button type="submit">Изменить название дисциплины</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</h6>
<br>
Добавить новый предмет:
<form action="<c:url value="SubjectController"/>" method="post">
    Введите название дисциплины: <label> <input type="text" name="title"/> </label><br/>
    <input type="reset">
    <button type="submit">Создать предмет</button>
</form>
<br>
<jsp:include page="admin_footer.jsp"/>
</body>
</html>
