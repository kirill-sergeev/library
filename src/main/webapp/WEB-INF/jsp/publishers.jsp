<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="publishers"/>
<%@ include file="../jspf/header.jspf" %>

<div class="ui center aligned grid basic segment">
    <div class="ui grid centered">
        <div class="center floated row">
            <form action="<c:url value="/publishers.do"/>" method="get" class="ui form">
                <div class="ui action input">
                    <input type="text" name="search" placeholder="Search...">
                    <button class="ui icon button"><i class="search icon"></i></button>
                </div>
                <div class="ui error message"></div>
            </form>
        </div>
        <c:choose>
            <%--@elvariable id="publishers" type="java.util.List<ua.nure.serhieiev.library.model.entities.Publisher>"--%>
            <c:when test="${not empty publishers}">
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
                        <c:forEach items="${publishers}" var="publisher">
                            <div class="item">
                                <div class="content">
                                    <a href="<c:url value="books.do?publisher=${publisher.id}"/>">${publisher.title}</a>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                <div class="center floated row">
                    <c:if test="${numberOfPages > 1}">
                        <c:set var="action" scope="page" value="publishers.do"/>
                        <%@ include file="../jspf/pagination.jspf" %>
                    </c:if>
                </div>
            </c:when>
            <c:otherwise>
                <div class="ui message info">
                    <div class="header">Not found</div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<%@ include file="../jspf/footer.jspf" %>