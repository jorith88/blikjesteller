package nl.jorith.blikjesteller.bd;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class TestBD {

	@GET @Path("")
	public String test() {
		return "Jawohl!";
	}
}
