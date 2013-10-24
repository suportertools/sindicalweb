package br.com.rtools.utilitarios;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

public class servletRetorno extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileUploadException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            FileItemFactory fileItemFactory = new DiskFileItemFactory();
            ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
            uploadHandler.setSizeMax(3072 * 3072);
            try {
                List<FileItem> items = uploadHandler.parseRequest(new ServletRequestContext(request));
                for (int i = 0; i < items.size(); i++){
                    upload(items.get(i), request);
                }
            } finally {
            }
        } catch (FileUploadException e) {
            
        }
        response.sendRedirect("/Sindical/retornoBanco.jsf");
    }

    public void upload(FileItem item, HttpServletRequest request) {
        String cliente = "";
        if (request.getSession().getAttribute("sessaoCliente") != null) {
            cliente = (String) request.getSession().getAttribute("sessaoCliente");
        }
        String caminhoA = request.getServletContext().getRealPath("/Cliente/" + cliente + "/Arquivos/retorno/");
        String caminhoB = request.getServletContext().getRealPath("/Cliente/" + cliente + "/Arquivos/retorno/pendentes/");
        File fileA = new File(caminhoA);
        if (!fileA.exists()) {
            fileA.mkdir();
        }
        File fileB = new File(caminhoB);
        if (!fileB.exists()) {
            fileB.mkdir();
        }
        String nomeArq = item.getName();
        String caminho = caminhoB;
        caminho = caminho + "/" + nomeArq;
        try {
            File fl = new File(caminho);
            InputStream in = item.getInputStream();
            FileOutputStream out = new FileOutputStream(fl.getPath());

            byte[] buf = new byte[(int) item.getSize()];
            int count;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (FileUploadException ex) {
            Logger.getLogger(servletUpload.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (FileUploadException ex) {
            Logger.getLogger(servletUpload.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
