<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pagename" scope="request" value="orders"/>
<%@ include file="../jspf/header.jspf" %>
<%@ include file="../jspf/navbar.jspf" %>

<div class="container">
    <div class="row">
            <table class="table table-sm table-bordered sortable">
                <thead>
                <tr>
                    <th>Reader</th>
                    <th>Librarian</th>
                    <th>Books</th>
                    <th>Order Date</th>
                    <th>Expected Date</th>
                    <th>Return Date</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${orders}" var="order">
                    <tr>
                        <th>${order.reader.name}</th>
                        <th>${order.librarian.name}</th>
                        <th>
                            <c:forEach items="${order.books}" var="book">
                                <a href="<c:url value="books.do?id=${book.id}"/>">${book.title}</a><br>
                            </c:forEach>
                        </th>
                        <td>${order.orderDate}</td>
                        <td>${order.expectedReturnDate}</td>
                        <td>${order.returnDate}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    <c:if test="${not empty reader}"><c:set var="group" scope="page" value="reader=${reader}&"/></c:if>
    <c:set var="action" scope="page" value="orders.do"/>
    <%@ include file="../jspf/pagination.jspf" %>
</div>

<%@ include file="../jspf/footer.jspf" %>