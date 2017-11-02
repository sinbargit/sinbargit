package xiaobai.restful;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;

import javax.jcr.*;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("resource")
public class Resource {
    private static Repository repository = null;

    @Path("welcome")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String welcome() throws Exception {
        return "hello world!";
    }

    @Path("/{name}/{path: .+}")
    @Produces("text/json; charset=utf-8")
    @GET
    public String get(@PathParam("name") String name, @HeaderParam("ps") String ps, @PathParam("path") String path,@Context HttpServletResponse response) throws Exception {
        // this.checkUser(name,ps);
        Session session = getRepository().login(new SimpleCredentials("xiaobai", "201314".toCharArray()));
        Node root = session.getRootNode();
        Node node = root.getNode(path);
        NodeIterator siblings = node.getParent().getNodes();
        JsonObjectBuilder jsonBuild  = Json.createObjectBuilder();
        JsonArrayBuilder arrBuild = Json.createArrayBuilder();
        while(siblings.hasNext())
        {
            JsonObjectBuilder t = Json.createObjectBuilder();
            Node temp = siblings.nextNode();
            temp.getProperties().nextProperty().getString();
            t.add(temp.getName(),temp.getProperty(Property.JCR_MIMETYPE).getString());
            arrBuild.add(t);
        }
        jsonBuild.add("sibings",arrBuild);
        jsonBuild.add("article",root.getNode(path).getNode(Node.JCR_CONTENT).getProperty(Property.JCR_DATA).getString());
        return jsonBuild.build().toString();
    }

    @Path("/{name}/{path}")
    @PUT
    public void put(@PathParam("name") String name, @PathParam("path") String path, @HeaderParam("ps") String ps) throws Exception {
        Session session = getRepository().login(new SimpleCredentials(name, ps.toCharArray()));
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
}
