<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pagename" scope="request" value="books"/>
<%@ include file="../jspf/header.jspf" %>
<%@ include file="../jspf/navbar.jspf" %>

<div class="container">
    <div class="row">
        <div class="col-8 col-offset-2">
            <table class="table table-sm table-bordered sortable">
                <thead>
                <tr>
                    <th>id</th>
                    <th>Title</th>
                    <th>Authors</th>
                    <th>Genres</th>
                    <th>Publisher</th>
                    <th>Publication</th>
                    <th>ISBN</th>
                    <th>Available</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <%--@elvariable id="books" type="java.util.List<ua.nure.serhieiev.library.model.entities.Book>"--%>
                <c:forEach items="${books}" var="book">
                    <tr>
                        <th scope="row">${book.title}</th>
                        <th scope="row">${book.id}</th>
                        <td>
                            <c:forEach items="${book.authors}" var="author" varStatus="status">
                                ${author.name}<c:if test="${not status.last}">,</c:if>
                            </c:forEach>
                        </td>
                        <td>
                            <c:forEach items="${book.genres}" var="genre" varStatus="status">
                                ${genre.title}<c:if test="${not status.last}">,</c:if>
                            </c:forEach>
                        </td>
                        <td>${book.publisher.title}</td>
                        <td>${book.publicationDate}</td>
                        <td>${book.isbn}</td>
                        <td>${book.available}</td>
                        <td>
                            <div class="btn-group">
                                <form action="<c:url value="/cart.do"/>" method="post">
                                    <input type="hidden" name="book" value="${book.id}"/>
                                    <jsp:useBean id="localCart" scope="session" type="java.util.Map<java.lang.Integer, java.time.LocalDateTime>"/>
                                    <c:if test="${empty localCart.get(book.id)}">
                                        <button type="submit" name="button" value="add" class="btn btn-danger">Add
                                        </button>
                                    </c:if>
                                    <c:if test="${not empty localCart.get(book.id)}">
                                        <button type="submit" name="button" value="remove" class="btn btn-danger">
                                            Remove
                                        </button>
                                    </c:if>
                                    <button type="submit" name="button" value="clear" class="btn btn-danger">Clear
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <c:if test="${not empty author}"><c:set var="group" scope="page" value="author=${author}&"/></c:if>
    <c:if test="${not empty genre}"><c:set var="group" scope="page" value="genre=${genre}&"/></c:if>
    <c:if test="${not empty publisher}"><c:set var="group" scope="page" value="publisher=${publisher}&"/></c:if>
    <c:set var="action" scope="page" value="books.do"/>
    <%@ include file="../jspf/pagination.jspf" %>
</div>

<%@ include file="../jspf/footer.jspf" %>
