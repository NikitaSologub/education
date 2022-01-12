<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html>
<body>
<h6 class="session">
    <c:out value="В системе пользователь: ${sessionScope.sessionEntity.lastname} ${sessionScope.sessionEntity.firstname}
    ${sessionScope.sessionEntity.patronymic} под ником ${sessionScope.sessionEntity.credential.login}"/>
</h6>
<h3 class="message">
    <c:if test="${not empty requestScope.message}">
        <c:out value="${requestScope.message}"/>
    </c:if>
</h3>
</body>
</html>