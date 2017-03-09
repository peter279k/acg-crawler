package webapp;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class StaticPage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
    	String filePath = "./assets/www/" + req.getPathInfo();
    	File f = new File(filePath);

    	String output = "";
    	if(filePath.contains("images")) {
    		BufferedImage bi = null;
    		if(f.exists()) {
    			bi = ImageIO.read(f);
    		} else {
    			InputStream is = getServletContext().getResourceAsStream("/WEB-INF/assets/www/" + req.getPathInfo());
    			bi = ImageIO.read(is);
    		}

			OutputStream outputStream = resp.getOutputStream();

			if(filePath.contains("ico")) {
				resp.setHeader("Content-Type", "image/png");
				ImageIO.write(bi, "png", outputStream);
			} else {
				resp.setHeader("Content-Type", "image/gif");
				ImageIO.write(bi, "gif", outputStream);
			}

            outputStream.close();
    	} else {
    		if(f.exists()) {
    			Path indexPath = Paths.get(filePath);
    			List<String>contents = Files.readAllLines(indexPath, Charset.forName("UTF-8"));
    			for(int index=0;index<contents.size();index++) {
    				output += contents.get(index);
    			}
    		} else {
    			InputStream is = getServletContext().getResourceAsStream("/WEB-INF/assets/www/" + req.getPathInfo());
        		BufferedReader b = new BufferedReader( new InputStreamReader(is, "UTF-8"));
        		String str = "";
        		while((str = b.readLine()) != null) {
        			output += str;
        		}
        		is.close();
        		b.close();
    		}

    		if(filePath.contains("css")) {
    			resp.setHeader("Content-Type", "text/css; charset=utf-8");
    		} else if(filePath.contains("js")) {
    			resp.setHeader("Content-Type", "application/javascript; charset=utf-8");
    		} else {
    			resp.setHeader("Content-Type", "application/json; charset=utf-8");
    		}

    		resp.getWriter().print(output);
    	}
    }
}
