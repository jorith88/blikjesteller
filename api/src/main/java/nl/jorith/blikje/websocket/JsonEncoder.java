package nl.jorith.blikje.websocket;

import com.google.gson.Gson;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class JsonEncoder<T> implements Encoder.Text<T> {
	private static final Gson GSON = new Gson();

	@Override
	public void destroy() {}

	@Override
	public void init(EndpointConfig arg0) {}

	@Override
	public String encode(T object) throws EncodeException {
		return GSON.toJson(object);
	}
}
