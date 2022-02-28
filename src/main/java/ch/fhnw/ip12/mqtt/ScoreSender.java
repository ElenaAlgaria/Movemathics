package ch.fhnw.ip12.mqtt;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.hivemq.client.mqtt.MqttClientState;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.fhnw.ip12.gui.model.Player;

/**
 * Class description: Singleton class to connect and transfer data to the mqtt
 * server
 */

public class ScoreSender {
  private static Logger scoreSenderLogger = LogManager.getLogger(ScoreSender.class);

  private Player player;

  private static final String MQTT_BROKER = "broker.hivemq.com";
  private static final String TOPIC_SCORE = "fhnw/ip12/scoreboard/score";
  private static final String GAME_NAME = "MoveMathics";

  private final Mqtt5AsyncClient client;
  private static final ScoreSender INSTANCE = new ScoreSender();

  /**
   * @return instance of ScoreSender
   */
  public static ScoreSender getInstance() {
    scoreSenderLogger.traceEntry("getInstance");
    return INSTANCE;
  }

  /**
   * Singelton constructor
   */
  private ScoreSender() {
    client = createMqttClient();
    player = Player.getInstance();
  }

  /**
   * send message to the mqtt broker
   */
  public void sendResults() {
    Executor executor = Executors.newSingleThreadExecutor();
    executor.execute(() -> {
      scoreSenderLogger.trace("begin sending reuslts");
      connectSender(this.client);
      scoreSenderLogger.debug("sending message: Player:{}, Score:{}", player.getName(), player.getPoints());
      this.client.publishWith().topic(TOPIC_SCORE)
          .payload((GAME_NAME + ";" + player.getName() + ";" + player.getPoints()).getBytes()).send();
      this.client.disconnect();
      scoreSenderLogger.trace("end sending results - disconnect");
    });
  }

  /**
   * Connect to the mqtt client - process will be killed after 1 second
   * 
   * @param senderClient
   */
  private void connectSender(Mqtt5AsyncClient senderClient) {
    scoreSenderLogger.debug("connecting client");

    // Getting the current date
    Date date = new Date();
    long timeMilli = date.getTime();
    senderClient.connect();
    while (senderClient.getState() != MqttClientState.CONNECTED) {
      if (date.getTime() > timeMilli + 1000) {
        scoreSenderLogger.warn("client connecting failed");
        return;
      }
    }
    scoreSenderLogger.debug("connection etablished");
  }

  /**
   * create a mqtt client
   * 
   * @return Mqtt5AsyncClient
   */
  private Mqtt5AsyncClient createMqttClient() {
    scoreSenderLogger.traceEntry("create mqtt client");
    return Mqtt5Client.builder().serverHost(MQTT_BROKER).identifier(UUID.randomUUID().toString()).buildAsync();
  }

}
