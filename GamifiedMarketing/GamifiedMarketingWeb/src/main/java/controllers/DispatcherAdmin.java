package controllers;


import javax.ejb.EJB;

import org.thymeleaf.context.WebContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import services.ProductService;


import java.io.IOException;


@WebServlet("/DispatcherAdmin")
@MultipartConfig
public class DispatcherAdmin extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;

    public DispatcherAdmin() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init() throws ServletException {

        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String choice=null;
        String path=null;

        if (request.getParameter("button") != "" && !request.getParameter("button").isEmpty()) {
            choice= request.getParameter("button");
        }

        if (choice != null) {

            switch(choice) {
                case "insert-button":
                    path = "/WEB-INF/creation.html";
                    break;
                case "inspect-button":
                    path = "/WEB-INF/inspection.html";
                    break;
                case "delete-button":
                    path = "/WEB-INF/deletion.html";
                    break;
                default:
                    path = "/WEB-INF/home_admin.html";
                    break;
            }

            ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            templateEngine.process(path, ctx, response.getWriter());
        }
        else{
            path = "/WEB-INF/home_admin.html";
            ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            templateEngine.process(path, ctx, response.getWriter());
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }


}
