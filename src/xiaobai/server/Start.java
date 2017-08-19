package xiaobai.server;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.server.ServerProperties;

public class Start
{
    public static void main(String[] args) throws Exception
    {
        // Set up a simple configuration that logs on the console.
        BasicConfigurator.configure();
        Server server = new Server(8080);
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        server.setHandler(contextHandler);
        ServletHolder holder = new ServletHolder (ServletContainer.class);
        holder.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "xiaobai.restful");
        contextHandler.addServlet(holder,"/*");
        holder.setInitOrder(1);
        server.start();
        server.join();
    }
}