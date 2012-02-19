package com.kanawish.hibernate;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * <p>I'm not sure how to combine modules yet.. I'll add this code to the BusyBeeServletConfig class for PoC...
 * TODO: Use this module alongside instead.
 */
public class TransactionModule extends AbstractModule {
  @Override
  protected void configure() {
    TransactionInterceptor intercepter = new TransactionInterceptor();
    requestInjection(intercepter);
    bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactionnal.class), intercepter);
  }
}