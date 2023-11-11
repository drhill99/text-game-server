# Overview

This Java project is a proof of concept of using a network server as an interface between two users playing a text combat game. In the present form the server connects to user on a single machine, but can be modified to function between the server and two users on a network. Utilizing the server is as simple as running the server program which will wait for two clients to connect when the client programs are individually started.

[Software Demo Video](https://www.youtube.com/watch?v=QUA4MrpG9xM)

# Network Communication

The network communication supported by this server is a client-server-client architecture, utilizing TCP and port number 20000 for the server, and the localhost ID address of 127.0.0.1.
The clients are communicating with each other by reading and changing attributes of a class object being received and transmitted through the server.

The client program can be found in the following [repository.](https://github.com/drhill99/Java-text-RPG)

# Development Environment

This network server program is written in the Java programming language using the JetBrains [Intelli-J IDE](https://www.jetbrains.com/idea/)and [Open JDK-21](https://jdk.java.net/)

# Useful Websites

* [geeksforgeeks.org](https://www.geeksforgeeks.org/)
* [javatpoint.com](https://www.javatpoint.com/)

# Future Work

* Update the server to loop indefinitely allowing users to start multiple games.
* Update the server to allow for multiple users.
* Update the server and clients to allow for communication on a network instead of just a single machine.
