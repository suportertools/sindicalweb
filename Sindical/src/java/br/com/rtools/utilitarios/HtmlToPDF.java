package br.com.rtools.utilitarios;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.lowagie.text.html.simpleparser.HTMLWorker;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

public class HtmlToPDF {

    public static void convert(String input, OutputStream out) throws IOException, DocumentException {
        convert(new ByteArrayInputStream(input.getBytes()), out);
    }

    public static void convert(InputStream input, OutputStream out) throws IOException, DocumentException {
        
        // USANDO --  HTMLWorker -- Nativo
        //String k = "<html><body> This is my Project </body></html>";
        //OutputStream file = new FileOutputStream(new File("D:\\Test.pdf"));
//        Document document = new Document();
//        PdfWriter pdf  = PdfWriter.getInstance(document, out);
//        document.open();
//        HTMLWorker htmlWorker = new HTMLWorker(document);
//        
//
//        htmlWorker.parse(new StringReader(input.toString()));
//        document.close();
        
        
        // USANDO --  XMLWorker -- BAIXAR LIB http://sourceforge.net/projects/xmlworker/
        
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();

        XMLWorkerHelper.getInstance().parseXHtml(writer, document, input);

        document.close();
        
        
//        Document document = new Document();
//        PdfWriter pdf  = PdfWriter.getInstance(document, out);
//        document.open();
//        
//        //XMLWorkerHelper.getInstance().parseXHtml(pdf, document, input);
//        XMLWorkerHelper.getInstance().parseXHtml(PdfWriter., document, input);
//        document.close();
        
//        try {
//            String k = "<html><body> This is my Project </body></html>";
//            OutputStream file = new FileOutputStream(new File("C:\\Test.pdf"));
//            Document document = new Document();
//            PdfWriter writer = PdfWriter.getInstance(document, file);
//            document.open();
//            InputStream is = new ByteArrayInputStream(k.getBytes());
//            XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
//            document.close();
//            file.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }        
    }
}
//        try{
//            Tidy tidy = new Tidy();
//            Document doc = tidy.parseDOM(input, null);
//            ITextRenderer renderer = new ITextRenderer();
//            renderer.setDocument(doc, null);
//            renderer.layout();
//            renderer.createPDF(out);
//        }catch(DocumentException e){
//            e.printStackTrace();
//        }