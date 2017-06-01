<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="cart"/>
<%@ include file="../jspf/header.jspf" %>

<%--@elvariable id="alert" type="ua.nure.serhieiev.library.controller.util.Alert"--%>
<%--@elvariable id="books" type="java.util.List<ua.nure.serhieiev.library.model.entities.Book>"--%>
<%--@elvariable id="localCart" type="java.util.Map<java.lang.Integer, java.time.LocalDateTime>"--%>

<div class="ui center aligned grid basic segment">
    <div class="ui grid centered">
        <c:if test="${not empty alert}">
            <div class="row">
                <div class="ui message ${alert.type}">
                    <div class="header"><fmt:message key="${alert.description}"/></div>
                </div>
            </div>
        </c:if>
        <div class="ui center aligned basic segment">
            <c:choose>
                <c:when test="${not empty localCart}">
                    <table class="ui green striped table">
                        <thead>
                        <tr>
                            <th><fmt:message key="table.title"/></th>
                            <th><fmt:message key="table.authors"/></th>
                            <th><fmt:message key="table.genres"/></th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${books}" var="book">
                            <tr>
                                <td class="selectable"><a
                                        href="<c:url value="/book.do?id=${book.id}"/>">${book.title}</a></td>
                                <td>
                                    <c:forEach items="${book.authors}" var="author" varStatus="status">
                                        ${author.name}<c:if test="${not status.last}">,</c:if>
                                    </c:forEach>
                                </td>
                                <td>
                                    <c:forEach items="${book.genres}" var="genre" varStatus="status">
                                        ${genre.title}<c:if test="${not status.last}">,</c:if>
                                    </c:forEach>
                                </td>
                                <td>
                                    <form action="<c:url value="/cart.do"/>" method="post">
                                        <input type="hidden" name="book" value="${book.id}"/>
                                        <button class="ui submit button circular icon negative" type="submit"
                                                name="button" value="remove"><i class="remove icon"></i></button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                        <tfoot>
                        <tr>
                            <th colspan="3">
                                <form action="<c:url value="/cart.do"/>" method="post" class="ui form">
                                    <button type="submit" name="button" value="order" class="ui submit button positive">
                                         <fmt:message key="cart.button.make_order"/>
                                    </button>
                                    <div class="ui checkbox">
                                        <input type="checkbox" name="internal" value="true">
                                        <label><fmt:message key="cart.checkbox.internal"/></label>
                                    </div>
                                    <button type="submit" name="button" value="clear"
                                            class="ui submit button negative right floated">
                                       <fmt:message key="cart.button.clear"/>
                                    </button>
                                </form>
                            </th>
                        </tr>
                        </tfoot>
                    </table>
                </c:when>
                <c:otherwise>
                    <c:if test="${empty alert}">
                        <div class="ui message info">
                            <div class="header"><fmt:message key="alert.info.empty_cart"/></div>
                        </div>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>