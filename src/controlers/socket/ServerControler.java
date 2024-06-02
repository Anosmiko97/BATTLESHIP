package controlers.socket;

import java.io.*;
import java.net.*;

/* Clases propias */
import models.AppProperties;
import views.CreateMatchView;

public class ServerControler {
    private ServerSocket serverSocket;
    private AppProperties properties = new AppProperties();
    private int port = properties.getPort();
    private boolean running;

    public ServerControler() {}

    public void testServer() {
        running = true;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor escuchando en el puerto " + port);
            while (running) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Recibido: " + inputLine);
                        out.println(inputLine);
                    }
                } catch (IOException e) {
                    System.out.println("Error al manejar la conexión del cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }

    public boolean compareKey(String key) {  
        running = true;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor escuchando en el puerto " + port);
            while (running) {
                try (Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Recibido: " + inputLine);
                        if (key.equals(inputLine)) {
                            System.out.println("La clave coincide");
                            out.println(true);
                            System.out.println("conexion cerrada");
                            return true;
                        } else {
                            System.out.println("La clave no coincide");
                            out.println(false);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error al manejar la conexión del cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
        } finally {
            stopServer();
        }

        return false;
    }

    public void stopServer() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Servidor detenido");
            } catch (IOException e) {
                System.out.println("Error al cerrar el servidor: " + e.getMessage());
            }
        }
    }
}

