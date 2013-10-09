package br.com.rtools.utilitarios;

import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.db.CnaeDB;
import br.com.rtools.pessoa.db.CnaeDBToplink;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class ConverterListShuttle implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null) {
            return null;
        } else {
            CnaeDB db = new CnaeDBToplink();
            int fdf = Integer.parseInt(value);
            Cnae cnae = db.pesquisaCodigo(Integer.parseInt(value));
            return cnae;
        }
    }

    public String getAsString(FacesContext facesContext, UIComponent uIComponent, Object object) {
        if (object == null) {
            return null;
        } else {
            String dd = String.valueOf(((Cnae) object).getId());
            return String.valueOf(((Cnae) object).getId());
        }
    }
}