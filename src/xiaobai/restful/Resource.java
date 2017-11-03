package xiaobai.restful;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
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
        JsonObjectBuilder wrap  = Json.createObjectBuilder();
        if(node.isNodeType(NodeType.NT_FOLDER))
        {
            NodeIterator children = node.getNodes();
            JsonArrayBuilder arr = Json.createArrayBuilder();
            while(children.hasNext())
            {
                JsonObjectBuilder item  = Json.createObjectBuilder();
                Node child = children.nextNode();
                item.add("name",child.getName());
                if(child.isNodeType(NodeType.NT_FOLDER))
                {
                    item.add("type","folder");
                }
                else if(child.isNodeType(NodeType.NT_FILE))
                {
                    item.add("type","file");
                    item.add("ext",child.getProperty(Property.JCR_MIMETYPE).getString());
                }
                arr.add(item);
            }
            return arr.build().toString();
        }
        else if(node.isNodeType(NodeType.NT_FILE))
        {
            wrap.add("name",node.getName());
            wrap.add("ext",node.getProperty(Property.JCR_MIMETYPE).getString());
            wrap.add("content",node.getNode(Node.JCR_CONTENT).getProperty(Property.JCR_DATA).getString());
            wrap.add("type","file");
            return wrap.build().toString();
        }
        else
        {
            return "{}";
        }
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
