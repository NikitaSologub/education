<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Marks page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/student_style.css'/>"/>
    <link rel="shortcut icon" href="../img/book_favicon.png" type="image/png">
</head>
<body>
<h1>STUDENT ALL MARKS PAGE!</h1>
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
        </tr>
        <c:forEach var="mark" items="${requestScope.objectsSet}">
            <tr>
                <td><c:out value="${mark.id}"/></td>
                <td><c:out value="${mark.subject.title}"/></td>
                <td><c:out value="${mark.point}"/></td>
                <td><c:out value="${mark.date}"/></td>
            </tr>
        </c:forEach>
    </table>
</h6>
<jsp:include page="student_footer.jsp"/>
</body>
</html>