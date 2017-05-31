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

    $('.ui.dropdown')
        .dropdown()
    ;

    $('.ui.checkbox')
        .checkbox()
    ;

    $('.datepicker').calendar(
        {
            type: 'date',
            firstDayOfWeek: 1,
            formatter: {
                date: function (date, settings) {
                    if (!date) return '';
                    return date.toLocaleString('en-us', {
                        year: 'numeric',
                        month: '2-digit',
                        day: '2-digit'
                    }).replace(/(\d+)\/(\d+)\/(\d+)/, '$3-$1-$2');
                }
            }
        }
    );