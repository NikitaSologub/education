<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Login page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value='css/login.css'/>"/>
    <link rel="icon" href="img/book_favicon.png" type="image/png">
</head>
<body class="basic">
<h1>Welcome in login page!</h1>
<h1>Please login</h1>
<form action="<c:url value="/LoginController"/>" method="post">
    Username: <label>
    <input type="text" name="login" required placeholder="ваш логин"/>
</label><br/>
    Password: <label>
    <input type="password" name="password" required placeholder="ваш пароль"/>
</label><br/>
    <input type="submit" title="Отправить"/>
    <input type="reset" title="Очистить">
</form>
<h3 class="errorMessage">
    <c:if test="${not empty requestScope.errorMessage}">
        <c:out value="${requestScope.errorMessage}"/>
    </c:if>
</h3>
<a href="${pageContext.request.contextPath}/registration_page.jsp">Not registered? Create an account.</a>
</body>
</html>