<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pagename" scope="request" value="reader-profile"/>
<%@ include file="../jspf/header.jspf" %>
<%@ include file="../jspf/navbar.jspf" %>

<div class="container">
    <div class="row">
        <div class="col-sm-4 col-sm-offset-4">
            <table class="table table-sm table-bordered sortable">
                <thead>
                <tr>
                    <th>Books</th>
                    <th>Order Date</th>
                    <th>Expected Date</th>
                    <th>Returned Date</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${orders}" var="order">
                    <tr>
                        <th scope="row">
                            <c:forEach items="${order.books}" var="book">
                                <a href="<c:url value="books.do?id=${book.id}"/>">${book.title}</a><br>
                            </c:forEach>
                        </th>
                        <td>order.orderDate</td>
                        <td>order.expectedDate</td>
                        <td>order.returnedDate</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <c:set var="action" scope="page" value="orders.do"/>
    <%@ include file="../jspf/pagination.jspf" %>
</div>

<%@ include file="../jspf/footer.jspf" %>