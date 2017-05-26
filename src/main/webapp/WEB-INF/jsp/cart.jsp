<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pagename" scope="request" value="cart"/>
<%@ include file="../jspf/header.jspf" %>
<%@ include file="../jspf/navbar.jspf" %>

<div class="container">
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <c:if test="${not empty alert}">
                <div class="alert ${alert} alert-dismissable">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                </div>
            </c:if>
            <c:choose>
                <c:when test="${not empty localCart}">
                    <table class="table table-sm table-bordered sortable">
                        <thead>
                        <tr>
                            <th>Book</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="book" items="${books}">
                            <tr>
                                <th scope="row">
                                    <a href="<c:url value="/book.do?id=${book.id}"/>">${book.title}</a>
                                </th>
                                <td>
                                    <div class="btn-group">
                                        <form action="<c:url value="/cart.do"/>" method="post">
                                            <input type="hidden" name="book" value="${book.id}"/>
                                            <button type="submit" name="button" value="remove" class="btn btn-danger">
                                                Remove
                                            </button>
                                            <button type="submit" name="button" value="order" class="btn btn-primary">
                                                Make order
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <form action="<c:url value="/cart.do"/>" method="post">
                        <button type="submit" name="button" value="clear" class="btn btn-danger">
                            Clear cart
                        </button>
                        <button type="submit" name="button" value="order" class="btn btn-primary">
                            Make order
                        </button>
                    </form>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-info">
                        Cart is empty, maybe you want to read something?
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>


<%--            <table class="table table-sm table-bordered sortable">
                <thead>
                <tr>
                    <th>Title</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${globalCart}" var="b">
                    <tr>
                        <th scope="row">${b}</th>
                    </tr>
                </c:forEach>
                </tbody>
            </table>--%>