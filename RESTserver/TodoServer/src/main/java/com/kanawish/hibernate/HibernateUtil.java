package com.kanawish.hibernate;

import java.net.URL;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>From the hibernate classic examples. The goal here is to move to using JTA/EntityManager as soon as practical. Then look into 
 * working from within a EJB3 container to get easier transaction management.
 * 
 * <p>We execute SchemaExport, SchemaUpdate and SchemaValidator directly for now.
 * [Using the proper command line switches, i.e. --config=hibernate.cfg.xml, etc]
 * 
 * {@link http://docs.jboss.org/hibernate/core/3.6/reference/en-US/html_single/#toolsetguide}
 * 
 */
public class HibernateUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
	
    private static SessionFactory sessionFactory ;
    
    // TODO: Fix this junk, just wanted to get my PoC running ASAP.
    public static void initSessionFactory() {
    	initSessionFactory("hibernate.cfg.xml");
    }
    
    public static void initSessionFactory(String configFile) {
    	try {
            // Create the SessionFactory from resource X
        	URL resource = HibernateUtil.class.getClassLoader().getResource(configFile);
            sessionFactory = new Configuration().configure(resource).buildSessionFactory();
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
        	logger.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static Session getCurrentSession() {
    	return sessionFactory.getCurrentSession();
    }
    
    /**
     * <p>This version gets the current session via factory.getCurrentSession().
     * @deprecated
     */
    public static void doSessionTxWork( Work work ) {
    	SessionFactory factory = getSessionFactory();
    	
    	// Non-managed environment idiom with getCurrentSession()
    	try {
    	    factory.getCurrentSession().beginTransaction();

    	    // do some work
    	    work.work(factory.getCurrentSession());

    	    factory.getCurrentSession().getTransaction().commit();
    	}
    	catch (RuntimeException e) {
    	    Transaction tx = factory.getCurrentSession().getTransaction();
    	    // I added this safeguard to example hibernate code, but I'm not sure it's needed.
    	    if( tx != null ) {
    	    	tx.rollback();
    	    }

    	    logger.error("Caught an exception doing work", e);

    	    throw e; // or display error message
    	}
    }
    
    /**
     * <p>This version opens a new session every time.
     * @deprecated
     */
    public static void doTxWork( Work work ) {
    	// Non-managed environment idiom
    	Session sess = getSessionFactory().openSession();
    	Transaction tx = null;
    	try {
    	    tx = sess.beginTransaction();

    	    // do some work
    	    work.work(sess);

    	    tx.commit();
    	}
    	catch (RuntimeException e) {
    	    if (tx != null) { 
    	    	tx.rollback();
    	    }
    	    logger.error("Caught an exception doing work", e);
    	    throw e; 
    	}
    	finally {
    	    sess.close();
    	}
    }
    
}