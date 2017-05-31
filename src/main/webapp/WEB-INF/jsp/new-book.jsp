<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pagename" scope="request" value="new-book"/>
<%@ include file="../jspf/header.jspf" %>

<div class="ui three column center aligned grid basic segment">
    <div class="column">
        <div class="center aligned row">
        <h2 class="ui header">New book</h2>
        <form action="<c:url value="/new-book.do"/>" method="post" class="ui large form">
            <div class="ui stacked segment">
                <div class="field">
                    <div class="ui left icon input">
                        <i class="text width icon"></i>
                        <input type="text" name="bookTitle" placeholder="Title">
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="info icon"></i>
                        <input type="text" name="isbn" placeholder="ISBN">
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="line chart icon"></i>
                        <input type="text" name="quantity" placeholder="Quantity">
                    </div>
                </div>
                <div class="field">
                    <div class="ui calendar datepicker">
                        <div class="ui input left icon">
                            <i class="calendar icon"></i>
                            <input type="text" name="publicationDate" placeholder="Publication Date">
                        </div>
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="registered icon"></i>
                        <input type="text" name="publisherTitle" placeholder="Publisher">
                    </div>
                </div>
                <div class="authors">
                    <div class="field">
                        <div class="ui left icon action input">
                            <i class="user icon"></i>
                            <input type="text" name="authorName1" placeholder="Author">
                            <button class="ui icon button addAuthor"><i class="green plus icon"></i></button>
                        </div>
                    </div>
                </div>
                <br>
                <div class="genres">
                    <div class="field">
                        <div class="ui left icon action input">
                            <i class="tag icon"></i>
                            <input type="text" name="genreTitle1" placeholder="Genre">
                            <button class="ui icon button addGenre"><i class="green plus icon"></i></button>
                        </div>
                    </div>
                </div>
                <br>
                <div class="field">
                    <textarea name="description" placeholder="Description" rows="5"></textarea>
                </div>
                <div class="ui large green submit button">Add book</div>
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

<script>
    $(document).ready(function () {
        var max_fields = 5;
        var wrapper = $(".authors");
        var add_button = $(".addAuthor");
        var x = 1;
        $(add_button).click(function (e) {
            e.preventDefault();
            if (x < max_fields) {
                x++;
                $(wrapper).append('<div class="field"><div class="ui left icon action input"><i class="user icon"></i><input type="text" name="authorName[]" placeholder="Author"><button class="ui icon button deleteAuthor"><i class="red minus icon"></i></button></div></div>'.replace("[]", x+''));
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
        var x = 1;
        $(add_button).click(function (e) {
            e.preventDefault();
            if (x < max_fields) {
                x++;
                $(wrapper).append('<div class="field"><div class="ui left icon action input"><i class="tag icon"></i><input type="text" name="genreTitle[]" placeholder="Genre"><button class="ui icon button deleteGenre"><i class="red minus icon"></i></button></div></div>'.replace("[]", x+''));
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
