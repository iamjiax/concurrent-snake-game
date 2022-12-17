package snakeClient;

import static utils.Config.*;

import utils.Message;
import utils.MessageReader;
import utils.MessageSender;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import snakeClient.controller.GameController;

public class SnakeClient {

  public static void start(String[] args) {
    Message initMsg = parse(args);
    try (DatagramSocket clientSocket = new DatagramSocket(initMsg.getPlayerPort())) {
      System.out.println("UDP Snake Game Client");
      MessageReader reader = new MessageReader(clientSocket);
      MessageSender sender = new MessageSender(clientSocket);
      InetAddress serverIP = InetAddress.getByName(SERVER_IP);

      GameController controller =
          new GameController(initMsg.getGameId(), initMsg.getPlayerName(), serverIP, sender);
      controller.setVisible(true);
      sender.send(initMsg, serverIP, SERVER_PORT);
      while (true) {
        Message msg = reader.readNextMessage();
        System.out.println("Get: " + msg);
        controller.update(msg);
        if (msg.getType() == GAME_OVER) break;
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Message parse(String[] args) {
    try {
      int type;
      if (args[0].equals("create")) {
        type = 1;
      } else if (args[0].equals("join")) {
        type = 2;
      } else {
        throw new IllegalArgumentException();
      }
      String gameId = args[1];
      String playerName = args[2];
      String playerIP = args[3];
      int playerPort = Integer.parseInt(args[4]);

      InetAddress playerAddr = InetAddress.getByName(playerIP);
      Message initMsg =
          new Message.Builder(type)
              .setGameId(gameId)
              .setPlayerName(playerName)
              .setPlayerIP(playerAddr)
              .setPlayerPort(playerPort)
              .build();
      return initMsg;

    } catch (Exception e) {
      throw new IllegalArgumentException(
          "Command format error. Please enter:\n"
              + "create/join [game_id] [nick_name] [IP] [port_number]");
    }
  }
}
