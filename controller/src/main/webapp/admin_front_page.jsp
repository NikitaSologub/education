<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Admin front page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/admin_front_page.css'/>"/>
    <link rel="shortcut icon" href="img/book_favicon.png" type="image/png">
</head>
<body>
<h1>Welcome in ADMIN HOME PAGE!</h1>

<h3 class="message">
    <c:if test="${not empty requestScope.message}">
        <c:out value="${requestScope.message}"/>
    </c:if>
</h3>
<br>
<h5>
    <a href="${pageContext.request.contextPath}/admin_teachers_page.jsp">Go to admin salaries page.</a>
</h5>
<br>
<h5>
    <a href="${pageContext.request.contextPath}/LogoutController">Log out.</a>
</h5>
</body>
</html>