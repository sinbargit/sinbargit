package xiaobai.restful;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;

import javax.jcr.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("resource")
public class Resource {
    private static Repository repository = null;
    private static Session session = null;
    private static UserManager userManager = null;

    @Path("hello")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() throws Exception {
        return "hello wolrd! ";
    }

    @Path("/{name}/{path}")
    @GET
    public String get(@PathParam("name") String name, @HeaderParam("ps") String ps,@PathParam("path") String path) throws Exception {
      this.checkUser(name,ps);
      Session session = getRepository().login(new SimpleCredentials(name,ps.toCharArray()));
      Node root = session.getRootNode();
      return  root.getNode(path).getName();
    }

    @Path("/{name}/{path}")
    @PUT
    public void put(@PathParam("name") String name,@PathParam("path") String path, @HeaderParam("ps") String ps) throws Exception {
        Session session = getRepository().login(new SimpleCredentials(name,ps.toCharArray()));
        Node root = session.getRootNode();
        root.addNode(path);
    }

    private Repository getRepository() throws Exception {
        if (null == repository) {
            ClientRepositoryFactory factory = new ClientRepositoryFactory();
            repository = factory.getRepository("rmi://localhost:9000/content");
        }
        return repository;
    }

    private Session getSession() throws Exception
    {
        if(null == session)
        {
            session = getRepository().login(new SimpleCredentials("admin", "admin".toCharArray()));
        }
        return session;
    }

    private UserManager getUserManager() throws Exception
    {
        if(null == userManager)
        {
            userManager = ((JackrabbitSession)getSession()).getUserManager();
        }
        return userManager;
    }

    private void checkUser(String name,String password) throws Exception
    {
        UserManager userManager = getUserManager();
        if(!userManager.findAuthorizables(name,name,UserManager.SEARCH_TYPE_USER).hasNext())
        {
            userManager.createUser(name,password);
        }
    }
}
