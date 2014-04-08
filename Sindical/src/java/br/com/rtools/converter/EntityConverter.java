package br.com.rtools.converter;

import br.com.rtools.utilitarios.BaseEntity;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("entityConverter")
public class EntityConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
        if (value != null) {
            return this.getAttributesFrom(component).get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent component, Object value) {

        if (value != null
                && !"".equals(value)) {

            BaseEntity entity = (BaseEntity) value;

            // adiciona item como atributo do componente  
            this.addAttribute(component, entity);

            int codigo = entity.getId();
            if (codigo != -1) {
                return String.valueOf(codigo);
            }
        }

        return (String) value;
    }

    protected void addAttribute(UIComponent component, BaseEntity o) {
        String key = Integer.toString(o.getId()); // codigo da empresa como chave neste caso  
        this.getAttributesFrom(component).put(key, o);
    }

    protected Map<String, Object> getAttributesFrom(UIComponent component) {
        return component.getAttributes();
    }
}
