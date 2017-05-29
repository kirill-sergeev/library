<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="genres"/>
<%@ include file="../jspf/header.jspf" %>

<div class="ui center aligned grid basic segment">
    <div class="ui grid centered">
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
                <c:forEach items="${genres}" var="genre">
                    <div class="item">
                        <div class="content">
                            <a href="<c:url value="books.do?genre=${genre.id}"/>">${genre.title}</a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        <div class="center floated row">
            <c:if test="${numberOfPages > 1}">
                <c:set var="action" scope="page" value="genres.do"/>
                <%@ include file="../jspf/pagination.jspf" %>
            </c:if>
        </div>
    </div>
</div>
<%@ include file="../jspf/footer.jspf" %>