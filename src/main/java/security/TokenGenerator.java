package security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;

public class TokenGenerator implements CsrfTokenRepository {
	
	private int cookieMaxAge = 300;
	private String parameterName = "X-CSRF-Token";
	private String headerName = "X-CSRF";
	private RandomValueStringGenerator generator = new RandomValueStringGenerator(10); 

    public int getCookieMaxAge() { 
        return this.cookieMaxAge; 
    } 
 
    public void setCookieMaxAge(int cookieMaxAge) { 
        this.cookieMaxAge = cookieMaxAge; 
    } 
	
	public void setHeaderName(String name) {
		this.headerName = name;
	}
	
	public String getHeaderName() {
		return this.headerName;
	}
	
	public String getParameterName() { 
        return this.parameterName; 
    } 
 
    public void setParameterName(String parameterName) { 
        this.parameterName = parameterName; 
    } 
	
	public void setGenerator(RandomValueStringGenerator generator) { 
	    this.generator = generator; 
	}
	
	public RandomValueStringGenerator getGenerator() { 
		return this.generator; 
	} 
	
	@Override
	public CsrfToken generateToken(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String token = generator.generate();

		return new DefaultCsrfToken(this.getHeaderName(), this.getParameterName(), token);
	}

	@Override
	public void saveToken(org.springframework.security.web.csrf.CsrfToken token, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		boolean expire = false;
		if(token == null) {
			token = this.generateToken(request);
			expire = true;
		}
		
		Cookie csrfCookie = new Cookie(token.getParameterName(), token.getToken());
		csrfCookie.setHttpOnly(true);
		if(expire) {
			csrfCookie.setMaxAge(0);
		} else {
			csrfCookie.setMaxAge(this.getCookieMaxAge());
		}
		
		response.addCookie(csrfCookie);
	}

	@Override
	public org.springframework.security.web.csrf.CsrfToken loadToken(HttpServletRequest request) {
		// TODO Auto-generated method stub
		Cookie []cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie:request.getCookies()) {
				if(this.getParameterName().equals(cookie.getName()) &&
						cookie.getValue().equals(request.getHeader(this.getHeaderName()))) {

					return new DefaultCsrfToken(this.getHeaderName(), this.getParameterName(), cookie.getValue());
				}
			}
		}

		return null;
	}
}
