<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Title</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/registration.css" type="text/css"/>
    <link rel="shortcut icon" href="img/book_favicon.png" type="image/png">
</head>

<body>
<h1>Welcome in registration page!</h1>
<h1>Please register your account</h1>

<form action="<c:url value="/FrontController"/>" method="post">
    Username: <label>
    <input type="text" name="username"/>
</label><br/>
    Password: <label>
    <input type="password" name="password"/>
</label><br/>
    Firstname: <label>
    <input type="text" name="firstname"/>
</label><br/>
    Lastname: <label>
    <input type="password" name="lastname"/>
</label><br/>
    Patronymic: <label>
    <input type="password" name="patronymic"/>
</label><br/>
    Date of birth: <label>
    <input type="date" name="dateOfBirth"/>
</label><br/>
    <input type="submit"/>
    <input type="reset">
</form>

<h3 id="errorMessage">
    <c:if test="${not empty errorMessage}">
        <c:out value="${errorMessage}"/>
    </c:if>
</h3>
<a href="${pageContext.request.contextPath}/login_page.jsp">Already have an account? Login here.</a>
</body>
</html>
