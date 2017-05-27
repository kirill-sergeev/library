<%@ page contentType="text/javascript" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <fmt:setLocale value="${language}"/>
    <fmt:setBundle basename="text"/>

    $(document)
        .ready(function () {
            $('.ui.form')
                .form({
                    fields: {
                        password: {
                            identifier: 'password',
                            rules: [
                                {
                                    type: 'length[6]',
                                    prompt: '<fmt:message key="validation.password.length"/>'
                                }
                            ]
                        },
                        confirmation: {
                            identifier: 'retype-password',
                            rules: [
                                {
                                    type: 'match[password]',
                                    prompt: '<fmt:message key="validation.password.match"/>'
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