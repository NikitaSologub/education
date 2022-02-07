<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title>Salaries page</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/teacher_style.css'/>"/>
        <link rel="shortcut icon" href="../img/book_favicon.png" type="image/png">
    </head>
<body>
<h1>TEACHER SALARIES PAGE!</h1>
<jsp:include page="header.jsp"/>
${requestScope.teacher.credential.login} | ${requestScope.teacher.lastname} | ${requestScope.teacher.firstname} |
${requestScope.teacher.patronymic} | ${requestScope.teacher.dateOfBirth}

<form action="<c:url value="/salaries/${requestScope.teacher.id}"/>" method="post">
    <button type="submit">Узнать среднюю зарплату</button>
</form>
<h3 class="average">
    <c:if test="${not empty requestScope.average}">
        <c:out value="Сумма средней заработной платы ${requestScope.average} белорусских рублей"/>
    </c:if>
</h3>

<h6>
    <table border="1" width="100%">
        <tr>
            <td>ID</td>
            <td>COINS</td>
            <td>DATE</td>
        </tr>
        <c:forEach var="salary" items="${requestScope.objectsSet}">
            <tr>
                <td><c:out value="${salary.id}"/></td>
                <td><c:out value="${salary.coins}"/></td>
                <td><c:out value="${salary.date}"/></td>
            </tr>
        </c:forEach>
    </table>
</h6>
<jsp:include page="teacher_footer.jsp"/>
</body>
</html>