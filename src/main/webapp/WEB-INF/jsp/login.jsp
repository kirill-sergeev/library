<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="login"/>
<%@ include file="../jspf/header.jspf" %>

<div class="ui center aligned grid basic segment">
    <div class="column form-column">
        <h2 class="ui teal image header"><fmt:message key="login.header.login"/></h2>
        <form action="<c:url value="/login.do"/>" method="post" class="ui large form">
            <div class="ui stacked segment">
                <div class="field">
                    <div class="ui left icon input">
                        <i class="user icon"></i>
                        <input type="text" name="email" placeholder="<fmt:message key="login.field.email"/>">
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="lock icon"></i>
                        <input type="password" name="password" placeholder="<fmt:message key="login.field.password"/>">
                    </div>
                </div>
                <div class="ui fluid large teal submit button"><fmt:message key="login.button.login"/></div>
            </div>
            <div class="inline field">
                <div class="field">
                    <div class="ui checkbox">
                        <input type="checkbox" id="remember" name="remember" value="true">
                        <label for="remember"><fmt:message key="login.checkbox.remember"/></label>
                    </div>
                </div>
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
            <fmt:message key="login.label.new"/><a href="<c:url value="/register.do"/>"> <fmt:message key="login.url.register"/></a>
        </div>
        <div class="ui message">
            <fmt:message key="login.label.forgot"/><a href="<c:url value="/reset.do"/>"> <fmt:message key="login.url.reset"/></a>
        </div>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>