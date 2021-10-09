<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Login page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/login.css" type="text/css"/>
    <link rel="shortcut icon" href="img/book_favicon.png" type="image/png">
</head>
<body>
<h1>Welcome in login page!</h1>
<h1>Please login</h1>

<form action="<c:url value="/LoginController"/>" method="post" >
    Username: <input type="text" name="login"/><br/>
    Password: <input type="password" name="password"/><br/>
    <input type="submit" />
    <input type="reset">
</form>

<h3 id="errorMessage">
    <c:if test="${not empty errorMessage}">
        <c:out value="${errorMessage}" />
    </c:if>
</h3>
<a href="${pageContext.request.contextPath}/registration_page.jsp">Not registered? Create an account.</a>
</body>
</html>
