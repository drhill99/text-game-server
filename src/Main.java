import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Main {
    public void main(String[] args) {
        final int serverPort = 20000;
        // start server socket
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            // client 1 connect
            Socket clientSocket1 = serverSocket.accept();
            // client 2 connect
            Socket clientsocket2 = serverSocket.accept();
            // create input and output object streams for both clients
            try (
                    // input and output streams for client1
                    ObjectInputStream inputStream1 = new ObjectInputStream(clientSocket1.getInputStream());
                    ObjectOutputStream outputStream1 = new ObjectOutputStream(clientSocket1.getOutputStream());
                    // input and output streams for client2
                    ObjectInputStream inputStream2 = new ObjectInputStream(clientsocket2.getInputStream());
                    ObjectOutputStream outputStream2 = new ObjectOutputStream(clientsocket2.getOutputStream());
            ) {
                    // randomly choose a player to start, create the object and send to that client first
                int randomPlayer = new Random().nextInt(2) + 1;
                gameData gameDataObject = new gameData();
                if(randomPlayer == 1){
                    outputStream1.writeObject(gameDataObject);
                } else {
                    outputStream2.writeObject(gameDataObject);
                }
                if (randomPlayer == 1) {
                    while (true) {
                        // deserialize game data received from client1
                        gameData recvObject1 = (gameData) inputStream1.readObject();
                        // deserialize game data received from client2
                        gameData recvObject2 = (gameData) inputStream2.readObject();
                        // check for game over condition to kill the server
                        // look at the health of each player in the game data object and end loop and close server
                        // if one is at 0 health

                        if (recvObject1 != null) { // check player 1 if the object is not null, this means that player 1
                            // just transmitted the object
                            if (checkGameOver(recvObject1)) { // if game over check returns true, break and end the server
                                break;
                            } else { // if the game over check is false, transmit the object to player 2
                                outputStream2.writeObject(recvObject1);
                            }
                        } else if (recvObject2 != null) { // check player 2 if the object is not null, this means that player 2
                            // just transmitted the object
                            if (checkGameOver(recvObject2)) { // if game over check returns true, break and end the server
                                break;
                            } else { // if the game over check is false, transmit the object to player 1
                                outputStream1.writeObject(recvObject2);
                            }
                        } else {
                            break;
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class gameData implements Serializable {
        public int playerOneHealth = 0;
        public int playerTwoHealth = 0;
        public int playerOneOutgoingDamage = 0;
        public int playerTwoOutgoingDamage = 0;
        public gameData(){

            }
    }

//    static gameData deserializeGameDataObject(ObjectInputStream objectInput){
//        try(objectInput){
//            return (gameData) objectInput.readObject();
//
//        } catch (IOException | ClassNotFoundException e){
//            e.printStackTrace();
//        }
//        return null;
//    }

//    static byte[] serializeGameDataObject(gameData gameDataObject){
//            // try block creates and returns byte array stream object
//        try(ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
//            ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput)) {
//            objectOutput.writeObject(gameDataObject);
//                // return serialized game data object
//            return byteOutput.toByteArray();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//        return null;
//    }

    static boolean checkGameOver(gameData gameDataObject){

        if(gameDataObject.playerOneHealth == 0 || gameDataObject.playerTwoHealth == 0){
            return true;
        } else {
            return false;
        }
    }
}