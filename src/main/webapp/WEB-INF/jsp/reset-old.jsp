<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="reset"/>
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
                <div class="login-form"><!--reset form-->
                    <h2><fmt:message key="reset.section.reset"/></h2>
                    <form action="<c:url value="reset.do"/>" method="POST">
                        <input type="email" name="email" placeholder="<fmt:message key="reset.label.email"/>"/>
                        <button type="submit" class="btn btn-default"><fmt:message key="reset.button.reset"/></button>
                    </form>
                </div><!--/reset form-->
            </div>
        </div>
    </div>
</section>
<!--/form-->

<%@ include file="../jspf/footer.jspf" %>