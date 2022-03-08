package com.log;

import com.binance.client.model.trade.MyTrade;
import com.binance.client.model.trade.Position;
import com.futures.TP_SL;
import com.strategies.Strategy;
import com.tgbot.AsyncSender;
import com.utils.I18nSupport;
import com.utils.Utils;

import java.io.IOException;
import java.util.Date;

public class TradeLogger {
    public static AsyncSender asyncSender;
    public static Long chatId = null;

    public static void logOpenPosition(Position position) {
        logTgBot(position != null ? I18nSupport.i18n_literals("position.open",
                position.getPositionSide().equals("LONG") ? "\uD83D\uDCC8" : "\uD83D\uDCC9",
                new Date(),
                position.getSymbol(),
                position.getPositionSide(),
                position.getEntryPrice(),
                new Date()) : I18nSupport.i18n_literals("position.not.open", "Position = null!"));
    }

    public static void logClosePosition(MyTrade myTrade) {
        logTgBot(myTrade != null ?
                I18nSupport.i18n_literals("position.close",
                        myTrade.getSymbol(),
                        myTrade.getPositionSide(),
                        myTrade.getPrice(),
                        new Date(myTrade.getTime()),
                        myTrade.getRealizedPnl()) :
                I18nSupport.i18n_literals("position.not.close", "MyTrade = null!"));
    }

    public static void logTP_SLOrders(TP_SL tp_sl) {
        boolean isPost = false;

        if (tp_sl != null) {
            if (tp_sl.getTakeProfitOrder() != null) {
                logTgBot(I18nSupport.i18n_literals("post.take.profit", tp_sl.getTakeProfitOrder().getStopPrice()));
                isPost = true;
            }

            if (tp_sl.getStopLossOrder() != null) {
                logTgBot(I18nSupport.i18n_literals("post.stop.loss", tp_sl.getStopLossOrder().getStopPrice()));
                isPost = true;
            }
        }

        if (!isPost) {
            logTgBot(I18nSupport.i18n_literals("tp.sl.not.posted"));
        }
    }

    public static void logException(Exception exception) {
        logTgBot(I18nSupport.i18n_literals("error.occured", exception));
    }

    public static void logTgBot(String log) {
        if (asyncSender != null && chatId != null) {
            asyncSender.sendTextMsgAsync(log, chatId, "HTML");
        }
    }

    public static void logCloseLog(Strategy strategy, MyTrade myTrade) {
        try {
            Utils.appendStrToFile(Utils.getLogFileName(strategy, myTrade.getSymbol()),
                    I18nSupport.i18n_literals("file.close.log",
                            new Date(myTrade.getTime()),
                            myTrade.getSymbol(),
                            myTrade.getRealizedPnl()));
        } catch (IOException ioException) {
            logException(ioException);
        }
    }
}
