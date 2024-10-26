package javaBeans;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javaBeans.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet(name = "CadUser", urlPatterns = {"/CadUser"})
@MultipartConfig
public class CadUser extends HttpServlet {

    String statusSQL = null;
    String sHTML = "";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(true);
        if (String.valueOf(session.getAttribute("nome")) == null) 
            response.sendRedirect("../index.html");
        
        Usuario user = new Usuario(); // Instancia o objeto Usuario
        user.email = request.getParameter("email");
        user.nome = request.getParameter("nome");
        user.celular = request.getParameter("celular");
        user.senha = request.getParameter("senha");
        user.nivel = request.getParameter("nivel");
               
        Part part = request.getPart("arquivo");
        InputStream arquivo = part.getInputStream();
        user.foto = arquivo;
        user.tamanho = part.getSize();
        

        if ( request.getParameter("gravar") != null) user.gravar();
 
        if ( request.getParameter("deletar") != null ) {
             user.deletar();
             session.invalidate();
        }
    
        statusSQL = user.statusSQL;
            
        //String url = request.getContextPath() + "/javaJSP/cadastro.jsp";
        //if ( user.buscarEmail() ) {
     
        //    response.sendRedirect(url);
        //}

        try (PrintWriter out = response.getWriter()) {
            sHTML = "<!DOCTYPE html>";
            sHTML += "<html><head><title>Cadastro de Usuários</title>";
            sHTML += "</head><body style=\"background-color: greenyellow;\">";
            sHTML += "<br><br><center> " + statusSQL + "<br> ";
            sHTML += "A sessão foi finalizada por que você deletou seu usuário..!<br>";
            sHTML += "<a href ='" + request.getContextPath() + "/" + "index.html";
            sHTML += "'> Voltar </a></center>";
            sHTML += "</body></html>";
            out.print(sHTML);
        }
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
