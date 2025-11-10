package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT = 1234;

    public static void main(String[] args) throws IOException {

        System.out.println("Starting server...");
        try (ServerSocket serverSocket = new ServerSocket(Server.PORT)) {
            System.out.println("Server started on port " + serverSocket.getLocalPort());
            System.out.println("Waiting for client...");
            // TODO support multiple clients

            try (Socket clientSocket = serverSocket.accept();
                 var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 var out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                System.out.println("Client connected from port " + clientSocket.getPort());

                while (true) {
                    String lineFromClient = in.readLine();
                    if (lineFromClient == null) {
                        System.out.println("Client disconnected from port " + clientSocket.getPort());
                        break;
                    }
                    System.out.println(clientSocket.getPort() + ": " + lineFromClient);
                    String lineToClient = new StringBuilder(lineFromClient).reverse().toString();
                    out.println(lineToClient);
                }
            }
        }
    }
}

// TODO++ write multi-client chat program
