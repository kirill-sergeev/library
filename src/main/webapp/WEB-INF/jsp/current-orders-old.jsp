<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pagename" scope="request" value="current-orders"/>
<%@ include file="../jspf/header.jspf" %>
<%@ include file="../jspf/navbar.jspf" %>

<div class="container">
    <div class="row">
        <c:choose>
            <c:when test="${not empty orders}">
            <table class="table table-sm table-bordered sortable">
                <thead>
                <tr>
                    <th>Reader</th>
                    <th>Librarian</th>
                    <th>Books</th>
                    <th>Order date</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${orders}" var="order">
                    <tr>
                        <th>${order.reader.name}</th>
                        <th>
                            <c:forEach items="${order.books}" var="book">
                                <a href="<c:url value="books.do?id=${book.id}"/>">${book.title}</a><br>
                            </c:forEach>
                        </th>
                        <td>${order.librarian.name}</td>
                        <td>${order.orderDate}</td>
                        <td>
                            <div class="btn-group">
                                <form action="<c:url value="/current-orders.do"/>" method="post">
                                    <input type="hidden" name="order" value="${order.id}"/>
                                    <button type="submit" name="button" value="return" class="btn btn-danger">Return
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">
                    No current orders!
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>