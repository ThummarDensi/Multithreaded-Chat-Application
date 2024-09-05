
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient("localhost", 12345);
        client.start();
    }

    public ChatClient(String serverAddress, int port) throws IOException {
        socket = new Socket(serverAddress, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void start() {
        // Start a thread to listen for incoming messages from the server
        new Thread(new IncomingMessageHandler()).start();

        // Read input from the user and send it to the server
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            out.println(message);
        }
    }

    private class IncomingMessageHandler implements Runnable {

        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("Error in incoming message handler: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Failed to close socket: " + e.getMessage());
                }
            }
        }
    }
}
