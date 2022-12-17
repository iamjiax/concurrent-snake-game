package snakeServer.controller;

import static utils.Config.*;

import java.util.Map;
import snakeServer.model.GameState;
import utils.Message;
import utils.MessageSender;

public class GameManager {

  public static void manage(Map<String, GameState> games, Message msg, MessageSender sender) {
    switch (msg.getType()) {
      case CREATE:
//        if (games.containsKey(msg.getGameId())) {
//          // send game name occupied information
//          // return;
//        }
        GameState newGame =
            new GameState(
                msg.getGameId(), msg.getPlayerName(), msg.getPlayerIp(), msg.getPlayerPort());
        games.put(newGame.getId(), newGame);
        Message msgWaitPlayer = new Message.Builder(WAIT_PLAYER).build();
        sender.send(msgWaitPlayer, newGame.getPlayer1().getIP(), newGame.getPlayer1().getPort());
        break;
      case JOIN:
        GameState gameToStart = games.get(msg.getGameId());
//        if (gameToStart.getPlayer1().getName().equals(msg.getPlayerName())) {
//          // send player name occupied information
//          return;
//        }
        gameToStart.addPlayer(msg.getPlayerName(), msg.getPlayerIp(), msg.getPlayerPort());
        new Thread(new GameRunner(gameToStart, sender)).start();
        break;
      case CHANGE_DIR:
        GameState game = games.get(msg.getGameId());
        if (!game.isGameOver()) game.updateDirection(msg.getPlayerName(), msg.getDirection());
        break;
    }
  }
}
