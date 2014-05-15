/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.rtools.converter;

import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author rtools
 */
@FacesConverter("fisicaConverter")
public class FisicaConverter  implements Converter {
 
    public static List<Fisica> fisicaDB;
 
    static {
        
    }
 
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("-1")) {
            return new Fisica();
        } else {
            try {
                int number = Integer.parseInt(submittedValue);
                Fisica fisica = (Fisica)(new SalvarAcumuladoDBToplink()).pesquisaCodigo(number, "Fisica");
                return fisica;
//                for (Fisica p : fisicaDB) {
//                    if (p.getId() == number) {
//                        return p;
//                    }
//                }
 
            } catch(NumberFormatException exception) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));
            }
        }
 
        //return null;
    }
 
    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(((Fisica) value).getId());
        }
    }
    
}
