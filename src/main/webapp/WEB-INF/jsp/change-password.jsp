<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="change-password"/>
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
            <div class="col-sm-4 col-sm-offset-4">
                <div class="login-form"><!--change form-->
                    <h2><fmt:message key="change-password.section.change"/></h2>
                    <form action="<c:url value="reset.do"/>" method="POST">
                        <input type="hidden" name="token" value="${token}"/>
                        <input type="password" name="password" placeholder="
                            <fmt:message key="change-password.label.password"/>"/>
                        <button type="submit" class="btn btn-default">
                            <fmt:message key="change-password.button.change"/></button>
                    </form>
                </div><!--/change form-->
            </div>
        </div>
    </div>
</section>
<!--/form-->

<%@ include file="../jspf/footer.jspf" %>