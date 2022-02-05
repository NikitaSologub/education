<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Student front page</title>
    <link rel="shortcut icon" href="../../img/book_favicon.png" type="image/png">
</head>
<body>
<h1>Welcome in Student front page</h1>
<jsp:include page="/view/header.jsp"/>
<a href="${pageContext.request.contextPath}/logout">Log out.</a>
</body>
</html>