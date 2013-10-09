/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rtools.utilitarios;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.PdfWriter;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import org.postgresql.util.Base64;

public class PDF {

    public static void arquivoPDF(String args, String filename) throws DocumentException, FileNotFoundException, IOException {
        Document pdfDocument = new Document();
        Reader htmlreader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(pdfDocument, baos);
        pdfDocument.open();
        StyleSheet styles = new StyleSheet();
        styles.loadTagStyle("body", "font", "Bitstream Vera Sans");
        ArrayList arrayElementList = HTMLWorker.parseToList(htmlreader, styles);
        for (int i = 0; i < arrayElementList.size(); ++i) {
            Element e = (Element) arrayElementList.get(i);
            pdfDocument.add(e);
        }
        pdfDocument.close();
        byte[] bs = baos.toByteArray();
        String pdfBase64 = Base64.encodeBytes(bs); //output
        File pdfFile = new File("pdfExample.pdf");
        FileOutputStream out = new FileOutputStream(pdfFile);
        out.write(bs);
        out.close();
    }
}
