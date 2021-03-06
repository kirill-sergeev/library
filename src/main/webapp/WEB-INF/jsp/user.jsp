<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>
<c:set var="pagename" scope="request" value="user"/>
<%@ include file="../jspf/header.jspf" %>

<%--@elvariable id="alert" type="java.util.List<ua.nure.serhieiev.library.controller.util.Alert>"--%>
<%--@elvariable id="profile" type="ua.nure.serhieiev.library.model.entities.User"--%>
<%--@elvariable id="orders" type="java.util.Map<ua.nure.serhieiev.library.model.entities.Order, java.lang.Integer>"--%>

<div class="ui center aligned grid basic segment">
    <div class="ui grid centered">
        <div class="column">
            <c:if test="${not empty alert}">
                <div class="row">
                    <div class="ui message ${alert.type}">
                        <div class="header"><fmt:message key="${alert.description}"/></div>
                    </div>
                </div>
            </c:if>
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
                                <tr ${order.key.returnDate == null && order.key.expectedDate.isBefore(f:getNowDate()) ? 'class="error"': ''}>
                                    <td>
                                        <div class="ui middle aligned ordered list">
                                            <c:forEach items="${order.key.books}" var="book">
                                                <div class="item">
                                                    <div class="content">
                                                        <div class="header"><a
                                                                href="<c:url value="book.do?id=${book.id}"/>">${book.title}</a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </td>
                                    <td>${order.key.internal? '<i class="checkmark icon"></i>' : '<i class="minus icon"></i>'}</td>
                                    <td>${order.key.orderDate}</td>
                                    <td>${order.key.expectedDate}</td>
                                    <td>${order.value == 0 ? order.key.returnDate : '<i class="attention icon"></i>$'.concat(order.value)}</td>
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

            <c:if test="${profile.id == user.id}">
                <div class="column form-column">
                    <h3 class="ui header"><fmt:message key="change-password.header.change"/></h3>
                    <form action="<c:url value="/user.do"/>" method="post" class="ui form">
                        <div class="ui stacked">
                            <div class="field">
                                <div class="ui left icon input">
                                    <i class="lock icon"></i>
                                    <input type="password" name="password"
                                           placeholder="<fmt:message key="change-password.field.password"/>">
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui left icon input">
                                    <i class="lock icon"></i>
                                    <input type="password" name="retype-password"
                                           placeholder="<fmt:message key="change-password.field.retype"/>">
                                </div>
                            </div>
                            <div class="ui fluid large teal submit button"><fmt:message key="book.button.change"/></div>
                        </div>
                        <div class="ui error message"></div>
                    </form>
                </div>
                <div class="column form-column">
                    <h3 class="ui header"><fmt:message key="user.header.change_email"/></h3>
                    <form action="<c:url value="/user.do"/>" method="post" class="ui form">
                        <div class="ui stacked">
                            <div class="field">
                                <div class="ui left icon input">
                                    <i class="user icon"></i>
                                    <input type="email" name="email"
                                           placeholder="<fmt:message key="register.field.email"/>">
                                </div>
                            </div>
                            <div class="ui fluid large teal submit button"><fmt:message key="book.button.change"/></div>
                        </div>
                        <div class="ui error message"></div>
                    </form>
                </div>
            </c:if>
        </div>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>