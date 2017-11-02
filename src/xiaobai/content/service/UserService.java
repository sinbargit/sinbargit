package xiaobai.content.service;

import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import javax.jcr.security.*;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlList;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.core.security.principal.PrincipalImpl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

public class UserService {
    private Session session;
    private UserManager userManager;
    public UserService(Session session)
    {
        this.session = session;
    }
    public void sessionOut(){
        this.session.logout();
    }
    public void setRoot() throws Exception
    {
        Node root = session.getRootNode();
        root.setProperty("welcome", "Hello, World!");
        this.session.save();
    }
    private Node addNodeWithoutSNS(Node node,String name,String type) throws RepositoryException {
        if(node.hasNode(name))
        {
            return node.getNode(name);
        }
        else
        {
            return node.addNode(name,type);
        }
    }
    public Node set(String type,String path,String name,Node parent) throws Exception
    {
        Node index = addNodeWithoutSNS(parent,name, NodeType.NT_FOLDER);
        Node file = addNodeWithoutSNS(index,"content",NodeType.NT_FILE);
        InputStream stream = new FileInputStream(path);
        Binary binary = session.getValueFactory().createBinary(stream);
        Node content = addNodeWithoutSNS(file,Node.JCR_CONTENT,NodeType.NT_RESOURCE);
        content.setProperty(Property.JCR_DATA,binary);
        file.addMixin(NodeType.MIX_MIMETYPE);
        file.setProperty(Property.JCR_MIMETYPE,type);
        this.session.save();
        return file;
    }
    public void delete(String path) throws Exception
    {
        Node root = session.getRootNode();
        root.getNode(path).removeShare();
    }
//    public Node setIndex() throws Exception
//    {
//        Node root = session.getRootNode();
//        Node index = addNodeWithoutSNS(root,"index", NodeType.NT_FOLDER);
//        Node file = addNodeWithoutSNS(index,"content",NodeType.NT_FILE);
//        InputStream stream = new FileInputStream("src/xiaobai/content/index.md");
//        Binary binary = session.getValueFactory().createBinary(stream);
//        Node content = addNodeWithoutSNS(file,Node.JCR_CONTENT,NodeType.NT_RESOURCE);
//        content.setProperty(Property.JCR_DATA,binary);
//        file.addMixin(NodeType.MIX_MIMETYPE);
//        file.setProperty(Property.JCR_MIMETYPE,"md");
//        this.session.save();
//        return file;
//    }
    public boolean createUser(String name,String password,Node root) throws Exception {
        UserManager userManager = this.getUserManager();
        if(userManager.getAuthorizable(name)==null)
        {
            userManager.createUser(name,password);
            AccessControlManager acm = this.session.getAccessControlManager();
            JackrabbitAccessControlList  jacl = (JackrabbitAccessControlList)acm.getPolicies(root.getPath())[0];
            Privilege[] privileges = new Privilege[] { acm.privilegeFromName(Privilege.JCR_ALL) };
            jacl.addEntry(new PrincipalImpl(name),privileges, true, new HashMap());
            acm.setPolicy(root.getPath(),jacl);
            this.session.save();
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean removeUser(String name) throws Exception {
        UserManager userManager = this.getUserManager();
        User user = (User) userManager.getAuthorizable(name);
        if(user!=null)
        {
            user.remove();
            this.session.save();
            return true;
        }
        else
        {
            return false;
        }
    }
    private UserManager getUserManager() throws Exception
    {
        if(null == this.userManager)
        {
            this.userManager = ((JackrabbitSession)this.session).getUserManager();
        }
        return this.userManager;
    }
}
