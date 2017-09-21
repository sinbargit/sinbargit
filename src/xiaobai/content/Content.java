package xiaobai.content;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.rmi.remote.RemoteRepository;
import org.apache.jackrabbit.rmi.server.RemoteAdapterFactory;
import org.apache.jackrabbit.rmi.server.ServerAdapterFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.log4j.BasicConfigurator;
import xiaobai.content.setcontent.SetRoot;

public class Content {
    public static void main(String[] args) throws Exception
    {

        // Set up a simple configuration that logs on the console.
        BasicConfigurator.configure();
        Repository repository = JcrUtils.getRepository();
        Session session = repository.login(
                new SimpleCredentials("admin", "admin".toCharArray()));
        new SetRoot(session).setRoot();
        LocateRegistry.createRegistry(9000);
        String name = "rmi://localhost:9000/content";
        RemoteAdapterFactory factory = new ServerAdapterFactory();
        RemoteRepository remote = factory.getRemoteRepository(repository);
        Naming.bind(name,remote);
        Logger logger = LoggerFactory.getLogger(Content.class);
        logger.info("rmi on 9000");
    }
}
