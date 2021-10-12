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

<%--Заменим позже адрес выхода на /LogoutController который будет инвалидировать сессию
и посылать дальнейший httpRequest на страницу /login_page.jsp--%>
<a href="${pageContext.request.contextPath}/login_page.jsp">Log out.</a>
</body>
</html>