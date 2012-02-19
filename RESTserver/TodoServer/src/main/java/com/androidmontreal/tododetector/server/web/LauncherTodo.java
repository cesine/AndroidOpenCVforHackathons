package com.androidmontreal.tododetector.server.web;

import org.eclipse.jetty.embedded.SecuredHelloHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.inject.servlet.GuiceFilter;
import com.kanawish.hibernate.HibernateUtil;

/**
 * <p>Based on examples and tutorials found on the web.
 * <p>The security part of this is inspired from {@link SecuredHelloHandler} in example-jetty-embedded.
 * 
 */
public class LauncherTodo {
	
	public static void main(String[] args) throws Exception {
		// FIXME: This is horrid, but I wanted my PoC running ASAP!
		HibernateUtil.initSessionFactory( "hibernate.todo.cfg.xml" );
		
		// Create the server.
		Server server = new Server(8080);

		// ** SERVLET CONTEXT HANDLER
		// Create a servlet context and add the jersey servlet.
		ServletContextHandler sch = new ServletContextHandler(server, "/");
		
		// Add our Guice listener that includes our bindings
		sch.addEventListener(new TodoServletConfig());
		
		// Then add GuiceFilter and configure the server to
		// reroute all requests through this filter.
		sch.addFilter(GuiceFilter.class, "/*", null);
		
		
		// Must add DefaultServlet for embedded Jetty.
		// Failing to do this will cause 404 errors.
		// This is not needed if web.xml is used instead.
		sch.addServlet(DefaultServlet.class, "/");
		
		// ** FILE SERVER HANDLER
		
//		ResourceHandler resourceHandler = new ResourceHandler();
		// TODO This is just during dev.
//		resourceHandler.setDirectoriesListed(true);
//		resourceHandler.setWelcomeFiles( new String[] { "cdr.html" });
//		URL webURL = LauncherTodo.class.getResource("/web-voip/");
//		resourceHandler.setResourceBase(webURL.getPath());
		
		// ** COMBINE HANDLERS
		
		HandlerList handlerList = new HandlerList();
//		handlerList.addHandler(resourceHandler);
		handlerList.addHandler(sch);
		
		server.setHandler(handlerList);
		
		// Start the server
		server.start();
		server.join();
	}
	
}