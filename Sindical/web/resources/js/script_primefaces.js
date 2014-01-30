jQuery(function($) {
    // Máscara de telefone sem DDD
    $(".telefone").focus(function() {
        $(this).mask("9999-9999?9");
    });
    $(".telefone").focusout(function() {
        var phone, element;
        element = $(this);
        element.unmask();
        phone = element.val().replace(/\D/g, '');
        if (phone.length > 8) {
            element.mask("99999-999?9");
        } else {
            element.mask("9999-9999?9");
        }
    });
});
    