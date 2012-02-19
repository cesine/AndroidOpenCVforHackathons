package com.androidmontreal.tododetector.server.web;

import com.androidmontreal.tododetector.server.service.TodoService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.GuiceServletContextListener;
import com.kanawish.hibernate.TransactionInterceptor;
import com.kanawish.hibernate.Transactionnal;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * <p>This is our master bean-wiring.
 * <p>
 */
public class TodoServletConfig extends GuiceServletContextListener {
	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new JerseyServletModule() {
			@Override
			protected void configureServlets() {
				// @Transactionnal configuration.
				// TODO: Use module instead if possible. It would be cleaner I think.
				TransactionInterceptor interceptor = new TransactionInterceptor();
				requestInjection(interceptor);
				bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactionnal.class), interceptor);
				
				// Register the management services
				bind(TodoService.class);

				// Important.
				bind(GuiceContainer.class);
				// Route all requests through GuiceContainer.
				serve("/*").with(GuiceContainer.class);
			}
		});
	}

}