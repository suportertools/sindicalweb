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
    $(".ddd_telefone").focus(function() {
        $(this).mask("(99) 9999-9999?9");
    });
    $(".ddd_telefone").focusout(function() {
        var phone, element;
        element = $(this);
        element.unmask();
        phone = element.val().replace(/\D/g, '');
        if (phone.length > 10) {
            element.mask("(99) 99999-999?9");
        } else {
            element.mask("(99) 9999-9999?9");
        }
    });
    $(".data").blur(function() {
        $(this).mask("99/99/9999");
    });
});


function selectInputText(setthis) {
    setthis.focus();
    setthis.select();
    return false;
}

function listener(data) {
    var status = data.status; // Can be "begin", "complete" or "success".

    switch (status) {
        case "begin": // Before the ajax request is sent.
            // ...
            break;

        case "complete": // After the ajax response is arrived.
            $('.testexxx').click();
            break;

        case "success": // After update of HTML DOM based on ajax response..            
            break;
    }
}