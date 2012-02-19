package com.kanawish.hibernate;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionInterceptor implements MethodInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(TransactionInterceptor.class);

	/**
	 * Very simple version, needs work/testing for nesting.
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		Transactionnal annotation = method.getAnnotation(Transactionnal.class);
		// Make sure we're dealing with a @Transaction annotated method.
		if( annotation == null ) {
			return invocation.proceed();
		} 
		// Make any other checks on parameters and such here. None for this case yet.
		
		// We need to start a transaction on the current session for this thread.
		Session currentSession = HibernateUtil.getCurrentSession();
		
		try {
			// This will return an existing transaction if already started.
			Transaction peek = currentSession.getTransaction();
			boolean firstCaller = false ;
			if( peek == null ) {
				firstCaller = true ;
			}
			
			if( peek == null || !peek.isActive() ) {
				// TODO: Looks like isActive() would be true only if we're in a nested @Transactionnal call, so we could use this to avoid committing anywhere but 
				// from the first caller...
				logger.info("no current transaction in session.");
			}
			
			// Important to consider when we'll support nested @Transactionnal annotations.
			currentSession.beginTransaction();
			Object retVal = invocation.proceed();
			// TODO: Check how this behaves with nested @Transactionnal annotations.
			if( firstCaller ) {
				currentSession.getTransaction().commit();
			}
			
			return retVal;
		} catch (RuntimeException e) {
			Transaction tx = HibernateUtil.getCurrentSession().getTransaction();
			// I added this safeguard to example hibernate code, but I'm not
			// sure it's needed.
			if (tx != null) {
				tx.rollback();
			}

			logger.error("Caught an exception during a transaction.", e);

			throw e; // or display error message

		}
		
	}

}
