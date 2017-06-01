<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="closed-orders"/>
<%@ include file="../jspf/header.jspf" %>

<%--@elvariable id="alert" type="java.util.List<ua.nure.serhieiev.library.controller.util.Alert>"--%>
<%--@elvariable id="order" type="ua.nure.serhieiev.library.model.entities.Order"--%>
<%--@elvariable id="orders" type="java.util.List<ua.nure.serhieiev.library.model.entities.Order>"--%>

<div class="ui middle aligned center aligned grid basic segment">
    <div class="ui grid centered">
        <div class="twelve wide column">
            <div class="ui center aligned segment">
                <c:if test="${not empty alert}">
                    <div class="ui message ${alert.type}">
                        <ul class="list">
                            <li><fmt:message key="${alert.description}"/></li>
                        </ul>
                    </div>
                </c:if>
                <c:choose>
                    <c:when test="${not empty orders}">
                        <table class="ui black striped table">
                            <thead>
                            <tr>
                                <th class="three wide">Reader</th>
                                <th class="three wide">Librarian</th>
                                <th class="three wide">Books</th>
                                <th>In Library</th>
                                <th>Order date</th>
                                <th>Expected date</th>
                                <th>Return date</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${orders}" var="order">
                                <tr>
                                    <td class="selectable">
                                        <a href="<c:url value="/user.do?id=${order.reader.id}"/>">${order.reader.name}</a>
                                    </td>
                                    <td class="selectable">
                                        <a href="<c:url value="/user.do?id=${order.librarian.id}"/>">${order.librarian.name}</a>
                                    </td>
                                    <td>
                                        <div class="ui middle aligned ordered list">
                                            <c:forEach items="${order.books}" var="book">
                                                <div class="item">
                                                    <div class="content">
                                                        <div class="header"><a href="<c:url value="books.do?id=${book.id}"/>">${book.title}</a></div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </td>
                                    <td>${order.internal? '<i class="checkmark icon"></i>' : '<i class="minus icon"></i>'}</td>
                                    <td>${order.orderDate}</td>
                                    <td>${order.expectedDate}</td>
                                    <td>${order.returnDate}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                            <tfoot>
                            <tr>
                                <th colspan="1">
                                    <c:set var="group" scope="page" value="type=closed&"/>
                                    <c:set var="action" scope="page" value="orders.do"/>
                                    <%@ include file="../jspf/pagination.jspf" %>
                                </th>
                                <th colspan="6">
                                    <form action="<c:url value="/orders.do"/>" method="get" class="ui form right floated">
                                        <input type="hidden" name="type" value="closed">
                                        <div class="inline fields">
                                            <div class="field">
                                                <select class="ui dropdown" name="items">
                                                    <option value="10">10</option>
                                                    <option value="15">15</option>
                                                    <option value="20">20</option>
                                                    <option value="25">25</option>
                                                    <option value="50">50</option>
                                                </select>
                                            </div>
                                            <div class="field">
                                                <select class="ui dropdown" name="sort">
                                                    <option value="reader_id">Reader</option>
                                                    <option value="librarian_id">Librarian</option>
                                                    <option value="order_date">Order Date</option>
                                                    <option value="expected_date">Expected Date</option>
                                                    <option value="return_date">Return Date</option>
                                                    <option value="internal">In Library</option>
                                                </select>
                                            </div>
                                            <div class="field">
                                                <div class="ui toggle checkbox">
                                                    <input <c:out value="${order == 'desc' ? 'checked=checked' : 'value=desc'}"/> type="checkbox" name="order">
                                                    <label>DESC</label>
                                                </div>
                                            </div>
                                            <div class="field">
                                                <div class="ui submit button">Submit</div>
                                            </div>
                                        </div>
                                    </form>
                                </th>
                            </tr>
                            </tfoot>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="ui message info"><div class="header">No closed orders</div></div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>