package utils;

import static utils.Config.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.LinkedList;

public class MessageReader {
  private static final int BUF_SIZE = BOARD_SIZE * 32;
  private DatagramSocket socket;

  public MessageReader(DatagramSocket socket) {
    this.socket = socket;
  }

  public Message readNextMessage() throws IOException {
    byte[] buf = new byte[BUF_SIZE];
    DatagramPacket pkt = new DatagramPacket(buf, BUF_SIZE);
    socket.receive(pkt);
    byte[] data = pkt.getData();

    int startIdx = 0;
    int type = data[startIdx++];
    Message.Builder msgBuilder = new Message.Builder(type);

    if (type == CREATE || type == JOIN || type == CHANGE_DIR) {
      int idLen = data[startIdx++];
      int nameLen = data[startIdx++];
      String gameId = new String(data, startIdx, idLen);
      startIdx += idLen;

      String playerName = new String(data, startIdx, nameLen);
      startIdx += nameLen;

      msgBuilder = msgBuilder.setGameId(gameId).setPlayerName(playerName);
      if (type == CREATE || type == JOIN) {
        byte[] addr = Arrays.copyOfRange(data, startIdx, startIdx + 4);
        InetAddress playerIP = InetAddress.getByAddress(addr);
        startIdx += 4;

        int playerPort = 0;
        playerPort |= (data[startIdx++] & 0xFF) << 8;
        playerPort |= (data[startIdx++] & 0xFF);

        msgBuilder = msgBuilder.setPlayerIP(playerIP).setPlayerPort(playerPort);
      } else if (type == CHANGE_DIR) {
        int dir = data[startIdx++];
        msgBuilder = msgBuilder.setDirection(Direction.values()[dir]);
      }
    } else if (type == GAME_OVER) {
      int result = data[startIdx++];
      msgBuilder = msgBuilder.setResult(result);
      if (result == HAS_WINNER) {
        int nameLen = data[startIdx++];
        String winnerName = new String(data, startIdx, nameLen);
        msgBuilder = msgBuilder.setWinnerName(winnerName);
        startIdx += nameLen;
      }
    } else if (type == IN_PROGRESS) {
      int seqNum = data[startIdx++];
      msgBuilder = msgBuilder.setSeqNum(seqNum);
      int appleRow = data[startIdx++];
      int appleCol = data[startIdx++];
      Position apple = new Position(appleRow, appleCol);
      msgBuilder = msgBuilder.setApple(apple);
      LinkedList<Position> snake1 = readSnakePos(data, startIdx);
      startIdx += BOARD_SIZE * BOARD_SIZE / 8;
      LinkedList<Position> snake2 = readSnakePos(data, startIdx);
      msgBuilder = msgBuilder.setSnake1(snake1).setSnake2(snake2);
    }
    return msgBuilder.build();
  }

  private LinkedList<Position> readSnakePos(byte[] data, int startIdx) {
    LinkedList<Position> snake = new LinkedList<>();
    int bytesLen = BOARD_SIZE * BOARD_SIZE / 8;
    for (int byteIdx = 0; byteIdx < bytesLen; byteIdx++) {
      int curByte = data[startIdx + byteIdx];
      for (int bitInByte = 7; bitInByte >= 0; bitInByte--) {
        if ((curByte & (1 << bitInByte)) != 0) {
          int bitIdx = byteIdx * 8 + (7 - bitInByte);
          int rPos = bitIdx / BOARD_SIZE;
          int cPos = bitIdx % BOARD_SIZE;
          Position p = new Position(rPos, cPos);
          snake.add(p);
        }
      }
    }
    return snake;
  }
}
