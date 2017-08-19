package xiaobai.restful;
import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;

import javax.jcr.GuestCredentials;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
@Path("resource")
public class Resource {
    @Path("hello")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() throws Exception {
        return "hello wolrd! ";
    }
    @Path("rmi")
    @GET
    public String rmi() throws Exception {
        ClientRepositoryFactory factory = new ClientRepositoryFactory();
        Repository repository = factory.getRepository("rmi://localhost:9000/content");
        Session session = repository.login(new GuestCredentials());
        try {
            String user = session.getUserID();
            String name = repository.getDescriptor(Repository.REP_NAME_DESC);
            return name+"--"+user;
        } finally {
            session.logout();
        }
    }
}
