package com.company.app.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class AjaxSessionExpirationFilter implements Filter {
	
	private final int customSessionExpiredErrorCode = 901;
	private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
	        Arrays.asList("", "/login", "/logout", "/register","/forgotPassword","/resetPassword","/resetPassword/resetPasswordProcess")));
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fliterChain)
			throws IOException, ServletException {
		
		try{
			
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;
			
			HttpSession session = request.getSession(false);
			String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
			
			boolean allowedPath = ALLOWED_PATHS.contains(path);
			SecurityContextImpl sci = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
			String ajaxHeader = request.getHeader("X-Requested-With");
			
			// If no session and request is ajax request and the request path is not included paths inside the system
			if(sci == null){
				if("XMLHttpRequest".equals(ajaxHeader) && !allowedPath){
			
				response.sendError(this.customSessionExpiredErrorCode);
				
                return;
                
				}else{
					fliterChain.doFilter(req, res);
				}
			}else{
				fliterChain.doFilter(req, res);
			}
					
		}catch(NullPointerException e ){
			
			System.out.println("NULL EXCP: "+e.getMessage());
			fliterChain.doFilter(req, res);
			
		}
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		Filter.super.init(filterConfig);
	}
	
}
