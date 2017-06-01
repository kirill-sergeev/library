<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="index"/>
<%@ include file="../jspf/header.jspf" %>

<div class="ui center aligned grid basic segment">
    <div class="column form-column">
            <div class="container">
                <h3 class="ui icon header">
                    <i class="circular bookmark icon"></i>
                    This is main page of library website
                </h3>
                <c:choose>
                    <c:when test="${user == null || user.role == 'GUEST'}">
                        <p>After registration, you can reserve books online.</p>
                    </c:when>
                    <c:when test="${user.role == 'READER'}">
                        <p>You can reserve books online</p>
                    </c:when>
                    <c:when test="${user.role == 'LIBRARIAN'}">
                        <p>You can manage the library, block users and confirm orders.</p>
                    </c:when>
                    <c:when test="${user.role == 'ADMIN'}">
                        <p>You're ADMIN. You can anything, except ordering books.</p>
                    </c:when>
                </c:choose>
                <p>Now you can search for books by name, sort them by authors, genres and publishers.</p>
            </div>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>