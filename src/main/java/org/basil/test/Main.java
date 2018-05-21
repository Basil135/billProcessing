package org.basil.test;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * This class is Main class of the program, contains main method.
 *
 * @author Kutsykh Vasily (mailto:basil135@mail.ru)
 * @version $Id$
 * @since 22.05.2018
 */
public class Main {

    /**
     * parameter describes base uri of the restful service.
     */
    public static final String BASE_URI = "http://localhost:8080";

    /**
     * method describes simple http server.
     *
     * @return http server
     */
    public static HttpServer startServer() {

        final ResourceConfig rc = new ResourceConfig().packages("org.basil.test.controller");

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * method to start the program.
     *
     * @param args is input args
     * @throws IOException throw the exception
     */
    public static void main(String[] args) throws IOException {

        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.shutdownNow();
    }
}
