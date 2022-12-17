package snakeClient.controller;

import static utils.Config.*;

import utils.Direction;
import utils.Message;
import utils.MessageSender;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import snakeClient.model.GameState;

class DirectionChangeHandler extends KeyAdapter {

  private MessageSender sender;

  public DirectionChangeHandler(MessageSender sender) {
    this.sender = sender;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (GameState.get().getState() != IN_PROGRESS) return;

    switch (e.getKeyCode()) {
      case KeyEvent.VK_LEFT:
        sendMsgChangeDir(Direction.LEFT);
        break;
      case KeyEvent.VK_RIGHT:
        sendMsgChangeDir(Direction.RIGHT);
        break;
      case KeyEvent.VK_UP:
        sendMsgChangeDir(Direction.UP);
        break;
      case KeyEvent.VK_DOWN:
        sendMsgChangeDir(Direction.DOWN);
        break;
    }
  }

  private void sendMsgChangeDir(Direction dir) {
    String gameId = GameState.get().getGameId();
    String playerName = GameState.get().getPlayerName();
    InetAddress serverIP = GameState.get().getServerIP();
    int serverPort = GameState.get().getServerPort();
    Message msgChangeDir =
        new Message.Builder(CHANGE_DIR)
            .setGameId(gameId)
            .setPlayerName(playerName)
            .setDirection(dir)
            .build();
    sender.send(msgChangeDir, serverIP, serverPort);
    System.out.println("Send: " + msgChangeDir);
  }
}
