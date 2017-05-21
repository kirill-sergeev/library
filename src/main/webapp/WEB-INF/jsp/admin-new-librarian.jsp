<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="new-librarian"/>
<%@ include file="../jspf/header.jspf" %>
<%@ include file="../jspf/navbar.jspf" %>
<%--@elvariable id="alert" type="com.sergeev.webapp.controller.util.Alert"--%>

<div class="container">
    <div class="col-sm-4 col-sm-offset-4">
        <c:if test="${not empty alert}">
            <div class="alert ${alert.type} alert-dismissable">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                <fmt:message key="${alert.description}"/>
            </div>
        </c:if>
        <div class="row">
            <div class="signup-form"><!--register form-->
                <h2><fmt:message key="login.section.registration"/></h2>
                <form action="<c:url value="/admin/new-librarian.do"/>" method="POST">
                    <input type="text" name="name" placeholder="<fmt:message key="login.label.name"/>"/>
                    <input type="email" name="email" placeholder="<fmt:message key="login.label.username"/>"/>
                    <input type="password" name="password" placeholder="<fmt:message key="login.label.password"/>"/>
                    <button type="submit" class="btn btn-default"><fmt:message key="login.button.register"/></button>
                </form>
            </div><!--/register form-->
        </div>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>
