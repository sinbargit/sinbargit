package xiaobai.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import xiaobai.dao.entity.test;

public class Dao {
    static Configuration cfg = null;
    static SessionFactory factory = null;
    static {
        cfg = new Configuration().configure();
        factory = cfg.buildSessionFactory();
    }


    public static void main(String[] args)
    {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        test tt= new test();
        tt.setName("qxiaobai");
        session.save(tt);
        transaction.commit();
        session.close();
    }
}
