package utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MessageSender {
  private DatagramSocket socket;

  public MessageSender(DatagramSocket socket) {
    this.socket = socket;
  }

  public synchronized void send(Message msg, InetAddress addr, int port) {
    byte[] msgBytes = msg.getBytes();
    DatagramPacket pkt = new DatagramPacket(msgBytes, msgBytes.length, addr, port);
    try {
      socket.send(pkt);
      // System.out.println("Send: " + msg);
    } catch (IOException e) {
      System.err.println("Failed to send message:\n" + msg);
      e.printStackTrace();
    }
  }
}
