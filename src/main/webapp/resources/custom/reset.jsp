<%@ page contentType="text/javascript" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <fmt:setLocale value="${language}"/>
    <fmt:setBundle basename="text"/>

    $(document)
        .ready(function () {
            $('.ui.form')
                .form({
                    fields: {
                        email: {
                            identifier: 'email',
                            rules: [
                                {
                                    type: 'email',
                                    prompt: '<fmt:message key="validation.email"/>'
                                }
                            ]
                        }
                    }
                })
            ;
        })
    ;
    $('.ui.checkbox')
        .checkbox()
    ;