package xiaobai.content;
import javax.imageio.spi.ServiceRegistry;
import javax.jcr.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;
import java.util.Iterator;

import org.apache.commons.cli.*;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.rmi.remote.RemoteRepository;
import org.apache.jackrabbit.rmi.server.RemoteAdapterFactory;
import org.apache.jackrabbit.rmi.server.ServerAdapterFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.log4j.BasicConfigurator;
import xiaobai.content.service.UserService;

import java.util.Scanner;


public class Content {
    private static UserService xiaobaiS = null;
    private static Session xiaobai = null;
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
        LocateRegistry.createRegistry(9000);
        String name = "rmi://localhost:9000/content";
        RemoteAdapterFactory factory = new ServerAdapterFactory();
        RemoteRepository remote = factory.getRemoteRepository(repository);
        Naming.bind(name,remote);
        Logger logger = LoggerFactory.getLogger(Content.class);
        logger.info("rmi on 9000");
        Options options = new Options();
        Option init = new Option("init",false,"init as user xiaobai");
        Option add =  Option.builder("add").numberOfArgs(4).desc("desc of node").build();
        Option delete = Option.builder("delete").hasArg().desc("path of node").build();
        options.addOption(init);
        options.addOption(add);
        options.addOption(delete);
        Scanner scanner=new Scanner(System.in);
        System.out.println("cli start");
        while (scanner.hasNextLine())
        {
            try {
                String commend = scanner.nextLine();
                String[] params = commend.split(" +");
                CommandLineParser parser = new DefaultParser();
                CommandLine cl = parser.parse(options,params);
                if(cl.hasOption("init"))
                {
                    Session session = repository.login(
                            new SimpleCredentials("admin", "admin".toCharArray()));
                    UserService adminS = new UserService(session);
                    Node root = session.getRootNode();
                    adminS.removeUser("xiaobai");
                    adminS.createUser("xiaobai","201314",root);
                    adminS.sessionOut();
                }
                else if(cl.hasOption("add"))
                {
                    UserService xiaobaiS = getXiaobaiS(repository);
                    xiaobaiS.setRoot();
                    String[] keys =  cl.getOptions()[0].getValues();
                    Node root = getXiaobai(repository).getRootNode();
                    Node node = null;
                    if(keys[2].equals("/"))
                    {
                        node = root;
                    }
                    else
                    {
                        node = root.getNode(keys[2]);
                    }
                    xiaobaiS.set(keys[0],keys[1],node,keys[3]);
                    xiaobaiS.sessionOut();
                }
                else if(cl.hasOption("delete"))
                {
                    UserService xiaobaiS = getXiaobaiS(repository);
                    String path = cl.getOptions()[0].getValues()[0];
                    xiaobaiS.delete(path);
                    xiaobaiS.sessionOut();
                }
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        }
    }
    private static UserService getXiaobaiS(Repository repository) throws Exception
    {
        if(xiaobaiS==null||xiaobai==null||!xiaobai.isLive())
        {
            Session xiaobai = getXiaobai(repository);
            xiaobaiS = new UserService(xiaobai);
        }
        return xiaobaiS;
    }
    private static Session getXiaobai(Repository repository) throws Exception
    {
        if(xiaobai==null||!xiaobai.isLive())
        {
            xiaobai = repository.login(new SimpleCredentials("xiaobai", "201314".toCharArray()));
        }
        return xiaobai;
    }
}
