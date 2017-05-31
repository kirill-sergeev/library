<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pagename" scope="request" value="register"/>
<%@ include file="../jspf/header.jspf" %>

<div class="ui middle aligned center aligned grid basic segment">
    <div class="column form-column">
        <h2 class="ui teal image header"><fmt:message key="register.header.register"/></h2>
        <form action="<c:url value="/register.do"/>" method="post" class="ui large form">
            <div class="ui stacked segment">
                <div class="field">
                    <div class="ui left icon input">
                        <i class="user icon"></i>
                        <input type="text" name="name" placeholder="<fmt:message key="register.field.name"/>">
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="mail icon"></i>
                        <input type="text" name="email" placeholder="<fmt:message key="register.field.email"/>">
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="lock icon"></i>
                        <input type="password" name="password" placeholder="<fmt:message key="register.field.password"/>">
                    </div>
                </div>
                <div class="ui fluid large teal submit button"><fmt:message key="register.button.register"/></div>
            </div>
            <div class="field">
                <div class="ui checkbox">
                    <input type="checkbox" id="terms">
                    <label for="terms"><fmt:message key="register.checkbox.terms"/></label>
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
            <fmt:message key="register.label.login"/>
            <a href="<c:url value="/login.do"/>"><fmt:message key="register.url.login"/></a>
        </div>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>