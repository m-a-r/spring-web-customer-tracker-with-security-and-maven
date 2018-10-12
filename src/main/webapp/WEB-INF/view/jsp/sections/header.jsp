<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="wrapper">
     <%-- Can't logout like below because CSRF protection --%>
    <%--<div><a href="${pageContext.request.contextPath}/logout">logout</a></div>--%>

    <%-- Logout button--%>
    <form:form action="${pageContext.request.contextPath}/logout" method="POST">
        <input type="submit" value="Logout" class="logout-button"/>
    </form:form>

    <%-- Logout link (uses js and doesn't work if opened in new tab) --%>
    <%--<a href="#" onclick="document.getElementById('logout-form').submit();"> Logout </a>--%>
    <%--<form id="logout-form" action="<c:url value="/logout"/>" method="post">--%>
        <%--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>--%>
    <%--</form>--%>

    <div id="header">
        <h2>CRM - Customer Relationship Manager</h2>
    </div>
</div>