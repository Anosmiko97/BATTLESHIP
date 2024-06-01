package controlers.test;

/* Clases propias */
import controlers.socket.ServerControler;

public class TestServer {
    public static void main(String[] args) {
        ServerControler server = new ServerControler();
        server.compareKey("1234");
    }
}
