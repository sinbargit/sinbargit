package xiaobai.content.service;

import javax.jcr.*;
import javax.jcr.security.*;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.core.SessionImpl;
import org.apache.jackrabbit.core.security.authorization.AccessControlEntryImpl;
import org.apache.jackrabbit.core.security.principal.PrincipalImpl;

import java.util.HashMap;
import java.util.Map;

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
        Node hello = root.addNode("helloo");
        this.session.save();
    }
    public boolean createUser(String name,String password,Node root) throws Exception {
        UserManager userManager = this.getUserManager();
        if(userManager.getAuthorizable(name)==null)
        {
            userManager.createUser(name,password);
            AccessControlManager acm = ((SessionImpl) this.session).getAccessControlManager();
            acm.setPolicy();
            AccessControlList accessList = new AccessControlEntryImpl(new PrincipalImpl(name),acm.privilegeFromName(Privilege.JCR_ALL),true,new HashMap());
            AccessControlPolicy policy = AccessControlList
            AccessControlPolicyIterator acpi = acm.getApplicablePolicies(root.getPath());
            while (acpi.hasNext()) {
                AccessControlPolicy acp = acpi.nextAccessControlPolicy();
                Privilege[] privileges = new Privilege[] { acm.privilegeFromName(Privilege.JCR_ALL) };

                ((AccessControlList) acp).addAccessControlEntry(new PrincipalImpl(name), privileges);
                acm.setPolicy(root.getPath(), acp);
            }

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
