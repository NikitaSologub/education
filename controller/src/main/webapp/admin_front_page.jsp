<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
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
    <c:if test="${not empty message}">
        <c:out value="${message}"/>
    </c:if>
</h3>
<a href="${pageContext.request.contextPath}/LogoutController">Log out.</a>
</body>
</html>