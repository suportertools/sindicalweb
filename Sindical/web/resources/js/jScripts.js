$(document).keydown(function(e) {
    if (e.which === 27) {
        fecharModal();
        return false;
    }
});

function modalOpcao(painel) {
    var maskHeight = $(document).height();
    var maskWidth = $(window).width();

    $('#mask').css({'width': maskWidth, 'height': maskHeight});

    $('#mask').fadeIn(100);
    $('#mask').fadeTo("slow", 0.8);

    var winH = $(window).height();
    var winW = $(window).width();

    $('#' + painel).css('top', winH / 2 - $('#' + painel).height() / 2);
    $('#' + painel).css('left', winW / 2 - $('#' + painel).width() / 2);

    $('#' + painel).fadeIn(2000);

    $('a[lang=close], .closeModal').click(function(e) {
        e.preventDefault();

        $('#mask').hide();
        $('.window').hide();
    });

    $('#mask').click(function() {
        $(this).hide();
        $('.window').hide();
    });
}

function fecharModal() {
    $('#mask').hide();
    $('.window').hide();
}

function fecharModalOpcao(painel, fundo) {
    $('#' + painel).hide();
    if (!fundo)
        $('#mask').hide();
}

function modalMensagem() {
    $(document).ready(function() {
        var painel = 'divMensagem';
        var maskHeight = $(document).height();
        var maskWidth = $(window).width();

        $('#mask').css({'width': maskWidth, 'height': maskHeight});

        $('#mask').fadeIn(80);
        $('#mask').fadeTo("slow", 0.8);

        var winH = $(window).height();
        var winW = $(window).width();

        $('#' + painel).css('top', winH / 2 - $('#' + painel).height() / 2);
        $('#' + painel).css('left', winW / 2 - $('#' + painel).width() / 2);

        $('#' + painel).fadeIn(2000);

        $('a[lang=close], .closeModal').click(function(e) {
            e.preventDefault();

            $('#mask').hide();
            $('.window').hide();
        });

        $('#mask').click(function() {
            $(this).hide();
            $('.window').hide();
        });
    });
}

jQuery(function($) {
    $(".idFazer").Watermark("O que quero fazer Agora?");
});

function setOpacity(id, valor) {
    document.getElementById(id).style.opacity = valor / 10;
    document.getElementById(id).style.filter = 'alpha(opacity=' + valor * 10 + ')';
}

// $(".telefone").mask("(99) 9999-9999?9");
jQuery(function($) {
    $(".data").mask("99/99/9999");
    $(".dataAno").mask("9999");
    $(".hora").mask("99:99");
    $(".telefone").focus(function () { $(this).mask("(99) 9999-9999?9"); }); 
    $(".telefone").focusout(function () { var phone, element; element = $(this); element.unmask(); phone = element.val().replace(/\D/g, ''); if (phone.length > 10) { element.mask("(99) 99999-999?9"); } else { element.mask("(99) 9999-9999?9"); } });
    $(".telefoneSimples").focus(function () { $(this).mask("9999-9999?9"); }); 
    $(".telefoneSimples").focusout(function () { var phone, element; element = $(this); element.unmask(); phone = element.val().replace(/\D/g, ''); if (phone.length > 8) { element.mask("99999-999?9"); } else { element.mask("9999-9999?9"); } });
    $(".cpf").mask("999.999.999-99");
    $(".cnpj").mask("99.999.999/9999-99");
    $(".cei").mask("99.999.99999/99");
    $(".cnae").mask("9999-9/99");
    $(".mac").mask("**-**-**-**-**-**");
    $(".referencia").mask("99/9999");
    $(".locadoraBarras").mask("9999999999999");
    $(".rg").mask("99.999.999?**");
    $(".cep").mask("99999-999");
});

jQuery(function($) {
    $(".real").maskMoney({showSymbol: true, decimal: ",", thousands: "."});
});

jQuery(function($) {
    $(".realPonto").maskMoney({showSymbol: true, decimal: ".", thousands: ","});
});

jQuery(function($) {
    $(".realSemSimbolo").maskMoney({showSymbol: false, decimal: ",", thousands: "."});
});
    

function somenteNumeros(setThis) {
    setThis.value = setThis.value.replace(/\D/g, '');
    return setThis;
}

function somenteNumerosTelefone(setThis) {
    setThis.value = setThis.value.replace(/\D/g, '');
    return setThis;
}

function somenteLetras(setThis) {
    /* 
     para não aceitar números use: 0-9
     para não aceitar letras use: a-z
     */
    setThis.value = setThis.value.replace(/([0-9])/g, '');
}

function upperCase(setThis) {
    setThis.value = setThis.value.toUpperCase();
    return setThis;
}

function lowerCase(setThis) {
    setThis.value = setThis.value.toLowerCase();
    return setThis;
}

function marcarTodosCheckbox() {
    $(".todosCheckbox").each(function() {
        if (!this.checked) {
            $(this).attr("checked", "checked");
        } else {
            $(this).removeAttr("checked");
        }
    })
}
