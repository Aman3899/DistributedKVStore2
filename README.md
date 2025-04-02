# Distributed Key-Value Store - Networking Assignment 🗄️🌐

[![Java Version](https://img.shields.io/badge/Java-17%2B-%23ED8B00)](https://www.oracle.com/java/technologies/downloads/) [![License](https://img.shields.io/badge/License-Educational-blue)](LICENSE)

Welcome to the Distributed Key-Value Store project! 🚀 This project provides a basic implementation of a single-node key-value store server using Java and UDP sockets. It serves as a foundation for understanding network programming, concurrency, and distributed systems concepts.

Nodes communicate using a simple text-based protocol over UDP to handle requests for storing, retrieving, and deleting key-value pairs.

---

## ✨ Features

-   [x] **Basic Key-Value Operations:** Supports `PUT`, `GET`, and `DELETE` commands via a simple text-based protocol.
    -   `PUT <key> <value>`: Stores the value associated with the key.
    -   `GET <key>`: Retrieves the value associated with the key.
    -   `DELETE <key>`: Removes the key and its associated value.
-   [x] **UDP Communication:** Utilizes `java.net.DatagramSocket` for network interaction. Handles incoming requests and sends responses over UDP.
-   [x] **Concurrent Request Handling:** Employs `java.util.concurrent.ExecutorService` (fixed thread pool) to efficiently manage multiple simultaneous client requests without blocking the main listening thread.
-   [x] **Thread-Safe Storage:** Uses `java.util.concurrent.ConcurrentHashMap` for safe in-memory storage access from multiple worker threads.
-   [x] **Command-Line Configuration:** The node's listening host address and port are configured via command-line arguments at startup.
-   [x] **Graceful Shutdown:** Includes a JVM shutdown hook (`Runtime.getRuntime().addShutdownHook`) to attempt clean resource release (closing socket, terminating threads) when the process is terminated (e.g., via Ctrl+C).
-   [ ] **_(Note)_** This implementation focuses on a single node and does *not* currently include:
    -   Advanced UDP reliability mechanisms (acknowledgments, retransmissions, ordering guarantees). UDP's inherent limitations (packet loss, duplication, reordering) are not explicitly handled.
    -   Inter-node communication (clustering, data replication, sharding).
    -   Data persistence (data is lost when the node stops).
    -   Leader election or consensus algorithms.

---

## 🛠️ Technology Stack & Prerequisites

Make sure you have the following installed and configured:

-   **Java Development Kit (JDK):** Version 17 or later is recommended.
    -   [Download Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.java.net/install/)
    -   Verify installation: `java -version` and `javac -version`
-   **Eclipse IDE:** "Eclipse IDE for Java Developers" is recommended.
    -   [Download Eclipse](https://www.eclipse.org/downloads/packages/)
-   **Git (Optional):** For cloning the repository if applicable.
    -   [Download Git](https://git-scm.com/downloads)
-   **Netcat (Optional, for testing):** A versatile networking utility often available on Linux/macOS (`nc`) or installable on Windows.

---

## 📂 Project Folder Structure (Eclipse)

When imported into Eclipse as a Java project, the structure typically looks like this:
DistributedKVStore/ <-- Eclipse Project Root
├── src/ <-- Source Folder
│ └── DistributedKVStore/
│ └── NetworkingAssignment/
│ ├── Node.java # Main Node server logic
│ └── NodeInterface.java # Interface defining KV operations
├── JRE System Library/ <-- Managed by Eclipse
├── bin/ <-- (Typically where Eclipse puts compiled .class files)
├── .settings/ <-- Eclipse project settings
├── .classpath <-- Eclipse classpath definition
├── .project <-- Eclipse project definition
└── README.md <-- This file



---

## 🚀 Setting Up and Running in Eclipse

Follow these steps carefully to get the server running:

1.  **Clone or Download:**
    *   If using Git: `git clone <repository_url>`
    *   Otherwise: Download the source code zip and extract it.

2.  **Import Project into Eclipse:**
    *   Open Eclipse IDE.
    *   Go to `File` → `Import...`.
    *   Expand `General` → Select `Existing Projects into Workspace` → Click `Next`.
    *   Click `Browse...` next to "Select root directory" and navigate to the `DistributedKVStore` folder you cloned or extracted.
    *   Ensure the project appears in the "Projects" list and is checked.
    *   Click `Finish`.

3.  **Verify Project Setup:**
    *   The project `DistributedKVStore` should appear in the "Package Explorer".
    *   Expand `src` → `DistributedKVStore.NetworkingAssignment`. You should see `Node.java` and `NodeInterface.java`.
    *   Eclipse should automatically compile the project. Check the "Problems" view (`Window` → `Show View` → `Problems`) for any compilation errors (red 'X' icons).

4.  **Configure Run Arguments (Crucial!):**
    *   The `Node` requires two command-line arguments: `<host>` and `<port>`.
    *   In the "Package Explorer", right-click on `Node.java`.
    *   Select `Run As` → `Run Configurations...`.
    *   In the left panel, select `Java Application`. Click the "New launch configuration" icon (📄+) at the top left.
    *   **Name:** Give it a descriptive name, e.g., `Run Node localhost 5000`.
    *   **Main class:** Eclipse should automatically detect `DistributedKVStore.NetworkingAssignment.Node`. If not, use the `Search...` button.
    *   **Select the `(x)= Arguments` tab.**
    *   In the **`Program arguments:`** text box, enter the desired host and port, separated by a space.
        *   **Example 1 (Listen on localhost port 5000):** `localhost 5000`
        *   **Example 2 (Listen on all interfaces port 8001):** `0.0.0.0 8001`
        *   **Example 3 (Listen on a specific IP port 9000):** `192.168.1.15 9000` (Replace with an actual IP of your machine)

5.  **Run the Node:**
    *   Click `Apply`.
    *   Click `Run`.

6.  **Check the Console:**
    *   The Eclipse "Console" view should open.
    *   You should see output similar to:
        ```
        Node started on localhost/127.0.0.1:5000
        Node running. Press Ctrl+C to exit.
        ```
        (The exact host/IP displayed depends on your arguments and system.)
    *   🎉 **The server is now running and listening for UDP packets on the specified address and port!**

---

## 🛠️ Testing the Server

You can test the running node using `netcat` (`nc`) or by creating a simple UDP client program.

**Using `netcat` (from a separate terminal/command prompt):**

*   Replace `localhost` and `5000` with the host and port your server is actually listening on.
*   The `-u` flag is essential for UDP communication.
*   Use `echo` to send the command string.

```bash
# 1. Store a key-value pair
echo "PUT user:1 name:Alice" | nc -u localhost 5000
# Expected output (from server console, not netcat): Stored: user:1 -> name:Alice
# Expected response (sent back to netcat, may not display depending on nc version/flags): OK

# 2. Retrieve the value
echo "GET user:1" | nc -u localhost 5000
# Expected response (sent back to netcat): name:Alice

# 3. Retrieve a non-existent key
echo "GET user:2" | nc -u localhost 5000
# Expected response: ERROR: Key not found

# 4. Delete the key
echo "DELETE user:1" | nc -u localhost 5000
# Expected output (from server console): Deleted: user:1
# Expected response: OK

# 5. Try retrieving the deleted key
echo "GET user:1" | nc -u localhost 5000
# Expected response: ERROR: Key not found

# 6. Send an invalid command
echo "UPDATE user:1" | nc -u localhost 5000
# Expected response: ERROR: Unknown command

