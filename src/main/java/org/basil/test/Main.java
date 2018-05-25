package org.basil.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class is Main class of the program, contains main method.
 *
 * @author Kutsykh Vasily (mailto:basil135@mail.ru)
 * @version $Id$
 * @since 22.05.2018
 */
@SpringBootApplication
public class Main {

    /**
     * parameter describes base uri of the restful service.
     */
    public static final String BASE_URI = "http://localhost:8080";


    /**
     * method to start the program.
     *
     * @param args is input args
     */
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
    }
}
