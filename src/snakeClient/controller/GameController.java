package snakeClient.controller;

import static utils.Config.*;

import utils.Message;
import utils.MessageSender;
import java.net.InetAddress;
import javax.swing.JFrame;
import snakeClient.model.GameState;
import snakeClient.view.Board;

public class GameController extends JFrame {
  private final Board board;
  private MessageSender sender;

  public GameController(
      String gameId, String playerName, InetAddress serverIP, MessageSender sender) {
    this.sender = sender;
    GameState.get().init(gameId, playerName, serverIP, SERVER_PORT);
    setTitle("Snake Game");
    board = new Board();
    add(board);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // Size the window so that all its contents are at or above their
    // preferred sizes.
    pack();
    addKeyListener(new DirectionChangeHandler(sender));
  }

  public void update(Message msg) {
    updateGameState(msg);
    board.repaint();
  }

  public void updateGameState(Message msg) {
    int type = msg.getType();
    switch (type) {
      case WAIT_PLAYER:
        GameState.get().setState(WAIT_PLAYER);
        break;
      case WAIT_START:
        GameState.get().setState(WAIT_START);
        break;
      case IN_PROGRESS:
        GameState.get().setState(IN_PROGRESS);
        GameState.get().setApple(msg.getApple());
        GameState.get().setSnake1(msg.getSnake1());
        GameState.get().setSnake2(msg.getSnake2());
        break;
      case GAME_OVER:
        GameState.get().setState(GAME_OVER);
        GameState.get().setWinnerName(msg.getWinnerName());
        break;
    }
  }
}
