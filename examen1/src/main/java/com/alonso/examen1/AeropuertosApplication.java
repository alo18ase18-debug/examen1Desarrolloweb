package com.alonso.examen1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de arranque de la aplicación AeroGestor.
 *
 * Ejecuta: mvn spring-boot:run
 * O bien:  java -jar aeropuertos-app-1.0.0.jar
 *
 * Luego abre: http://localhost:8080
 */
@SpringBootApplication
public class AeropuertosApplication {

    public static void main(String[] args) {
        SpringApplication.run(AeropuertosApplication.class, args);
    }
}
