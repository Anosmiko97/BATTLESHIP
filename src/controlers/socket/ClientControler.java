package controlers.socket;

import java.io.*;
import java.net.*;

/* Clases propias */
import models.AppProperties;

public class ClientControler {
    String hostName = "localhost";
    AppProperties properties = new AppProperties();
    int port = properties.getPort();

    public ClientControler(String[] args) {
        try (Socket socket = new Socket(hostName, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String userInput;
            System.out.println("Introduce un texto (\"exit\" para salir): ");
            while ((userInput = stdIn.readLine()) != null && !userInput.equalsIgnoreCase("exit")) {
                out.println(userInput);
                System.out.println("Echo: " + in.readLine());
            }
        } catch (UnknownHostException e) {
            System.err.println("No se puede encontrar el host " + hostName);
        } catch (IOException e) {
            System.err.println("Error al comunicar con el host " + hostName);
        }
    }
}

