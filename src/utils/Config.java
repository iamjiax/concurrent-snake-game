package utils;

public class Config {
  // Server IP and Port
  public static final String SERVER_IP = "localhost";
  public static final int SERVER_PORT = 8080;

  // Game Configuration
  public static final int BOARD_SIZE = 32;
  public static final int UNIT_SIZE = 10;
  public static final int GAME_SPEED_MS = 100;
  public static final int INIT_DELAY = 1000;

  // message type
  public static final int CREATE = 1;
  public static final int JOIN = 2;
  public static final int CHANGE_DIR = 3;
  public static final int WAIT_PLAYER = 4;
  public static final int WAIT_START = 5;
  public static final int GAME_OVER = 6;
  public static final int IN_PROGRESS = 7;
  // result type
  public static final int DRAW = 0;
  public static final int HAS_WINNER = 1;
}
