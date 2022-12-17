package snakeServer;

import static utils.Config.SERVER_PORT;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import snakeServer.controller.GameManager;
import snakeServer.model.GameState;
import utils.Message;
import utils.MessageReader;
import utils.MessageSender;

public class SnakeServer {
  private Map<String, GameState> games;

  public SnakeServer() {
    this.games = new ConcurrentHashMap<>();
  }

  public static void start(String[] args) {
    SnakeServer server = new SnakeServer();
    try (DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT)) {
      System.out.println("UDP Snake Game Server");
      MessageReader reader = new MessageReader(serverSocket);
      MessageSender sender = new MessageSender(serverSocket);
      while (true) {
        Message msg = reader.readNextMessage();
        System.out.println("Get: " + msg);
        GameManager.manage(server.games, msg, sender);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
