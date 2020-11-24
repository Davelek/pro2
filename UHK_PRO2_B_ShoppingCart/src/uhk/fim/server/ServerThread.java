package uhk.fim.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {

    private final ServerSocket serverSocket;


    public ServerThread(int port) throws IOException {

        serverSocket = new ServerSocket(port);
    }



    @Override
    public void run() {
      while (true){
          try{
              Socket socket = serverSocket.accept();

              new Thread(new Runnable() {
                  @Override
                  public void run() {
                    procesRequest(socket);
                  }
              }).start();


          }catch (IOException e){
              e.printStackTrace();
          }
      }
    }


    private void procesRequest(Socket socket){

        try {
        //    BufferedReader in = new BufferedReader(new InputStreamsocket.getInputStream());


            PrintWriter out = new PrintWriter(socket.getOutputStream());

            String responseContent = "<html><head><title>Welocme page</title></head><body>" +
                    "<h1>" +
                    "Ahoj světe" +
                    "</h1>" +
                    "" +
                    "</body></html>ahoj světe";
            out.println(responseContent);
            out.println("HTTp/1.1 200 OK");
            out.println("Content-type: text/html");
            out.println("Content-length: " + responseContent.length());
            out.println();
            out.println();

            out.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
