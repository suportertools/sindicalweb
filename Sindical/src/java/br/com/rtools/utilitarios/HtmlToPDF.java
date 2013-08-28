package br.com.rtools.utilitarios;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

/**
 * http://javafree.uol.com.br/topic-866449-Converta-HTML-para-PDF-com-06-linhas-de-Codigo.html
 * OutputStream os = new FileOutputStream("C:\hello.pdf");
 * Html2Pdf.convert("<h1 style=\"color:red\">Hello PDF</h1>", os);
 * os.close();
 */  
public class HtmlToPDF {  

    public static void convert(String input, OutputStream out) throws DocumentException{  
        convert(new ByteArrayInputStream(input.getBytes()), out);  
    }  

    public static void convert(InputStream input, OutputStream out) throws DocumentException{
        Tidy tidy = new Tidy();
        Document doc = tidy.parseDOM(input, null);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(doc, null);
        renderer.layout();
        renderer.createPDF(out);
    }
}