<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="login"/>
<%@ include file="../jspf/header.jspf" %>
<%@ include file="../jspf/navbar.jspf" %>
<%--@elvariable id="alert" type="com.sergeev.webapp.controller.util.Alert"--%>

<section id="form"><!--form-->
    <div class="container">
        <c:if test="${not empty alert}">
            <div class="alert ${alert.type} alert-dismissable">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                <fmt:message key="${alert.description}"/>
            </div>
        </c:if>
        <div class="row">
            <div class="col-sm-4 col-sm-offset-1">
                <div class="login-form"><!--login form-->
                    <h2><fmt:message key="login.section.login"/></h2>
                    <form action="<c:url value="login.do"/>" method="POST">
                        <input type="email" name="email" placeholder="<fmt:message key="login.label.username"/>"/>
                        <input type="text" name="password" placeholder="<fmt:message key="login.label.password"/>"/>
                        <span><input type="checkbox" class="checkbox" name="remember" value="true">
                            <fmt:message key="login.checkbox.remember"/>
                        </span>
                        <button type="submit" class="btn btn-default"><fmt:message key="login.button.login"/></button>
                    </form>
                </div><!--/login form-->
            </div>
            <div class="col-sm-1">
                <h2 class="or"><fmt:message key="login.text.or"/></h2>
            </div>
            <div class="col-sm-4">
                <div class="signup-form"><!--register form-->
                    <h2><fmt:message key="login.section.registration"/></h2>
                    <form action="<c:url value="register.do"/>" method="POST">
                        <input type="text" name="name" placeholder="<fmt:message key="login.label.name"/>"/>
                        <input type="email" name="email" placeholder="<fmt:message key="login.label.username"/>"/>
                        <input type="password" name="password" placeholder="<fmt:message key="login.label.password"/>"/>
                        <button type="submit" class="btn btn-default"><fmt:message key="login.button.register"/></button>
                    </form>
                </div><!--/register form-->
            </div>
        </div>
    </div>
</section>
<!--/form-->

<%@ include file="../jspf/footer.jspf" %>
