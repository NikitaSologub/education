<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Teacher front page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/general_style.css'/>"/>
    <link rel="shortcut icon" href="../img/book_favicon.png" type="image/png">
</head>
<body>
<h1>Welcome in Teacher front page</h1>
<jsp:include page="/view/header.jsp"/>
<jsp:include page="teacher_footer.jsp"/>
</body>
</html>