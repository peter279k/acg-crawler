package webapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    	String filePath = "./assets/www/index.html";
    	File htmlFilePath = new File(filePath);
    	String output = "";
    	if(htmlFilePath.exists() == false) {
    		InputStream is = getServletContext().getResourceAsStream("/WEB-INF/assets/www/index.html");
    		BufferedReader b = new BufferedReader( new InputStreamReader( is ));
    		String str = "";
    		while((str = b.readLine()) != null) {
    			output += str;
    		}
    	} else {
    		Path indexPath = Paths.get(filePath);
    		List<String>contents = Files.readAllLines(indexPath, Charset.forName("UTF-8"));
    		for(int index=0;index<contents.size();index++) {
    			output += contents.get(index);
    		}
    	}

    	resp.setHeader("Content-Type", "text/html; charset=utf-8");
    	resp.setHeader(csrf.getHeaderName(), token.getToken());
        resp.getWriter().println(output.replace("[csrf-token]", token.getToken()));
    }
}
