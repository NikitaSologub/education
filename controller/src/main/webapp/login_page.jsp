<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
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

<form action="<c:url value="/LoginController"/>" method="post">
    Username: <label>
    <input type="text" name="login"/>
</label><br/>
    Password: <label>
    <input type="password" name="password"/>
</label><br/>
    <input type="submit" title="Отправить"/>
    <input type="reset" title="Очистить">
</form>

<h3 id="errorMessage">
    <%--@elvariable id="errorMessage" type="java.lang.String"--%>
    <c:if test="${not empty errorMessage}">
        <c:out value="${errorMessage}"/>
    </c:if>
</h3>
<a href="${pageContext.request.contextPath}/registration_page.jsp">Not registered? Create an account.</a>
</body>
</html>
