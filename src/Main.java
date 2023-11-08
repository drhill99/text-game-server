import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        final int serverPort1 = 20000;
        final int serverPort2 = 20001;
            // start server socket
        try (ServerSocket serverSocket1 = new ServerSocket(serverPort1);
             ServerSocket serverSocket2 = new ServerSocket(serverPort2);
             ) {
                    // client 1 connect
                Socket clientSocket1 = serverSocket1.accept();
                    // client 2 connect
                Socket clientSocket2 = serverSocket1.accept();
                    // create input and output object streams for both clients
                try (
                        // input and output streams for client1
                        ObjectInputStream inputStream1 = new ObjectInputStream(clientSocket1.getInputStream());
                        ObjectOutputStream outputStream1 = new ObjectOutputStream(clientSocket1.getOutputStream());
                        // input and output streams for client2
                        ObjectInputStream inputStream2 = new ObjectInputStream(clientSocket2.getInputStream());
                        ObjectOutputStream outputStream2 = new ObjectOutputStream(clientSocket2.getOutputStream());
                ) {
                    // randomly choose a player to start, create the object and send to that client first
                    int randomPlayer = new Random().nextInt(2) + 1;
                    gameData gameDataObject = new gameData();
//                    int randomPlayer = 1;
                    if (randomPlayer == 1) {
                        outputStream1.writeObject(gameDataObject);
                        System.out.println("client 1 goes first");
                    } else {
                        outputStream2.writeObject(gameDataObject);
                        System.out.println("client 2 goes first");
                    }

                    while (true) {
                        System.out.println("waiting for client transmission....");
                        // deserialize game data received from client1
                        try {
                            gameData recvObject1 = (gameData) inputStream1.readObject();
                            // deserialize game data received from client2
                            if (recvObject1 == null) { // check player 1 if the object is not null, this means that player 1
                                System.out.println("Still waiting....");
                            } else {
                                System.out.println("received from client 1");
                                // just transmitted the object
                                if (checkGameOver(recvObject1)) { // if game over check returns true, break and end the server
                                    break;
                                } else { // if the game over check is false, transmit the object to player 2
                                    System.out.println("transmitted to client 2");
                                    outputStream2.writeObject(recvObject1);
                                }
                            }

                            gameData recvObject2 = (gameData) inputStream2.readObject();
                            // check for game over condition to kill the server
                            // look at the health of each player in the game data object and end loop and close server
                            // if one is at 0 health
                            if (recvObject2 == null) { // check player 2 if the object is not null, this means that player 2
                                System.out.println("Still waiting....");
                            } else {
                                System.out.println("received from client 2");
                                // just transmitted the object
                                if (checkGameOver(recvObject2)) { // if game over check returns true, break and end the server
                                    break;
                                } else { // if the game over check is false, transmit the object to player 1
                                    outputStream1.writeObject(recvObject2);
                                    System.out.println("transmitted to client 1");
                                }
                            }
//                        else {
//                            break;
//                        }
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        // Class object to be passed between clients
    static class gameData implements Serializable {
        public int playerOneHealth = -1;
        public int playerTwoHealth = -1;
        public int playerOneOutgoingDamage = 0;
        public int playerTwoOutgoingDamage = 0;
        public gameData(){
            }
    }

    static boolean checkGameOver(gameData gameDataObject){
            // return true if either player health is 0, else return false
        return gameDataObject.playerOneHealth == 0 || gameDataObject.playerTwoHealth == 0;
    }
}