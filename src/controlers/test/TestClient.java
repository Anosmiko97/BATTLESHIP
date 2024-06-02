package controlers.test;

import controlers.Server.ClientControler;

public class TestClient {
    public static void main(String[] args) {
        ClientControler clientControler = new ClientControler("localhost");
        clientControler.connect();
    }
}
