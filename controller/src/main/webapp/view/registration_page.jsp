<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/hallway_style.css'/>"/>
    <link rel="shortcut icon" href="../img/book_favicon.png" type="image/png">
</head>

<body>
<h1>Welcome in registration page!</h1>
<h1>Please register your account</h1>

<form action="<c:url value="/"/>" method="post"><!--форма пока что ведет в никуда-->
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
    <input type="text" name="lastname"/>
</label><br/>
    Patronymic: <label>
    <input type="text" name="patronymic"/>
</label><br/>
    Date of birth: <label>
    <input type="date" name="dateOfBirth"/>
</label><br/>
    <input type="submit"/>
    <input type="reset">
</form>

<h3 class="errorMessage">
    <c:if test="${not empty requestScope.errorMessage}">
        <c:out value="${requestScope.errorMessage}"/>
    </c:if>
</h3>
<a href="${pageContext.request.contextPath}/view/login_page.jsp">Already have an account? Login here.</a>
</body>
</html>