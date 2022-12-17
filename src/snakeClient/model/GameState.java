package snakeClient.model;

import static utils.Config.BOARD_SIZE;
import static utils.Config.WAIT_PLAYER;

import java.net.InetAddress;
import java.util.LinkedList;
import utils.Position;

public class GameState {
  private static GameState INSTANCE;

  private String gameId;
  private String playerName;
  private InetAddress serverIP;
  private Integer serverPort;
  private Position apple;
  private LinkedList<Position> snake1;
  private LinkedList<Position> snake2;
  private int state;
  private String winnerName;

  private GameState() {
    state = WAIT_PLAYER;
    apple = Position.random(BOARD_SIZE);
  }

  public static synchronized GameState get() {
    if (INSTANCE == null) {
      INSTANCE = new GameState();
    }
    return INSTANCE;
  }

  public synchronized void init(
      String gameId, String playerName, InetAddress serverIP, int serverPort) {
    if (this.gameId == null) this.gameId = gameId;
    if (this.playerName == null) this.playerName = playerName;
    if (this.serverIP == null) this.serverIP = serverIP;
    if (this.serverPort == null) this.serverPort = serverPort;
  }

  public String getGameId() {
    return this.gameId;
  }

  public String getPlayerName() {
    return this.playerName;
  }

  public InetAddress getServerIP() {
    return this.serverIP;
  }

  public int getServerPort() {
    return this.serverPort;
  }

  public Position getApplePosition() {
    return apple;
  }

  public LinkedList<Position> getSnake1() {
    return snake1;
  }

  public LinkedList<Position> getSnake2() {
    return snake2;
  }

  public int getState() {
    return state;
  }

  public String getWinnerName() {
    return this.winnerName;
  }

  public void setState(int state) {
    this.state = state;
  }

  public void setApple(Position apple) {
    this.apple = apple;
  }

  public void setSnake1(LinkedList<Position> snake1) {
    this.snake1 = snake1;
  }

  public void setSnake2(LinkedList<Position> snake2) {
    this.snake2 = snake2;
  }

  public void setWinnerName(String winnerName) {
    this.winnerName = winnerName;
  }
}
