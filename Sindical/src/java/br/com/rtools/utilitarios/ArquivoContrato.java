package br.com.rtools.utilitarios;

import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class ArquivoContrato {

    public static void lerContrato() {
        FacesContext context = FacesContext.getCurrentInstance();
        String caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/ArquivoContrato/");
        File fl = new File(caminho + "/contrato01.odt");
        try {
            FileInputStream fileInput = new FileInputStream(fl);
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(fileInput, "ISO-8859-1"));
//            String linha = "";
//            String xxxxxxxxx = "";
//            while((linha = buffReader.readLine()) != null){
//                xxxxxxxxx = linha;
//            }
            gerarPdf(fl.getPath(), "C:/ContratoModificado.pdf");
            Desktop.getDesktop().open(fl);
        } catch (Exception e) {
        }
    }

    public static void gerarPdf(String inputFile, String outputFile) {

        // create a new document
        Document document = new Document();

        // create a PDF writer to save the new document to disk
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputFile));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
        }

        // open the document for modifications
        document.open();

        // create a new parser to load the RTF file
        //RtfParser parser = new RtfParser(null);

        // read the rtf file into a compatible document
//        try {   
//            parser.convertRtfDocument(new FileInputStream(inputFile), document);
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//        }

        // save the pdf to disk
        document.close();
    }
}
