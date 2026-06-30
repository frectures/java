package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// Client v
// ...
// Edit...
// Modify options
// Allow multiple instances

public class Client {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    static void main() throws IOException {

        IO.println("Starting client...");
        try (Socket socket = new Socket("localhost", Server.PORT);
             var out = new PrintWriter(socket.getOutputStream(), true);
             var in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            IO.println("Client started on port " + socket.getLocalPort());
            IO.println("Please enter non-blank lines...");

            while (true) {
                String lineToServer = reader.readLine();
                if (lineToServer.isBlank()) break;

                out.println(lineToServer);
                String lineFromServer = in.readLine();
                if (lineFromServer == null) {
                    IO.println("Server disconnected from port " + socket.getPort());
                    break;
                }
                IO.println(lineFromServer);
            }
        }
    }
}
