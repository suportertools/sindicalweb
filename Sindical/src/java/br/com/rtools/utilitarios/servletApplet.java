/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rtools.utilitarios;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Lucas
 */
@WebServlet(name = "servletApplet", urlPatterns = {"/servletApplet"})
public class servletApplet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet servletApplet</title>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");

            out.println("<script type=\"text/javascript\">\n"
                    + "            \n"
                    + "            function abrir(){\n"
                    + "                document.applets[0].abrir();\n"
                    + "            }\n"
                    + "        </script>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet servletApplet at " + request.getContextPath() + "</h1>");
            out.println("<h2>Variável 01 " + request.getParameter("var1") + "</h2>");
            out.println("<h2>Variável 02 " + request.getParameter("var2") + "</h2>");

            out.println("<applet id=\"applet\" code=\"Bapplet.class\" width=\"350\" height=\"70\" codebase=\".\">\n"
                    + "     <param name=\"caminho\" value=\"teste\" />\n"
                    + "</applet> ");
            out.println("</body>");
            out.println("<a href='#' onclick='abrir()'>Abrir</a>");
            out.println("</html>");
        } finally {
            out.close();
        }
//        response.sendRedirect("/Sindical/retornoPadrao.jsf");

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
        processRequest(request, response);
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
        processRequest(request, response);
        try {
            response.setContentType("application/x-java-serialized-object");
            String parameter1 = request.getParameter("var1");
            String parameter2 = request.getParameter("var2");



        } catch (Exception e) {
            e.printStackTrace();
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