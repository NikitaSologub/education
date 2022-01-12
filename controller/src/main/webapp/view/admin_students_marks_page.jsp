<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Marks page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/general_style.css'/>"/>
    <link rel="shortcut icon" href="../img/book_favicon.png" type="image/png">
</head>
<body>
<h1>ADMIN STUDENT MARKS PAGE!</h1>
<jsp:include page="header.jsp"/>
${requestScope.student.credential.login} | ${requestScope.student.lastname} | ${requestScope.student.firstname} |
${requestScope.student.patronymic} | ${requestScope.student.dateOfBirth}

<h6>
    <table border="1" width="100%">
        <tr>
            <td>ID</td>
            <td>ПРЕДМЕТ</td>
            <td>ОЦЕНКА</td>
            <td>DATE</td>
            <td>УДАЛИТЬ</td>
            <td>ИЗМЕНИТЬ</td>
        </tr>
        <c:forEach var="mark" items="${requestScope.student.marks}">
            <tr>
                <td><c:out value="${mark.id}"/></td>
                <td><c:out value="${mark.subject.title}"/></td>
                <td><c:out value="${mark.point}"/></td>
                <td><c:out value="${mark.date}"/></td>
                <td>
                    <form action="<c:url value="MarkController"/>" method="post">
                        <input type="hidden" name="hidden_method" value="delete">
                        <input type="hidden" name="markId" value=${mark.id}>
                        <input type="hidden" name="login" value="${requestScope.student.credential.login}">
                        <input type="hidden" name="studentId" value=${requestScope.student.id}>
                        <button type="submit">Удалить оценку</button>
                    </form>
                </td>
                <td>
                    <form action="<c:url value="MarkController"/>" method="post">
                        <input type="hidden" name="hidden_method" value="put">
                        <input type="hidden" name="markId" value=${mark.id}>
                        <input type="hidden" name="subjectId" value=${mark.subject.id}>
                        <input type="hidden" name="subjectTitle" value=${mark.subject.title}>
                        <input type="hidden" name="login" value="${requestScope.student.credential.login}">
                        <input type="hidden" name="studentId" value=${requestScope.student.id}>
                        mark point:<input type="number" name="point" min="0" max="100" value=${mark.point}><br/>
                        mark date:<input type="date" name="date" value=${mark.date}><br/>
                        <button type="submit">Изменить оценку ученику</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</h6>
Добавить новую оценку ученику:
<form action="<c:url value="MarkController"/>" method="post">
    <input type="hidden" name="studentId" value=${requestScope.student.id}>
    <input type="hidden" name="login" value="${requestScope.student.credential.login}">
    Введите оценку (0-100) баллов: <label> <input type="number" name="point" value="0" min="0" max="100"/> </label><br/>
    Введите дату получения оценки: <label> <input type="date" name="date" value="2021-01-01"/> </label><br/>
    <label for="sub">Введите предмет по которому поставить оценку: </label><br>
    <c:forEach var="subject" items="${requestScope.subjectsSet}">
        <input type="radio" id="sub" name="subjectId" value="${subject.id}"/>${subject.title}<br/>
    </c:forEach>
    <input type="reset">
    <button type="submit">Создать оценку и добавить ученику ${requestScope.student.credential.login}</button>
</form>
<jsp:include page="admin_footer.jsp"/>
</body>
</html>