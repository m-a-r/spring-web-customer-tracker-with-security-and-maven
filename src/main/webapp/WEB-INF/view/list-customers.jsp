<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>

<html>

<head>
    <title>List Customers</title>

    <!-- reference our style sheet -->

    <link type="text/css"
          rel="stylesheet"
          href="${pageContext.request.contextPath}/resources/css/style.css"/>

</head>

<body>

<jsp:include page="jsp/sections/header.jsp"/>

<div id="container">

    <div id="content">

        <!-- Add Customer button -->
        <security:authorize access="hasAnyRole('ADMIN', 'MANAGER')">

            <input type="button" value="Add Customer"
                   onclick="window.location.href='showFormForAdd'; return false;"
                   class="add-button"
            />

        </security:authorize>

        <!--  add our html table here -->

        <table>
            <tr>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>

                <security:authorize access="hasAnyRole('ADMIN', 'MANAGER')">
                    <th>Action</th>
                </security:authorize>
            </tr>

            <!-- loop over and print our customers -->
            <c:forEach var="tempCustomer" items="${customers}">

                <!-- construct an "update" link with customer id -->
                <c:url var="updateLink" value="/customer/showFormForUpdate">
                    <c:param name="customerId" value="${tempCustomer.id}"/>
                </c:url>

                <!-- construct an "delete" link with customer id -->
                <c:url var="deleteLink" value="/customer/delete">
                    <c:param name="customerId" value="${tempCustomer.id}"/>
                </c:url>

                <tr>
                    <td> ${tempCustomer.firstName} </td>
                    <td> ${tempCustomer.lastName} </td>
                    <td> ${tempCustomer.email} </td>

                    <td>
                        <!-- display the update and delete link -->

                        <security:authorize access="hasAnyRole('ADMIN', 'MANAGER')">
                            <a href="${updateLink}">Update</a>
                        </security:authorize>
                        <security:authorize access="hasRole('ADMIN')">
                            |
                            <a href="${deleteLink}"
                               onclick="if (!(confirm('Are you sure you want to delete this customer?'))) return false">Delete</a>
                        </security:authorize>
                    </td>

                </tr>

            </c:forEach>

        </table>

    </div>

</div>


</body>

</html>









