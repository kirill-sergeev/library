<%@ page contentType="text/javascript" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <fmt:setLocale value="${language}"/>
    <fmt:setBundle basename="text"/>

$(document)
    .ready(function() {

        // fix menu when passed
        $('.masthead')
            .visibility({
                once: false,
                onBottomPassed: function() {
                    $('.fixed.menu').transition('fade in');
                },
                onBottomPassedReverse: function() {
                    $('.fixed.menu').transition('fade out');
                }
            })
        ;

        // create sidebar and attach to menu open
        $('.ui.sidebar')
            .sidebar('attach events', '.toc.item')
        ;

    })
;