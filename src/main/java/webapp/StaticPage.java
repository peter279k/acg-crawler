package webapp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    	String output = "";
    	if(filePath.contains("images")) {
    		File f = new File(filePath);
    		BufferedImage bi = ImageIO.read(f);
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
    		Path indexPath = Paths.get(filePath);
    		List<String>contents = Files.readAllLines(indexPath, Charset.forName("UTF-8"));
    		for(int index=0;index<contents.size();index++) {
        		output += contents.get(index);
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
