package xiaobai.restful;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
@Path("resource")
public class Resource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() throws Exception {
        return "hello wolrd! ";
    }
}
