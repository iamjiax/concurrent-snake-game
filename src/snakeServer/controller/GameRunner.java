package snakeServer.controller;

import static utils.Config.*;

import utils.Message;
import snakeServer.model.GameState;
import utils.MessageSender;
import snakeServer.model.Player;

public class GameRunner implements Runnable {

  private GameState game;
  private MessageSender sender;

  public GameRunner(GameState game, MessageSender sender) {
    this.game = game;
    this.sender = sender;
  }

  @Override
  public void run() {
    sendWaitStartMsg();
    try {
      Thread.sleep(INIT_DELAY);
    } catch (InterruptedException e) {
    }
    while (!game.isGameOver()) {
      game.moveSnake();
      sendGameState();
      try {
        Thread.sleep(GAME_SPEED_MS);
      } catch (InterruptedException e) {
      }
    }
    sendGameOverMsg();
  }

  public void sendWaitStartMsg() {
    Message msgWaitStart = new Message.Builder(WAIT_START).build();
    sender.send(msgWaitStart, game.getPlayer1().getIP(), game.getPlayer1().getPort());
    sender.send(msgWaitStart, game.getPlayer2().getIP(), game.getPlayer2().getPort());
  }

  public void sendGameOverMsg() {
    if (!game.isGameOver()) return;
    if (game.isDraw()) {
      Message msgGameOver = new Message.Builder(GAME_OVER).setResult(DRAW).build();
      sender.send(msgGameOver, game.getPlayer1().getIP(), game.getPlayer1().getPort());
      sender.send(msgGameOver, game.getPlayer2().getIP(), game.getPlayer2().getPort());
      return;
    }
    Player winner;
    if (game.getPlayer1().isAlive()) {
      winner = game.getPlayer1();
    } else {
      winner = game.getPlayer2();
    }
    Message msgGameOver =
        new Message.Builder(GAME_OVER)
            .setResult(HAS_WINNER)
            .setWinnerName(winner.getName())
            .build();
    sender.send(msgGameOver, game.getPlayer1().getIP(), game.getPlayer1().getPort());
    sender.send(msgGameOver, game.getPlayer2().getIP(), game.getPlayer2().getPort());
  }

  public void sendGameState() {
    int seqNum = game.getNextStateSeqNum();
    Message msgState1 =
        new Message.Builder(IN_PROGRESS)
            .setSeqNum(seqNum)
            .setApple(game.getApplePosition())
            .setSnake1(game.getPlayer1().getSnake())
            .setSnake2(game.getPlayer2().getSnake())
            .build();
    Message msgState2 =
        new Message.Builder(IN_PROGRESS)
            .setSeqNum(seqNum)
            .setApple(game.getApplePosition())
            .setSnake1(game.getPlayer2().getSnake())
            .setSnake2(game.getPlayer1().getSnake())
            .build();
    sender.send(msgState1, game.getPlayer1().getIP(), game.getPlayer1().getPort());
    sender.send(msgState2, game.getPlayer2().getIP(), game.getPlayer2().getPort());
  }
}
