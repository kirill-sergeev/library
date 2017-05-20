<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pagename" scope="request" value="error"/>
<%@ include file="../jspf/header.jspf" %>

<div class="container text-center">
    <div class="logo-404">
        <a href="<c:url value="/"/>"><img src="../../resources/images/home/logo.png" alt="" /></a>
    </div>
    <div class="content-404">
        <img src="../../resources/images/404/404.png" class="img-responsive" alt="" />
        <h1><b>OPPS!</b> We Couldn’t Find this Page</h1>
        <p>Uh... So it looks like you brock something. The page you are looking for has up and Vanished.</p>
        <h2><a href="<c:url value="/"/>">Bring Me Back Home</a></h2>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>
