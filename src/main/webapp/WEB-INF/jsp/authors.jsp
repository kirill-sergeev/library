<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="authors"/>
<%@ include file="../jspf/header.jspf" %>

<%--@elvariable id="numberOfPages" type="java.lang.Integer"--%>
<%--@elvariable id="alert" type="ua.nure.serhieiev.library.controller.util.Alert"--%>
<%--@elvariable id="authors" type="java.util.List<ua.nure.serhieiev.library.model.entities.Author>"--%>

<div class="ui center aligned grid basic segment">
    <div class="ui grid centered">
        <c:if test="${not empty alert}">
            <div class="ui message ${alert.type}">
                <div class="header"><fmt:message key="${alert.description}"/></div>
            </div>
        </c:if>
        <div class="center row">
            <form action="<c:url value="/authors.do"/>" method="get" class="ui form">
                <input type="text" name="search" placeholder="<fmt:message key="field.search"/>">
                <div class="ui error message"></div>
            </form>
        </div>
        <c:choose>
            <c:when test="${not empty authors}">
                <div class="center floated row">
                    <form method="get" class="ui form">
                        <select id="items" name="items" onchange="submit()">
                            <option value="10" ${items == 10 ? 'selected' : ''}>10</option>
                            <option value="15" ${items == 15 ? 'selected' : ''}>15</option>
                            <option value="20" ${items == 20 ? 'selected' : ''}>20</option>
                            <option value="25" ${items == 25 ? 'selected' : ''}>25</option>
                            <option value="50" ${items == 50 ? 'selected' : ''}>50</option>
                        </select>
                    </form>
                </div>
                <div class="ui center aligned segment">
                    <div class="ui middle aligned animated selection list">
                        <c:forEach items="${authors}" var="author">
                            <div class="item">
                                <div class="content">
                                    <a href="<c:url value="books.do?author=${author.id}"/>">${author.name}</a>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                <div class="center floated row">
                    <c:if test="${numberOfPages > 1}">
                        <c:set var="action" scope="page" value="authors.do"/>
                        <%@ include file="../jspf/pagination.jspf" %>
                    </c:if>
                </div>
            </c:when>
            <c:otherwise>
                <div class="ui message info">
                    <div class="header"><fmt:message key="alert.warning.not_found"/></div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>