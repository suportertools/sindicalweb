package br.com.rtools.utilitarios;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

public class servletEnvioArquivoContribuinte extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileUploadException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
            // fileItemFactory.setSizeThreshold(1 * 3072 * 3072); //1 MB  

            ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
            // uploadHandler.setSizeMax(3072 * 3072);  
            try {
                List items = uploadHandler.parseRequest(request);
                Iterator itr = items.iterator();
                int i = 0;
                while (itr.hasNext()) {
                    FileItem item = (FileItem) itr.next();
                    upload(item, request);
                    i++;
                    //if (!item.isFormField()) {  
                    //    int size = item.getInputStream().available();  
                    //    bs = new byte[size];  
                    //    item.getInputStream().read(bs);  
                    //}  
                }
            } finally {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //  response.sendRedirect((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno"));
        response.sendRedirect("/Sindical/enviarArquivosContribuinte.jsf");
        //response.sendRedirect("/Sindical/socios.jsf");
    }

    public void upload(FileItem item, HttpServletRequest request) {
        if (item.getName() == null) {
            return;
        }
        String cliente = "";
        String tipoEnvio = "ArquivoContribuinte";
        String caminho = "";
        if (request.getSession().getAttribute("sessaoCliente") != null) {
            cliente = (String) request.getSession().getAttribute("sessaoCliente");
        }
        if (tipoEnvio.equals("ArquivoContribuinte") || tipoEnvio.equals("ArquivoContabilidade")) {
            caminho = request.getServletContext().getRealPath("/Cliente/" + cliente + "/Arquivos/Anexos/");
            if (!new File(caminho).exists()) {
                File fl2 = new File(caminho);
                fl2.mkdir();
            }
            if (!new File(caminho + "/Pendentes/").exists()) {
                File fl2 = new File(caminho + "/Pendentes/");
                fl2.mkdir();
            }
            if (!new File(caminho + "/Pendentes/"+tipoEnvio+"/").exists()) {
                File fl2 = new File(caminho + "/Pendentes/"+tipoEnvio+"/");
                fl2.mkdir();
            }
        }
        try {
            File fl = new File(caminho + "/Pendentes/"+tipoEnvio+"/"+item.getName());
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
            request.getSession().removeAttribute("enviarArquivosBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enviarArquivosBean");            
        } catch (Exception e) {
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
            Logger.getLogger(servletEnvioArquivoContribuinte.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(servletEnvioArquivoContribuinte.class.getName()).log(Level.SEVERE, null, ex);
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
