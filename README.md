Distributed Key-Value Store - Networking Assignment 🗄️

Welcome to the Distributed Key-Value Store project! 🚀 This project is implemented in Java using UDP sockets, allowing nodes to communicate and store key-value pairs in a distributed manner.

📌 Features

✅ Supports PUT, GET, and DELETE operations on key-value pairs.✅ Uses UDP (DatagramSocket) for network communication.✅ Implements multithreading with ExecutorService for handling multiple requests.✅ Ensures thread-safe key-value storage using ConcurrentHashMap.✅ Graceful shutdown to avoid port conflicts.

🔧 Prerequisites

Make sure you have the following installed:

Eclipse IDE (Download: Eclipse)

Java Development Kit (JDK 17+) (Download: Oracle JDK)

Git (Optional for version control)

📂 Project Folder Structure

DistributedKVStore/
│── NetworkingAssignment/
│   │── Node.java  # Main UDP Server
│   │── NodeInterface.java  # Interface for key-value store
│── README.md  # Instructions
│── .gitignore  # Git ignore file

🚀 How to Run the Project in Eclipse

Follow these steps to set up and run the project in Eclipse:

1️⃣ Open Eclipse and Create a New Java Project

Open Eclipse.

Go to File → New → Java Project.

Name the project DistributedKVStore and click Finish.

2️⃣ Import the Existing Code

Right-click on your project DistributedKVStore → Click New → Package.

Name the package DistributedKVStore.NetworkingAssignment.

Copy & paste the Node.java and NodeInterface.java files into this package.

3️⃣ Compile the Code

Right-click on Node.java.

Click Run As → Java Application.

🎉 Your server should now be running!

🛠️ Testing the Server

Check if the Node is Running on Port 5000

To verify if the server is running on port 5000, open Command Prompt and run:

netstat -ano | findstr :5000

If it shows an active connection, the node is listening.

Handling "Port Already in Use" Issue

If you restart the server and get an error, free the port by running:

taskkill /F /IM java.exe

Then, rerun the program.

📡 Sending Requests to the Node

Use netcat (nc) or a simple Java UDP client to send requests:

Example Commands:

# Store a key-value pair
PUT key1 HelloWorld

# Retrieve a value
GET key1

# Delete a key
DELETE key1

The server will respond accordingly:

"OK" (for successful PUT/DELETE)

"ERROR: Key not found" (if the key doesn’t exist)

🛑 Shutting Down the Server

To gracefully stop the server, press CTRL + C in the Eclipse console, or run:

taskkill /IM java.exe /F

🎯 Future Enhancements

🔹 Add node-to-node communication for distributed storage.🔹 Implement a leader election algorithm.🔹 Add persistence (save data across restarts).

📜 License

This project is for educational purposes. Feel free to modify and improve it! 😊

🚀 Happy Coding! 🎯

