package snakeServer.model;

import static utils.Config.BOARD_SIZE;

import utils.Direction;
import utils.Position;
import java.net.InetAddress;

public class GameState {
  private String id;
  private Player player1;
  private Player player2;

  private Position apple;
  private boolean gameOver;

  private int stateSeqNum;

  public GameState(String gameId, String player1Name, InetAddress player1IP, int player1Port) {
    id = gameId;
    gameOver = false;
    player1 = new Player(player1Name, player1IP, player1Port);
    player1.initSnake();
    stateSeqNum = 0;
  }

  public void addPlayer(String player2Name, InetAddress player2IP, int player2Port) {
    this.player2 = new Player(player2Name, player2IP, player2Port);
    do {
      player2.initSnake();
    } while (!legalInitSnake2());
    generateApple();
  }

  public Player getPlayer1() {
    return this.player1;
  }

  public Player getPlayer2() {
    return this.player2;
  }

  public String getId() {
    return this.id;
  }

  public Position getApplePosition() {
    return apple;
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public void updateDirection(String playerName, Direction newDir) {
    if (player1.getName().equals(playerName)) {
      player1.updateDirection(newDir);
    } else if (player2.getName().equals(playerName)) {
      player2.updateDirection(newDir);
    }
  }

  public void moveSnake() {
    if (gameOver) return;
    Position nextHead1 = Position.copy(player1.getHead()).move(player1.getDir());
    Position nextHead2 = Position.copy(player2.getHead()).move(player2.getDir());
    checkPlayer(nextHead1, nextHead2);
    if (!player1.isAlive() || !player2.isAlive()) {
      gameOver = true;
      return;
    }
    if (nextHead1.equals(apple)) {
      player1.eatApple(apple);
      player2.moveSnake(nextHead2);
      generateApple();
    } else if (nextHead2.equals(apple)) {
      player2.eatApple(apple);
      player1.moveSnake(nextHead1);
      generateApple();
    } else {
      player1.moveSnake(nextHead1);
      player2.moveSnake(nextHead2);
    }
  }

  private void checkPlayer(Position nextHead1, Position nextHead2) {
    if (player1.getHead().equals(player2.getHead())
        || nextHead1.equals(player2.getHead())
        || nextHead2.equals(player1.getHead())) {
      player1.die();
      player2.die();
    } else if (nextHead1.outBound(BOARD_SIZE) // hit the boundaries
        || player1.isInSnakeButNotTail(nextHead1) // hit itself
        || player2.isInSnakeButNotTail(nextHead1)) { // hit the other snake
      player1.die();
    } else if (nextHead2.outBound(BOARD_SIZE)
        || player1.isInSnakeButNotTail(nextHead2)
        || player2.isInSnakeButNotTail(nextHead2)) {
      player2.die();
    }
  }

  private void generateApple() {
    do {
      apple = Position.random(BOARD_SIZE);
    } while (player1.isInSnake(apple) || player2.isInSnake(apple));
  }

  private boolean legalInitSnake2() {
    for (Position p : player2.getSnake()) {
      if (player1.isInSnake(p) || p.outBound(BOARD_SIZE)) {
        return false;
      }
    }
    return true;
  }

  public boolean isDraw() {
    return !player1.isAlive() && !player2.isAlive();
  }

  public int getNextStateSeqNum() {
    return this.stateSeqNum++;
  }
}
