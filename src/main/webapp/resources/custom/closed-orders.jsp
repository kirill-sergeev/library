<%@ page contentType="text/javascript" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <fmt:setLocale value="${language}"/>
    <fmt:setBundle basename="text"/>

    $(document)
        .ready(function() {
            $('.ui.form')
                .form()
            ;
        })
    ;

    $('.ui.dropdown')
        .dropdown()
    ;

    $('.ui.checkbox')
        .checkbox()
    ;