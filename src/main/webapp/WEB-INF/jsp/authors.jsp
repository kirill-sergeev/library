<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="authors"/>
<%@ include file="../jspf/header.jspf" %>

<div class="ui middle aligned center aligned grid basic segment">
    <div class="ui grid centered">
        <div class="ui center aligned segment">
            <div class="center floated column">
                <div class="ui middle aligned animated selection list">
                    <c:forEach items="${authors}" var="author">
                        <div class="item">
                            <div class="content">
                                <a href="<c:url value="books.do?author=${author.id}"/>">${author.name}</a>
                            </div>
                        </div>
                    </c:forEach>
                    <c:set var="action" scope="page" value="authors.do"/>
                    <%@ include file="../jspf/pagination.jspf" %>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="../jspf/footer.jspf" %>