<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="user"/>
<%@ include file="../jspf/header.jspf" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>

<div class="ui center aligned grid basic segment">
    <div class="ui grid centered">
        <c:if test="${not empty alert}">
            <div class="ui message ${alert.type}">
                <ul class="list">
                    <li><fmt:message key="${alert.description}"/></li>
                </ul>
            </div>
        </c:if>
        <div class="column">
            <div class="ui card">
                <div class="content centered">
                    <a class="header">${profile.name}</a>
                    <div class="meta">
                        <span class="date">Joined ${profile.registrationDate}</span>
                    </div>
                    <div class="meta">
                        <span class="date">Last visit ${profile.lastVisit}</span>
                    </div>
                    <div class="description">
                        <a class="ui basic label">${profile.role.value()}</a>
                        <c:if test="${not profile.enabled}"><a class="ui red label">blocked</a></c:if>
                    </div>
                </div>
                <c:if test="${profile.role == 'READER'}">
                    <div class="extra content">
                        <i class="remove bookmark icon"></i>Orders: ${orders.size()}
                    </div>
                </c:if>
            </div>
            <%--@elvariable id="alert" type="java.util.List<ua.nure.serhieiev.library.controller.util.Alert>"--%>
            <%--@elvariable id="orders" type="java.util.List<ua.nure.serhieiev.library.model.entities.Order>"--%>
            <c:choose>
                <c:when test="${not empty orders}">
                    <div class="ui center aligned segment">
                        <table class="ui black striped table">
                            <thead>
                            <tr>
                                <th>Books</th>
                                <th>In library</th>
                                <th>Order date</th>
                                <th>Expected date</th>
                                <th>Return date</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${orders}" var="order">
                                <tr ${order.expectedDate.isAfter(f:getNowDate()) ? 'class="error"': ''}>
                                    <td>
                                        <div class="ui middle aligned ordered list">
                                            <c:forEach items="${order.books}" var="book">
                                                <div class="item">
                                                    <div class="content">
                                                        <div class="header"><a href="<c:url value="book.do?id=${book.id}"/>">${book.title}</a>
                                                        </div>
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
                        </table>
                    </div>
                </c:when>
                <c:when test="${empty orders && profile.role == 'READER'}">
                    <div class="ui message info">
                        <div class="header">No orders</div>
                    </div>
                </c:when>
            </c:choose>
        </div>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>