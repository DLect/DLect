/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.data;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.TimeUnit;
import org.dlect.controller.MainController;
import org.dlect.controller.data.DatabaseHandler.DatabaseHandlerEventID;
import org.dlect.events.Event;
import org.dlect.events.EventListener;

/**
 *
 * @author lee
 */
public class DatabaseSavingHandler implements EventListener {

    private final Runnable SAVING_RUNNABLE = new Runnable() {

        @Override
        public void run() {
            if (mc.getDatabaseHandler().getDatabase() != null) {
                mc.getDatabaseHandler().saveDatabase();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    // Ignore error
                }
            }
        }
    };

    private final MainController mc;
    private final ArrayBlockingQueue<Runnable> executorQueue = new ArrayBlockingQueue<>(1);
    private final ThreadPoolExecutor savingService = new ThreadPoolExecutor(1, 1,
                                                                            5, TimeUnit.SECONDS,
                                                                            executorQueue,
                                                                            new DiscardPolicy());

    public DatabaseSavingHandler(MainController mc) {
        this.mc = mc;
    }

    @Override
    public void processEvent(Event e) {
        if (!e.getEventID().equals(DatabaseHandlerEventID.DATABASE_SAVED)
            && mc.getDatabaseHandler().getDatabase() != null) {
            savingService.submit(SAVING_RUNNABLE);
        }
    }

    public static void registerOn(MainController mc) {
        DatabaseSavingHandler sdu = new DatabaseSavingHandler(mc);
        mc.addListener(sdu);
    }

}
