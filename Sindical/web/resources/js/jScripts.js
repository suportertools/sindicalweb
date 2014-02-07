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
    $(".cnpjx").mask("99.?999.999/9999-99");
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
    $(".percentual").maskMoney();
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
    });
}

function insereTextoA(valorBotao, idTextarea){
    var textarea = document.getElementById(idTextarea);
    var descricao = valorBotao;
    textarea.value += descricao.value+", ";
}

// Função para adicionar valores dentro do textarea
function insereTexto(valorBotao, idTextarea) {
    //Pega a textarea  
    // form_contrato:idContrato1:inp
    if (document.getElementById(idTextarea) !== null) {
        var textarea = document.getElementById(idTextarea);
    } else if (document.getElementsByClassName(idTextarea) !== null) {
        var textarea = document.getElementsByClassName(idTextarea);
    } else if (document.getElementsByName(idTextarea) !== null) {
        var textarea = document.getElementsByName(idTextarea);
    }
    // form_contrato:idContrato2
    //Texto a ser inserido  
    //var texto = document.getElementById("txtValor").value;
    var texto = valorBotao;
    ////inicio da seleção  
    var selecaoInicial = textarea.selectionStart;
    //final da seleção  
    var selecaoFinal = textarea.selectionEnd;
    //tratamento para Mozilla    
    if (!isNaN(textarea.selectionStart)) {
        selecaoInicial = textarea.selectionStart;
        selecaoFinal = textarea.selectionEnd;
        mozWrap(textarea, texto, '');
        textarea.selectionStart = selecaoInicial + texto.length;
        textarea.selectionEnd = selecaoFinal + texto.length;
    } else if (textarea.createTextRange && textarea.caretPos) {
        if (baseHeight !== textarea.caretPos.boundingHeight) {
            textarea.focus();
            storeCaret(textarea);
        }
        var caret_pos = textarea.caretPos;
        caret_pos.text = caret_pos.texto.charAt(caret_pos.texto.length - 1) === ' ' ? caret_pos.text + text + ' ' : caret_pos.text + text;
        //Para quem não é possível inserir, inserimos no final mesmo (IE...) 
    } else {
        textarea.value = textarea.value + texto;        
    }
}
/* 
 Essa função abre o texto em duas strings e insere o texto bem na posição do cursor, após ele une novamento o texto mas com o texto inserido 
 Essa maravilhosa função só funciona no Mozilla... No IE não temos as propriedades selectionstart, textLength... 
 */
function mozWrap(txtarea, open, close) {
    var selLength = txtarea.textLength;
    var selecaoInicial = txtarea.selectionStart;
    var selecaoFinal = txtarea.selectionEnd;
    var scrollTop = txtarea.scrollTop;

    if (selecaoFinal === 1 || selecaoFinal === 2) {
        selecaoFinal = selLength;                }
    //S1 tem o texto do começo até a posição do cursor  
    var s1 = (txtarea.value).substring(0, selecaoInicial);    
    //S2 tem o texto selecionado  
    var s2 = (txtarea.value).substring(selecaoInicial, selecaoFinal);
    //S3 tem todo o texto selecionado  
    var s3 = (txtarea.value).substring(selecaoFinal, selLength);
    //COloca o texto na textarea. Utiliza a string que estava no início, no meio a string de entrada, depois a seleção seguida da string  
    //de fechamento e por fim o que sobrou após a seleção  
    txtarea.value = s1 + open + s2 + close + s3;
    txtarea.selectionStart = selecaoFinal + open.length + close.length;
    txtarea.selectionEnd = txtarea.selectionStart;
    txtarea.focus();
    txtarea.scrollTop = scrollTop;
    return;
}

function HighlightAll(theField) {
    window.status = "Conteúdo selecionado e copiado para a área de transferência!";
    //theField = theField;
//    therange = theField.createTextRange();
//    therange.execCommand("Copy");
}