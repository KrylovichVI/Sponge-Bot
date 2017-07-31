package com.belintersat.bot.Bot;
import com.belintersat.bot.Parser.Lists.HappyBirthdayList;
import com.belintersat.bot.ParserXLS.Parser;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendSticker;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Sticker;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Bot extends TelegramLongPollingBot{
    private long CHAT_TEST_ID = -1001135491699L;
    private long CHAT_NKU_ID = -120277389L;
    private String[] url = {"http://belintersat.by/images/Telegrambot/happy_summer.jpg",
            "http://belintersat.by/images/Telegrambot/happy_autumn.jpg",
            "http://belintersat.by/images/Telegrambot/happy_spring.jpg",
            "http://belintersat.by/images/Telegrambot/happy_winter.jpg"};

    private String[] money = {"А ты их заработал?", "Тебе да!", "Тебе нет!", "А ты кто?", "Ты помоему здесь вообще не работаешь."};

    @Override
    public void onUpdateReceived(Update update){
        System.out.println(update.getMessage().getFrom().getFirstName() + ": " + update.getMessage().getText());
        Message message = update.getMessage();

        if ((message != null) && (message.hasText())){
            //System.out.println(message.getChatId());
            System.out.println(message.getSticker());
          sendMsg(message);
        }
    }


    @Override
    public String getBotUsername(){
        return "Sponge Bot";
    }

    @Override
    public String getBotToken(){
        return "443780992:AAEzMQA70F42iiX3oFjxJ0beFecLo_ialz8";
    }


    private void sendMsg(Message message){
        if ((message.getText().equalsIgnoreCase("что со спутником")) || (message.getText().equalsIgnoreCase("что со спутником?")) ||
          (message.getText().equalsIgnoreCase("что со спутником ?")))
            sendMsgSatellite(message);
        if (message.getText().contains("#") && !message.getText().contains("#list"))
            sendMsgBelintersatList(message);
        if(message.getText().equalsIgnoreCase("Кто проживает на дне океана?") || message.getText().equalsIgnoreCase("Кто проживает на дне океана") ||
                message.getText().equalsIgnoreCase("Кто проживает на дне океана ?"))
            sendMsgBotName(message);
        if(message.getText().equalsIgnoreCase("#list")){
            sendAbonentsList(message);
        }
        if(message.getText().contains("деньги") || message.getText().contains("Деньги") || message.getText().contains("Денег") || message.getText().contains("денег")){
           sendMsgMoney(message);
        }
        if(message.getText().contains("совещание в") || message.getText().contains("Совещание в")){
            sendMsgMeeting(message);
        }


    }

    public long getCHAT_TEST_ID(){
        return CHAT_TEST_ID;
    }

    public long getCHAT_NKU_ID() {
        return CHAT_NKU_ID;
    }

    private void sendMsgBelintersatList(Message message) {
        HashMap<String, String> belintersatMap = Parser.parseAbonentList("List.xls").getBelintersatMap();
        Set<String> keySet = Parser.parseAbonentList("List.xls").getBelintersatMap().keySet();
        SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
        for (String key : keySet) {
            if (message.getText().equalsIgnoreCase(key)){
            sendMessage.setText(belintersatMap.get(key));
            break;
            }
        }
        if(sendMessage.getText() == null){
            sendMessage.setText("Досвидули!");
        }
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
  
    public void sendMsgBirthday(Date date){
        HappyBirthdayList happyBirthdayList = Parser.parserBirthday("ListBirthday.xls");
        ArrayList<String> list = happyBirthdayList.getUsers();
        String result = "";
        SendMessage sendMessage = new SendMessage().setChatId(getCHAT_NKU_ID());

    
        if (list.size() == 0)
            return;
        for (int i = 0; i < list.size(); i++) {
            if (list.size() == 1) {
                result = list.get(i) + ". ";
            } else if (list.size() > 1) {
            if (i < list.size() - 1) {
            result = result + list.get(i) + ", ";
            } else if (i == list.size() )
            result = result + list.get(i) + ". ";
            }
        }
        System.out.println(result);
        sendMessage.setText("  Сегодня День Рождения у " + result + "Поздравляю Вас с Днем Рождения! " +
        "От всей души желаю чтоб в жизни всегда было много радостных событий и ярких впечатлений, новых перспектив и начинаний, увлекательных путешествий и интересных встреч. " +
                "Пусть удача и успех сопутствуют во всем! " +
                "Sponge Bot и команда Belintersat");
        SendPhoto sendPhoto = new SendPhoto()
                                    .setChatId(getCHAT_NKU_ID());
        if(date.getMonth() == Calendar.JUNE || date.getMonth() == Calendar.JULY || date.getMonth() == Calendar.AUGUST ){
            sendPhoto.setPhoto(url[0]);
        } else if(date.getMonth() == Calendar.SEPTEMBER || date.getMonth() == Calendar.OCTOBER || date.getMonth() == Calendar.NOVEMBER ){
            sendPhoto.setPhoto(url[1]);
        } else if(date.getMonth() == Calendar.MARCH || date.getMonth() == Calendar.APRIL || date.getMonth() == Calendar.MAY ) {
            sendPhoto.setPhoto(url[2]);
        }  else if(date.getMonth() == Calendar.DECEMBER || date.getMonth() == Calendar.JANUARY || date.getMonth() == Calendar.FEBRUARY ) {
            sendPhoto.setPhoto(url[3]);
        }

        try{
            sendMessage(sendMessage);
            sendPhoto(sendPhoto);
        }
        catch(TelegramApiException e) {
            e.printStackTrace();
        }
    }
  
        private void sendMsgBotName(Message message){
            SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
            sendMessage.setText(getBotUsername());
            try{
                sendMessage(sendMessage);
            }catch(TelegramApiException e){
                e.printStackTrace();
            }
        }

        private void sendMsgSatellite(Message message){
            SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
            sendMessage.setText("Сегодня все ок, " + getTimes() + " сутки полета!");
            try{
                sendMessage(sendMessage);
            }catch(TelegramApiException e){
                e.printStackTrace();
            }
        }

        private void sendAbonentsList(Message message){
            SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
            HashMap<String, String> belintersatList = Parser.parseSipAbonents("Abonents.xls").getBelintersatMap();
            Set<String> keySet = belintersatList.keySet();
            String result = "";
            for(String key: keySet){
                result += key + " - " +  belintersatList.get(key) + "\n";
            }
            sendMessage.setText(result);
            try {
                sendMessage(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }

        private void sendMsgMoney(Message message) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
            Random random = new Random();
            String str = dateFormat.format(date);
            if( str.equals("5") || str.equals("6") || str.equals("20") || str.equals("21")){
                SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
                sendMessage.setText(money[random.nextInt(money.length)]);

                try {
                    sendMessage(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        }

        private void sendMsgMeeting(Message message) {
            SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
            sendMessage.setText("Меня не будет.");
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
            System.out.println(different);
            return (int)(different / (1000*60*60*24));
        }
}
