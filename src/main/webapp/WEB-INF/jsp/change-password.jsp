<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="change-password"/>
<%@ include file="../jspf/header.jspf" %>

<div class="ui middle aligned center aligned grid segment">
    <div class="column">
        <h2 class="ui teal image header"><fmt:message key="change-password.header.change"/></h2>
        <form action="<c:url value="/register.do"/>" method="post" class="ui large form">
            <div class="ui stacked segment">
                <div class="field">
                    <div class="ui left icon input">
                        <i class="lock icon"></i>
                        <%--@elvariable id="token" type="java.lang.String"--%>
                        <input type="hidden" name="token" value="${token}"/>
                        <input type="password" name="password" placeholder="<fmt:message key="change-password.field.password"/>">
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="lock icon"></i>
                        <input type="password" name="retype-password" placeholder="<fmt:message key="change-password.field.retype"/>">
                    </div>
                </div>
                <div class="ui fluid large teal submit button"><fmt:message key="change-password.button.change"/></div>
            </div>
            <div class="ui error message"></div>
            <%--@elvariable id="alert" type="ua.nure.serhieiev.library.controller.util.Alert"--%>
            <c:if test="${not empty alert}">
                <div class="ui message ${alert.type}">
                    <ul class="list"><li><fmt:message key="${alert.description}"/></li></ul>
                </div>
            </c:if>
        </form>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>