package com.belintersat.bot.Bot;

import com.belintersat.bot.Parser.Lists.BelintersatList;
import com.belintersat.bot.Parser.Lists.HappyBirthdayList;
import com.belintersat.bot.ParserXLS.Parser;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Bot extends org.telegram.telegrambots.bots.TelegramLongPollingBot
{
  private long CHAT_TEST_ID = -1001135491699L;
  
  public Bot() {}
  
  public void onUpdateReceived(Update update) {
      System.out.println(update.getMessage().getFrom().getFirstName() + ": " + update
      .getMessage().getText());

    Message message = update.getMessage();
    if ((message != null) && (message.hasText())) {
      sendMsg(message);
    }
  }
  
  public String getBotUsername()
  {
    return "Belintersatbot";
  }
  
  public String getBotToken()
  {
    return "443780992:AAEzMQA70F42iiX3oFjxJ0beFecLo_ialz8";
  }
  
  private void sendMsg(Message message) {
    if ((message.getText().equalsIgnoreCase("что со спутником")) || (message.getText().equalsIgnoreCase("что со спутником?")) || 
      (message.getText().equalsIgnoreCase("что со спутником ?")))
      sendMsgSatellite(message);
    if (message.getText().contains("#"))
      sendMsgBelintersatList(message);
  }
  
  public long getCHAT_TEST_ID() {
    return CHAT_TEST_ID;
  }
  
  private void sendMsgBelintersatList(Message message) {
    HashMap<String, String> belintersatMap = Parser.parseAbonentList("List.xls").getBelintersatMap();
    java.util.Set<String> keySet = Parser.parseAbonentList("List.xls").getBelintersatMap().keySet();
    SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
    for (String key : keySet) {
      if (message.getText().equalsIgnoreCase(key)) {
        sendMessage.setText(belintersatMap.get(key));
        break;
      }
    }
    try {
      sendMessage(sendMessage);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }
  
  public void sendMsgBirthday()
  {
    HappyBirthdayList happyBirthdayList = Parser.parserBirthday("ListBirthday.xls");
    ArrayList<String> list = happyBirthdayList.getUsers();
    String result = "";
    SendMessage sendMessage = new SendMessage().setChatId(Long.valueOf(getCHAT_TEST_ID()));
    
    if (list.size() == 0)
      return;
    for (int i = 0; i < list.size(); i++) {
      if (list.size() == 1) {
        result = list.get(i) + ". ";
      } else if (list.size() > 1) {
        if (i < list.size() - 1) {
          result = result + list.get(i) + ", ";
        } else if (i == list.size() - 1)
          result = result + list.get(i) + ". ";
      }
    }
    System.out.println(result);
    sendMessage.setText("Сегодня День Рождения у " + result + "Поздравляем от коллектива!");
    


    try
    {
      sendMessage(sendMessage);
    }
    catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }
  
  private void sendMsgSatellite(Message message)
  {
    SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
    sendMessage.setText("Сегодня все ок, " + getTimes() + " сутки полета!");
    try {
      sendMessage(sendMessage);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }
  
  private long getTimes() {
    GregorianCalendar calendar = new GregorianCalendar(2016, Calendar.JANUARY, 15);
    GregorianCalendar calendar1 = new GregorianCalendar();
    long different = calendar1.getTimeInMillis() - calendar.getTimeInMillis();
    return (int)(different / 1000*60*60*24);
  }
}
