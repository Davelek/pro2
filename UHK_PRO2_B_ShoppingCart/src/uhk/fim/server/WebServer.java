package uhk.fim.server;

import java.io.IOException;

public class WebServer {


    private int port;


    public WebServer(int port) {
        this.port = port;
    }

    public static void main(String[] args){
        WebServer server = new WebServer(8080);
        try {
            server.start();
        } catch (IOException e) {
            System.out.println("nastala chyba");
        }
    }

    private void start() throws IOException {
        System.out.println("Server spuštěn: naslouchá na adrese http://localhost:" + port);
        ServerThread thread = new ServerThread(port);
        thread.start();


    }
}
