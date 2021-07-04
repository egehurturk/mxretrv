//package org.mxretrv.threads;
//
//import org.mxretrv.events.Listeners;
//import org.mxretrv.json.JSONDoc;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class Dummy {
//    public static void main(String[] args) { ;
//        IOQueue<JSONDoc> ioQueue = new IOQueue<>();
//        Listeners.register(new IOWorker(ioQueue, "/Users/egehurturk/Development/Mxretrv/testData/input.txt", "/Users/egehurturk/Development/Mxretrv/src/main/java/org/mxretrv/threads/out.json"));
//        ExecutorService executorService = Executors.newFixedThreadPool(3);
//        executorService.execute(new MXWorker(ioQueue, new ArrayList<>(Arrays.asList("octeth.com", "yahoo.com", "google.com"))));
//        executorService.execute(new MXWorker(ioQueue, new ArrayList<>(Arrays.asList("amazon.com", "ebay.com", "stackoverflow.com"))));
//        executorService.execute(new MXWorker(ioQueue, new ArrayList<>(Arrays.asList("youtube.com", "reddit.com", "medium.com"))));
//        executorService.shutdown();
//    }
//}
//
//
//
///*
//
//
//
//
//
// */
