package nl.jorith.blikjesteller.filter;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.jorith.blikjesteller.rest.BlikjestellerCookie;
import nl.jorith.blikjesteller.rest.type.UserSession;

@WebFilter(urlPatterns = "/*")
public class DeviceIdFilter implements Filter {
	private static final Logger logger = Logger.getLogger(DeviceIdFilter.class.getName());

	
	@Inject	private UserSession userSession;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		String deviceId = BlikjestellerCookie.DEVICE_ID.getValue(httpRequest).orElseGet(() -> {
			
			String generatedDeviceId = generateDeviceId();
			logger.info("Generated new Device ID for device with IP address " + httpRequest.getRemoteAddr() + " (" + httpRequest.getHeader("User-Agent") + "): " + generatedDeviceId);
			
			BlikjestellerCookie.DEVICE_ID.setValue(httpRequest, httpResponse, generatedDeviceId);
			return generatedDeviceId;
		});
		
		userSession.setDeviceId(deviceId);
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}
	
	private String generateDeviceId() {
		UUID deviceId = UUID.randomUUID();
		return deviceId.toString();
	}

}
