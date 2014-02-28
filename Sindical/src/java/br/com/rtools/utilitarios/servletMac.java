package br.com.rtools.utilitarios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class servletMac extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = "ipconfig";
        String os = System.getProperty("os.name");
        String userNome = System.getProperty("user.home");
        Runtime r = Runtime.getRuntime();
        Process p = r.exec(command);
        BufferedReader inn = new BufferedReader(new InputStreamReader(p.getInputStream()));
        Pattern pattern = Pattern.compile(".*Physical Addres.*: (.*)");
        while (true) {
            String line = inn.readLine();
            if (line == null) {
                break;
            }
            Matcher mm = pattern.matcher(line);
            if (mm.matches()) {
                System.out.println(mm.group(1));
            }
        }

        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        String ip = MacAddress.getMacAddress(ipAddress);
        Enumeration nets = NetworkInterface.getNetworkInterfaces();
        response.sendRedirect("/Sindical/index.jsf");

    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
