package utils;

import static utils.Config.*;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

public class Message {
  private static final int BUF_SIZE = BOARD_SIZE * 32;
  private int type;
  private String gameId;
  private String playerName;
  private InetAddress playerIP;
  private Integer playerPort;

  private Direction direction;

  private Integer result;
  private String winnerName;

  private Integer seqNum;
  private Position apple;
  private LinkedList<Position> snake1;
  private LinkedList<Position> snake2;

  private Message(Builder builder) {
    this.type = builder.type;
    this.gameId = builder.gameId;
    this.playerName = builder.playerName;
    this.playerIP = builder.playerIP;
    this.playerPort = builder.playerPort;
    this.direction = builder.direction;
    this.result = builder.result;
    this.winnerName = builder.winnerName;
    this.seqNum = builder.seqNum;
    this.apple = builder.apple;
    this.snake1 = builder.snake1;
    this.snake2 = builder.snake2;
  }

  public int getType() {
    return this.type;
  }

  public String getGameId() {
    return this.gameId;
  }

  public String getPlayerName() {
    return this.playerName;
  }

  public InetAddress getPlayerIp() {
    return this.playerIP;
  }

  public Integer getPlayerPort() {
    return this.playerPort;
  }

  public Direction getDirection() {
    return this.direction;
  }

  public Integer getResult() {
    return this.result;
  }

  public String getWinnerName() {
    return this.winnerName;
  }

  public Integer getSeqNum() {
    return this.seqNum;
  }

  public Position getApple() {
    return this.apple;
  }

  public LinkedList<Position> getSnake1() {
    return this.snake1;
  }

  public LinkedList<Position> getSnake2() {
    return this.snake2;
  }

  /** @return the bytes array of this message */
  public byte[] getBytes() {
    byte[] buffer = new byte[BUF_SIZE];
    int start = 0;
    buffer[start++] = (byte) type;

    if (gameId != null && playerName != null) {
      buffer[start++] = (byte) gameId.length();
      buffer[start++] = (byte) playerName.length();

      byte[] gameIdBytes = gameId.getBytes();
      System.arraycopy(gameIdBytes, 0, buffer, start, gameIdBytes.length);
      start += gameIdBytes.length;

      byte[] playerNameBytes = playerName.getBytes();
      System.arraycopy(playerNameBytes, 0, buffer, start, playerNameBytes.length);
      start += playerNameBytes.length;
    }

    if (playerIP != null && playerPort != null) {
      byte[] bytesIP = playerIP.getAddress();
      System.arraycopy(bytesIP, 0, buffer, start, bytesIP.length);
      start += bytesIP.length;
      buffer[start++] = (byte) (playerPort >>> 8);
      buffer[start++] = playerPort.byteValue();
    }

    if (direction != null) {
      buffer[start++] = (byte) direction.ordinal();
    }

    if (result != null) {
      buffer[start++] = result.byteValue();
    }

    if (winnerName != null) {
      buffer[start++] = (byte) winnerName.length();
      byte[] winnerNameBytes = winnerName.getBytes();
      System.arraycopy(winnerNameBytes, 0, buffer, start, winnerNameBytes.length);
      start += winnerNameBytes.length;
    }

    if (seqNum != null && apple != null && snake1 != null && snake2 != null) {
      buffer[start++] = seqNum.byteValue();
      buffer[start++] = (byte) apple.getRow();
      buffer[start++] = (byte) apple.getCol();
      byte[] bitMap = snakeBitmap(snake1);
      System.arraycopy(bitMap, 0, buffer, start, bitMap.length);
      start += bitMap.length;
      bitMap = snakeBitmap(snake2);
      System.arraycopy(bitMap, 0, buffer, start, bitMap.length);
      start += bitMap.length;
    }
    int bytesLen = start;
    byte[] msgBytes = new byte[bytesLen];
    System.arraycopy(buffer, 0, msgBytes, 0, bytesLen);
    return msgBytes;
  }

  /**
   * Convert a snake (List of Position) to an bytes array
   *
   * @param snake a list of snake's Position
   * @return the bytes array of the list
   */
  private byte[] snakeBitmap(List<Position> snake) {
    byte[] bitmap = new byte[BOARD_SIZE * BOARD_SIZE / 8];
    for (Position p : snake) {
      int bitIdx = p.getRow() * BOARD_SIZE + p.getCol();
      int byteIdx = bitIdx / 8;
      int bitInByte = 7 - bitIdx % 8;
      bitmap[byteIdx] |= 1 << bitInByte;
    }
    return bitmap;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Message{");
    sb.append("type=" + type);
    if (gameId != null && playerName != null) {
      sb.append(", gameId='" + gameId + '\'' + ", playerName='" + playerName + '\'');
    }
    if (playerIP != null && playerPort != null) {
      sb.append(", playerIp=" + playerIP + ", playerPort=" + playerPort);
    }
    if (direction != null) {
      sb.append(", direction=" + direction.name());
    }
    if (result != null) {
      sb.append(", result=" + (result.equals(DRAW) ? "DRAW" : "HAS WINNER"));
    }
    if (winnerName != null) {
      sb.append(", winnerName='" + winnerName + '\'');
    }
    if (seqNum != null && apple != null && snake1 != null && snake2 != null) {
      sb.append(", seqNum=" + seqNum);
      sb.append(", apple=" + apple);
      sb.append(", snake1=" + snake1);
      sb.append(", snake2=" + snake2);
    }
    sb.append("}");
    return sb.toString();
  }

  public static class Builder {
    private Integer type;
    private String gameId;
    private String playerName;
    private InetAddress playerIP;
    private Integer playerPort;

    private Direction direction;

    private Integer result;
    private String winnerName;

    private Integer seqNum;
    private Position apple;
    private LinkedList<Position> snake1;
    private LinkedList<Position> snake2;

    public Builder(int type) {
      this.type = type;
    }

    public Builder setGameId(String gameId) {
      this.gameId = gameId;
      return this;
    }

    public Builder setPlayerName(String playerName) {
      this.playerName = playerName;
      return this;
    }

    public Builder setPlayerIP(InetAddress playerIP) {
      this.playerIP = playerIP;
      return this;
    }

    public Builder setPlayerPort(Integer playerPort) {
      this.playerPort = playerPort;
      return this;
    }

    public Builder setDirection(Direction direction) {
      this.direction = direction;
      return this;
    }

    public Builder setResult(Integer result) {
      this.result = result;
      return this;
    }

    public Builder setWinnerName(String winnerName) {
      this.winnerName = winnerName;
      return this;
    }

    public Builder setSeqNum(Integer seqNum) {
      this.seqNum = seqNum;
      return this;
    }

    public Builder setApple(Position apple) {
      this.apple = apple;
      return this;
    }

    public Builder setSnake1(LinkedList<Position> snake1) {
      this.snake1 = snake1;
      return this;
    }

    public Builder setSnake2(LinkedList<Position> snake2) {
      this.snake2 = snake2;
      return this;
    }

    public Message build() {
      return new Message(this);
    }
  }
}
