package nl.jorith.blikjesteller.util;

import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * Interface providing default methods for actions on cookies. This interface is meant for
 * being implemented by an enum that contains the various cookies that can be set along with their name,
 * path and max age.
 *
 * @author jorith.vandenheuvel
 *
 */
public interface CookieEnum {

	static final int MINUTE		= 60;
	static final int HOUR		= 60 * MINUTE;
	static final int DAY		= 24 * HOUR;
	static final int SESSION	= -1;

	String getName();
	int getMaxAge();
	String getPath();

	/**
	 * Retrieves the cookie value from the given request
	 *
	 * @param httpRequest
	 * @return An Optional containing the cookie value if the cookie is present in the request. Returns an
	 *         empty Optional if the cookie cannot be found.
	 */
	public default Optional<String> getValue(HttpServletRequest httpRequest) {
		
		if (httpRequest.getCookies() == null) {
			return Optional.empty();
		}
		
		return Stream.of(httpRequest.getCookies())
				.filter(cookie -> cookie.getName().equals(getName()))
				.map(cookie -> cookie.getValue())
				.findFirst();
	}

	public default void setValue(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String value) {
		setValue(httpRequest, httpResponse, value, null);
	}

	/**
	 * Stores the given value in a cookie
	 *
	 * @param httpRequest
	 * @param httpResponse
	 * @param value
	 */
	public default void setValue(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String value, String domain) {
		Cookie cookie = new Cookie(this.getName(), value);
		cookie.setPath(this.getPath());
		cookie.setMaxAge(this.getMaxAge());

		// set some additional security options
		cookie.setSecure(httpRequest.getScheme().equals("https"));
		cookie.setHttpOnly(true);

		if (StringUtils.isNotBlank(domain)) {
			cookie.setDomain(domain);
		}

		httpResponse.addCookie(cookie);
	}

	/**
	 * Retrieves the cookie value from the given request and returns the value as an
	 * enum constant of the specified enum type.
	 *
	 * @param httpRequest
	 * @param enumType
	 * @return An Optional containing the cookie value if the cookie is present in the request. Returns an
	 *         empty Optional if the cookie cannot be found.
	 * @throws IllegalArgumentException if the specified enum type has no constant with the specified name
	 * @throws NullPointerException if enumType is null
	 */
	public default <T extends Enum<T>> Optional<T> getEnumValue(HttpServletRequest httpRequest, Class<T> enumType) {
		Optional<String> stringValue = getValue(httpRequest);

		if (stringValue.isPresent()) {
			T enumValue = Enum.valueOf(enumType, stringValue.get());
			return Optional.of(enumValue);
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Stores the given enum value in a cookie
	 *
	 * @param httpRequest
	 * @param httpResponse
	 * @param value
	 */
	public default void setEnumValue(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Enum<?> value) {
		setValue(httpRequest, httpResponse, value.name());
	}

	/**
	 * Removes the cookie
	 *
	 * @param httpResponse
	 */
	public default void remove(HttpServletResponse httpResponse) {
		Cookie cookie = new Cookie(this.getName(), "");
		cookie.setPath(this.getPath());
		cookie.setMaxAge(0);
		httpResponse.addCookie(cookie);
	}
}
