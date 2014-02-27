package br.com.rtools.utilitarios;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HtmlToPDF {

    public static void convert(String input, OutputStream out) throws IOException, DocumentException {
        convert(new ByteArrayInputStream(input.getBytes()), out);
    }

    // ANTIGO HtmlToPDF
    public static void convert(InputStream input, OutputStream out) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, input);
        document.close();      
    }
}