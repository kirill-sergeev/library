<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pagename" scope="request" value="new-book"/>
<%@ include file="../jspf/header.jspf" %>

<div class="ui three column center aligned grid basic segment">
    <div class="column">
        <div class="center aligned row">
        <h2 class="ui teal image header">New book</h2>
        <form action="<c:url value="/new-book.do"/>" method="post" class="ui large form">
            <div class="ui stacked segment">
                <div class="field">
                    <div class="ui left icon input">
                        <i class="user icon"></i>
                        <input type="text" name="title" placeholder="Title">
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="user icon"></i>
                        <input type="text" name="isbn" placeholder="ISBN">
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="user icon"></i>
                        <input type="text" name="quantity" placeholder="Quantity">
                    </div>
                </div>
                <div class="field">
                    <div class="ui calendar datepicker">
                        <div class="ui input left icon">
                            <i class="calendar icon"></i>
                            <input type="text" placeholder="Publication Date">
                        </div>
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="user icon"></i>
                        <input type="text" name="publisherTitle" placeholder="Publisher">
                    </div>
                </div>
                <div class="field">
                        <textarea name="description" placeholder="Description" rows="5"></textarea>
                </div>
                <div class="ui fluid large teal submit button">Add book</div>
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
</div>

<%@ include file="../jspf/footer.jspf" %>
