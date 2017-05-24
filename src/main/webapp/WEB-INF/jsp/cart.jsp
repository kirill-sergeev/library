<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pagename" scope="request" value="cart"/>
<%@ include file="../jspf/header.jspf" %>
<%@ include file="../jspf/navbar.jspf" %>

<div class="container">
    <div class="row">
        <div class="col-8 col-offset-2">
            <table class="table table-sm table-bordered sortable">
                <thead>
                <tr>
                    <th>Title</th>
                </tr>
                </thead>
                <tbody>
                <%--<jsp:useBean id="cart" scope="session" type="java.util.Map<java.lang.Integer, java.time.LocalDateTime>"/>--%>
                <c:forEach items="${books}" var="book">
                    <tr>
                        <th scope="row">${book.title}</th>
                    </tr>
                </c:forEach>
                </tbody>
            </table>



            global
            <table class="table table-sm table-bordered sortable">
                <thead>
                <tr>
                    <th>Title</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${globalCart}" var="b">
                    <tr>
                        <th scope="row">${b}</th>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <div class="btn-group">
                <form action="<c:url value="/cart.do"/>" method="post">
                    <jsp:useBean id="localCart" scope="session" type="java.util.Map<java.lang.Integer, java.time.LocalDateTime>"/>
                    <c:if test="${not empty localCart}">
                        <button type="submit" name="button" value="clear" class="btn btn-danger">Clear</button>
                    </c:if>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>
