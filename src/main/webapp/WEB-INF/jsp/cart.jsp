<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="cart"/>
<%@ include file="../jspf/header.jspf" %>

<div class="ui middle aligned center aligned grid basic segment">
    <div class="ui grid centered">
        <div class="ui center aligned segment">
            <c:if test="${not empty alert}">
                <div class="ui message ${alert.type}">
                    <ul class="list">
                        <li><fmt:message key="${alert.description}"/></li>
                    </ul>
                </div>
            </c:if>
            <c:choose>
                <c:when test="${not empty localCart}">
                    <table class="ui green striped table">
                        <thead>
                        <tr>
                            <th>Title</th>
                            <th>Authors</th>
                            <th>Genres</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                            <%--@elvariable id="books" type="java.util.List<ua.nure.serhieiev.library.model.entities.Book>"--%>
                        <c:forEach items="${books}" var="book">
                            <tr>
                                <td class="selectable"><a href="<c:url value="/book.do?id=${book.id}"/>">${book.title}</a></td>
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
                                        <button class="ui submit button circular icon negative" type="submit" name="button" value="remove"><i class="remove icon"></i></button>
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
                                        Make order
                                    </button>
                                    <button type="submit" name="button" value="clear" class="ui submit button negative right floated">
                                        Clear cart
                                    </button>
                                </form>
                            </th>
                        </tr>
                        </tfoot>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="ui message info">
                        <div class="header">Cart is empty, maybe you want to read something?</div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>