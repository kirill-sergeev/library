<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="new-book"/>
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
            <div class="signup-form"><!--input form-->
                <h2>Add a new book</h2>
                <form action="<c:url value="/admin/new-book.do"/>" method="POST">
                    <input type="text" name="bookTitle" placeholder="Title"/>
                    <input type="text" name="isbn" placeholder="ISBN"/>
                    <input type="number" name="quantity" min="0" max="1000" placeholder="Quantity"/>
                    <input type="date" name="publicationDate" placeholder="Publication Date"/>
                    <div class="container1">
                        <div><input type="text" name="authorName1" placeholder="Author"/></div>
                        <button class="add_author">Add Author +</button>
                    </div>
                    <div class="container2">
                        <div><input type="text" name="genreTitle1" placeholder="Genre"/></div>
                        <button class="add_genre">Add Genre +</button>
                    </div>
                    <input type="text" name="publisherTitle" placeholder="Publisher"/>
                    <textarea name="description" cols="30" rows="10" placeholder="Description"></textarea>
                    <button type="submit" class="btn btn-default">Add book</button>
                </form>
            </div><!--/input form-->
        </div>
    </div>

</div>

<%@ include file="../jspf/footer.jspf" %>
