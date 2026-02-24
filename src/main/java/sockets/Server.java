package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT = 1234;

    static void main() throws IOException {

        IO.println("Starting server...");
        try (ServerSocket serverSocket = new ServerSocket(Server.PORT)) {
            IO.println("Server started on port " + serverSocket.getLocalPort());
            IO.println("Waiting for client...");
            // TODO support multiple clients

            try (Socket clientSocket = serverSocket.accept();
                 var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 var out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                IO.println("Client connected from port " + clientSocket.getPort());

                while (true) {
                    String lineFromClient = in.readLine();
                    if (lineFromClient == null) {
                        IO.println("Client disconnected from port " + clientSocket.getPort());
                        break;
                    }
                    IO.println(clientSocket.getPort() + ": " + lineFromClient);
                    String lineToClient = new StringBuilder(lineFromClient).reverse().toString();
                    out.println(lineToClient);
                }
            }
        }
    }
}

// TODO++ write multi-client chat program
