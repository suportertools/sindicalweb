//    function numerosBanco(campo){
//        var digits="0123456789.-/"
//        var campo_temp
//        for (var i=0;i<campo.value.length;i++){
//          campo_temp=campo.value.substring(i,i+1)
//          if (digits.indexOf(campo_temp)==-1){
//                campo.value = campo.value.substring(0,i);
//                break;
//           }
//        }
//    }
//
//    Effect.Transitions.Elastic = function(pos) {
//        return -1*Math.pow(4,-2*pos) * Math.sin((pos*3-1)*(3*Math.PI)/2) + 1;
//    };
//
//    function DataHora(evento, objeto){
//        var keypress=(window.event)?event.keyCode:evento.which;
//        campo = eval (objeto);
//        if (campo.value == '00/00/0000 00:00:00')
//        {
//            campo.value=""
//        }
//
//        caracteres = '0123456789';
//        separacao1 = '/';
//        separacao2 = ' ';
//        separacao3 = ':';
//        conjunto1 = 2;
//        conjunto2 = 5;
//        conjunto3 = 10;
//        conjunto4 = 13;
//        conjunto5 = 16;
//        if ((caracteres.search(String.fromCharCode (keypress))!=-1) && campo.value.length < (19))
//        {
//            if (campo.value.length == conjunto1 )
//            campo.value = campo.value + separacao1;
//            else if (campo.value.length == conjunto2)
//            campo.value = campo.value + separacao1;
//            else if (campo.value.length == conjunto3)
//            campo.value = campo.value + separacao2;
//            else if (campo.value.length == conjunto4)
//            campo.value = campo.value + separacao3;
//            else if (campo.value.length == conjunto5)
//            campo.value = campo.value + separacao3;
//        }
//        else
//            event.returnValue = false;
//    }