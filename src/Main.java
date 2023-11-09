import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        final int serverPort = 20000;
//        final int serverPort2 = 20001;
            // start server socket
        try (ServerSocket serverSocket = new ServerSocket(serverPort)

             ) {
                    // client 1 connect
                Socket clientSocket1 = serverSocket.accept();
                    // client 2 connect
                Socket clientSocket2 = serverSocket.accept();
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


                    Thread thread1 = new Thread(new clientHandler(inputStream1, outputStream2));
                    Thread thread2 = new Thread(new clientHandler(inputStream2, outputStream1));

                    thread1.start();
                    thread2.start();

                    thread1.join();
                    thread2.join();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static class clientHandler implements Runnable {
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;

        public clientHandler(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
            this.inputStream = inputStream;
            this.outputStream = outputStream;
        }

        @Override
        public void run() {
            while(true){
                try {
                    gameData recvObject = (gameData) inputStream.readObject();
                    if(recvObject.playerOneHealth == 0 | recvObject.playerTwoHealth ==0){
                        break;
                    } else {
                        outputStream.writeObject(recvObject);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
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