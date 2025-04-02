package NetworkingAssignment;

import java.net.*;
import java.util.concurrent.*;
import java.util.Map;
import java.io.IOException;

public class Node implements NodeInterface {
    private final int port;
    private final InetAddress address;
    private final DatagramSocket socket;
    private final Map<String, String> kvStore; // Key-value store
    private final ExecutorService executor;
    private volatile boolean running;

    public Node(String host, int port) throws IOException {
        this.port = port;
        this.address = InetAddress.getByName(host);
        this.socket = new DatagramSocket(port, address);
        this.kvStore = new ConcurrentHashMap<>(); // Thread-safe storage
        this.executor = Executors.newFixedThreadPool(10); // Handle multiple requests
        this.running = true;
        startListening();
    }

    // Start listening for UDP messages
    private void startListening() {
        executor.submit(() -> {
            byte[] buffer = new byte[1024];
            while (running) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    executor.submit(() -> handlePacket(packet));
                } catch (IOException e) {
                    if (running) System.err.println("Error receiving packet: " + e.getMessage());
                }
            }
        });
    }

    // Handle incoming UDP packet
    private void handlePacket(DatagramPacket packet) {
        String message = new String(packet.getData(), 0, packet.getLength());
        // Parse CRN message (placeholder - replace with RFC logic)
        String response = processCRNMessage(message);
        sendResponse(response, packet.getAddress(), packet.getPort());
    }

    // Process CRN message (to be implemented based on RFC)
    private String processCRNMessage(String message) {
        // Example: "PUT key value" -> store key-value pair
        String[] parts = message.split(" ", 3);
        switch (parts[0].toUpperCase()) {
            case "PUT":
                if (parts.length == 3) {
                    kvStore.put(parts[1], parts[2]);
                    return "OK";
                }
                return "ERROR: Invalid PUT format";
            case "GET":
                if (parts.length == 2) {
                    String value = kvStore.get(parts[1]);
                    return value != null ? value : "ERROR: Key not found";
                }
                return "ERROR: Invalid GET format";
            case "DELETE":
                if (parts.length == 2) {
                    kvStore.remove(parts[1]);
                    return "OK";
                }
                return "ERROR: Invalid DELETE format";
            default:
                return "ERROR: Unknown command";
        }
    }

    // Send response back to sender
    private void sendResponse(String response, InetAddress destAddr, int destPort) {
        try {
            byte[] data = response.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, destAddr, destPort);
            socket.send(packet);
        } catch (IOException e) {
            System.err.println("Error sending response: " + e.getMessage());
        }
    }

    // Shutdown node
    public void shutdown() {
        running = false;
        socket.close();
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Error during shutdown: " + e.getMessage());
        }
    }

    // Main method to run the node
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: java Node <host> <port>");
            System.exit(1);
        }
        Node node = new Node(args[0], Integer.parseInt(args[1]));
        Runtime.getRuntime().addShutdownHook(new Thread(node::shutdown));
    }

    // Placeholder for NodeInterface methods (replace with actual interface)
    @Override
    public void store(String key, String value) { kvStore.put(key, value); }
    @Override
    public String retrieve(String key) { return kvStore.get(key); }
    @Override
    public void delete(String key) { kvStore.remove(key); }
}

interface NodeInterface {
    void store(String key, String value);
    String retrieve(String key);
    void delete(String key);
}