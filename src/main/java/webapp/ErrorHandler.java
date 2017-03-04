package webapp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ErrorHandler extends HttpServlet {
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
    	resp.setHeader("Content-Type", "text/html");
    	resp.getWriter().print("<!DOCTYPE html>");
    	resp.getWriter().print("<html><head><title>Error</title></head>");
    	resp.getWriter().print("<body>");
        resp.getWriter().println("<h2>Something Error Happen...</h2></body></html>");
    }
}
