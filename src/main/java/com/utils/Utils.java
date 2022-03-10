package com.utils;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import com.strategies.Strategy;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsExchange;

public class Utils {
    public static void appendStrToFile(String fileName, String str) throws IOException{
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
        out.write(str);
        out.close();
    }

    public static String getLogFileName(Strategy strategy, String ticker) {
        return strategy + "_" + ticker + ".txt";
    }

    public static String readAllFromInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            return "";
        }

        return stringBuilder.toString();
    }

    public static Properties readPropertiesFile(String fileName) {
        Properties properties = null;

        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            properties = new Properties();
            properties.load(fileInputStream);
        } catch (IOException ignored) {

        }

        return properties;
    }

    public static void answerOkToHttpsRequest(HttpExchange httpExchange) {
        HttpsExchange httpsExchange = (HttpsExchange) httpExchange;

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpsExchange.getResponseBody()))) {
            String response = "OK";
            httpsExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            httpsExchange.sendResponseHeaders(200, response.getBytes().length);
            writer.write(response);
        } catch (IOException ignored) {

        }
    }

    public static int getCandlestickIndex(Date date, int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return (calendar.get(Calendar.HOUR_OF_DAY) *
                DateUtils.MINUTES_IN_HOUR +
                calendar.get(Calendar.MINUTE)) / interval;
    }
}
