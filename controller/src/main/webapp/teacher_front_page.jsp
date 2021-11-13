<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Teacher front page</title>
    <link rel="shortcut icon" href="img/book_favicon.png" type="image/png">
</head>
<body>
<h1>Welcome in Teacher front page</h1>

<h3 class="message">
    <c:if test="${not empty requestScope.message}">
        <c:out value="${requestScope.message}"/>
    </c:if>
</h3>
<a href="${pageContext.request.contextPath}/LogoutController">Log out.</a>
</body>
</html>
