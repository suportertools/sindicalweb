package br.com.rtools.logSistema;

import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
import javax.faces.context.FacesContext;

public class NovoLog extends salvaLogs{

    public String novo(String acao, String obs){
        Usuario user = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
        String iusuario = ": ";
        if (user != null)
            iusuario = iusuario+user.getPessoa().getId()+" "+user.getPessoa().getNome();
        String ihorario = "[horario "+ DataHoje.horaMinuto() +"]";
        String iacao = " >> "+acao;
        String iobs = "# "+obs;
        salvarLinha(ihorario +" "+iusuario+" "+iacao+" "+iobs);
        return null;
    }

}
