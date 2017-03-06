package webapp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import security.TokenGenerator;

@SuppressWarnings("serial")
public class IndexPage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
    	TokenGenerator csrf = new TokenGenerator();
    	CsrfToken token = csrf.generateToken(req);
    	csrf.saveToken(token, req, resp);

    	Path indexPath = Paths.get("./assets/www/index.html");
    	List<String>contents = Files.readAllLines(indexPath, Charset.forName("UTF-8"));
    	String output = "";
    	for(int index=0;index<contents.size();index++) {
    		output += contents.get(index);
    	}

    	resp.setHeader("Content-Type", "text/html; charset=utf-8");
    	resp.setHeader(csrf.getHeaderName(), token.getToken());
        resp.getWriter().println(output.replace("[csrf-token]", token.getToken()));
    }
}
