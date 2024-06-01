package controlers.test;

/*Clase propias */
import controlers.socket.ClientControler;

public class TestClient {
    public static void main(String[] args) {
        ClientControler clientControler = new ClientControler();
        clientControler.sendKey();
    }
}
