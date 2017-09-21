package xiaobai.content.setcontent;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Node;
import org.apache.jackrabbit.commons.JcrUtils;

public class SetRoot {
    Session session;
    public SetRoot(Session session)
    {
        this.session = session;
    }
    public void setRoot() throws Exception
    {
        try {
            Node root = session.getRootNode();
            root.setProperty("welcome", "Hello, World!");
            Node hello = root.addNode("hello");
            hello.addNode("world");
            session.save();
        } finally {
            session.logout();
        }
    }
}
