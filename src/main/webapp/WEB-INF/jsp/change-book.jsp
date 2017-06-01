<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="change-book"/>
<%@ include file="../jspf/header.jspf" %>
<jsp:useBean id="book" scope="request" type="ua.nure.serhieiev.library.model.entities.Book"/>

<%--@elvariable id="alert" type="ua.nure.serhieiev.library.controller.util.Alert"--%>

<div class="ui three column center aligned grid basic segment">
    <div class="column form-column">
        <div class="center aligned row">
            <h2 class="ui header">Change book</h2>
            <form action="<c:url value="/change-book.do"/>" method="post" class="ui large form">
                <div class="ui stacked segment">
                    <input type="hidden" name="id" value="${book.id}">
                    <div class="field">
                        <div class="ui left icon input">
                            <i class="text width icon"></i>
                            <input type="text" name="bookTitle" placeholder="Title" value="${book.title}">
                        </div>
                    </div>
                    <div class="field">
                        <div class="ui left icon input">
                            <i class="info icon"></i>
                            <input type="text" name="isbn" placeholder="ISBN" value="${book.isbn}">
                        </div>
                    </div>
                    <div class="field">
                        <div class="ui left icon input">
                            <i class="line chart icon"></i>
                            <input type="text" name="quantity" placeholder="Quantity" value="${book.quantity}">
                        </div>
                    </div>
                    <div class="field">
                        <div class="ui left icon input">
                            <i class="line chart icon"></i>
                            <input type="text" name="available" placeholder="Available" value="${book.available}">
                        </div>
                    </div>
                    <div class="field">
                        <div class="ui calendar datepicker">
                            <div class="ui input left icon">
                                <i class="calendar icon"></i>
                                <input type="text" name="publicationDate" placeholder="Publication Date" value="${book.publicationDate}">
                            </div>
                        </div>
                    </div>
                    <div class="field">
                        <div class="ui left icon input">
                            <i class="registered icon"></i>
                            <input type="text" name="publisherTitle" placeholder="Publisher" value="${book.publisher.title}">
                        </div>
                    </div>
                    <div class="authors">
                        <c:forEach items="${book.authors}" var="author" varStatus="status">
                            <div class="field">
                                <div class="ui left icon action input">
                                    <i class="user icon"></i>
                                    <input type="text" name="authorName${status.count}" placeholder="Author" value="${author.name}">
                                    <c:if test="${status.count == 1}">
                                    <button class="ui icon button addAuthor"><i class="green plus icon"></i></button>
                                    </c:if>
                                    <c:if test="${status.count > 1}">
                                        <button class="ui icon button deleteAuthor"><i class="red minus icon"></i></button>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <br>
                    <div class="genres">
                        <c:forEach items="${book.genres}" var="genre" varStatus="status">
                            <div class="field">
                                <div class="ui left icon action input">
                                    <i class="tag icon"></i>
                                    <input type="text" name="genreTitle${status.count}" placeholder="Genre" value="${genre.title}">
                                    <c:if test="${status.count == 1}">
                                <button class=" ui icon button addGenre"><i class="green plus icon"></i></button>
                                    </c:if>
                                    <c:if test="${status.count > 1}">
                                        <button class="ui icon button deleteGenre"><i class="red minus icon"></i></button>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <br>
                    <div class="field">
                        <textarea name="description" placeholder="Description" rows="5">${book.description}</textarea>
                    </div>
                    <div class="ui large green submit button">Change book</div>
                </div>
                <div class="ui error message"></div>
                <c:if test="${not empty alert}">
                    <div class="ui message ${alert.type}">
                        <ul class="list">
                            <li><fmt:message key="${alert.description}"/></li>
                        </ul>
                    </div>
                </c:if>
            </form>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        var max_fields = 5;
        var wrapper = $(".authors");
        var add_button = $(".addAuthor");
        var x = ${book.authors.size()};
        $(add_button).click(function (e) {
            e.preventDefault();
            if (x < max_fields) {
                x++;
                $(wrapper).append('<div class="field"><div class="ui left icon action input"><i class="user icon"></i><input type="text" name="authorName[]" placeholder="Author"><button class="ui icon button deleteAuthor"><i class="red minus icon"></i></button></div></div>'.replace("[]", x + ''));
            }
        });
        $(wrapper).on("click", ".deleteAuthor", function (e) {
            e.preventDefault();
            $(this).parent('div').remove();
            x--;
        })
    });

    $(document).ready(function () {
        var max_fields = 10;
        var wrapper = $(".genres");
        var add_button = $(".addGenre");
        var x = ${book.genres.size()};
        $(add_button).click(function (e) {
            e.preventDefault();
            if (x < max_fields) {
                x++;
                $(wrapper).append('<div class="field"><div class="ui left icon action input"><i class="tag icon"></i><input type="text" name="genreTitle[]" placeholder="Genre"><button class="ui icon button deleteGenre"><i class="red minus icon"></i></button></div></div>'.replace("[]", x + ''));
            }
        });
        $(wrapper).on("click", ".deleteGenre", function (e) {
            e.preventDefault();
            $(this).parent('div').remove();
            x--;
        })
    });

</script>


<%@ include file="../jspf/footer.jspf" %>
