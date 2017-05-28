<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="reset"/>
<%@ include file="../jspf/header.jspf" %>

<div class="ui middle aligned center aligned grid basic segment">
    <div class="column">
        <h2 class="ui teal image header"><fmt:message key="reset.header.reset"/></h2>
        <form action="<c:url value="reset.do"/>" method="post" class="ui large form">
            <div class="ui stacked segment">
                <div class="field">
                    <div class="ui left icon input">
                        <i class="user icon"></i>
                        <input type="text" name="email" placeholder="<fmt:message key="reset.field.email"/>">
                    </div>
                </div>
                <div class="ui fluid large teal submit button"><fmt:message key="reset.button.reset"/></div>
            </div>
            <div class="ui error message"></div>
            <%--@elvariable id="alert" type="ua.nure.serhieiev.library.controller.util.Alert"--%>
            <c:if test="${not empty alert}">
                <div class="ui message ${alert.type}">
                    <ul class="list"><li><fmt:message key="${alert.description}"/></li></ul>
                </div>
            </c:if>
        </form>
        <div class="ui message">
            <fmt:message key="reset.label.remember"/><a href="<c:url value="/login.do"/>"><fmt:message key="reset.url.login"/></a>
        </div>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>