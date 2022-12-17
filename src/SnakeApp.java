import snakeClient.SnakeClient;
import snakeServer.SnakeServer;

public class SnakeApp {
  public static void main(String[] args) {
    try {
      if (args[0].equals("start_server")) {
        SnakeServer.start(args);
      } else if (args[0].equals("create") || args[0].equals("join")) {
        SnakeClient.start(args);
      } else {
        throw new IllegalArgumentException();
      }
    } catch (Exception e) {
      throw new IllegalArgumentException(
          "Command format error. Please enter:\nstart_server\nOR\n"
              + "create/join [game_id] [nick_name] [IP] [port_number]\n");
    }
  }
}
