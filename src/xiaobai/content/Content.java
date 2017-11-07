package xiaobai.content;
import javax.imageio.spi.ServiceRegistry;
import javax.jcr.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;
import java.util.Iterator;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.rmi.remote.RemoteRepository;
import org.apache.jackrabbit.rmi.server.RemoteAdapterFactory;
import org.apache.jackrabbit.rmi.server.ServerAdapterFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.log4j.BasicConfigurator;
import xiaobai.content.service.UserService;

public class Content {
    public static void main(String[] args) throws Exception
    {

        // Set up a simple configuration that logs on the console.
        BasicConfigurator.configure();
        Iterator iterator = ServiceRegistry.lookupProviders(RepositoryFactory.class);
        int i =0;
        while(iterator.hasNext())
        {
            i++;
            System.out.println(iterator.next()+"---"+i);
        }

        Repository repository = JcrUtils.getRepository();
        Session session = repository.login(
                new SimpleCredentials("admin", "admin".toCharArray()));
        UserService adminS = new UserService(session);
        Node root1 = session.getRootNode();
        adminS.removeUser("xiaobai");
        adminS.createUser("xiaobai","201314",root1);
        Session xiaobai = repository.login(new SimpleCredentials("xiaobai", "201314".toCharArray()));
        Node root = xiaobai.getRootNode();
        UserService xiaobaiS = new UserService(xiaobai);
        xiaobaiS.setRoot();
        Node index = xiaobaiS.set("index");
        xiaobaiS.set("index","src/xiaobai/content/index.md",index,"md");
        xiaobaiS.sessionOut();
        LocateRegistry.createRegistry(9000);
        String name = "rmi://localhost:9000/content";
        RemoteAdapterFactory factory = new ServerAdapterFactory();
        RemoteRepository remote = factory.getRemoteRepository(repository);
        Naming.bind(name,remote);
        Logger logger = LoggerFactory.getLogger(Content.class);
        logger.info("rmi on 9000");
    }
}
