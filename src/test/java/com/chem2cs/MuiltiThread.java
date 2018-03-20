package com.chem2cs;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Consumer implements Runnable {
    private BlockingQueue<String> q;

    public Consumer(BlockingQueue<String> q) {
        this.q = q;
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println(Thread.currentThread().getName() + ":" + q.take());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Producer implements Runnable {
    private BlockingQueue<String> q;

    public Producer(BlockingQueue<String> q) {
        this.q = q;
    }

    @Override
    public void run() {
        try {
            while (true) {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(100);
                    q.put(String.valueOf(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class MyThread extends Thread {
    private int tid;

    public MyThread(int tid) {
        this.tid = tid;
    }

    public void run() {
        try {

            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                System.out.println(String.format("%d:%d", tid, i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class MuiltiThread {
    public static void testThread() {
        synchronized (Thread.class) {
            for (int i = 0; i < 10; i++) {

                new MyThread(i).start();
                System.out.println("i:" + i);
            }
        }
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
/*            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int j = 0; j < 10; j++) {
                            Thread.sleep(1000);
                            System.out.println(String.format("T2 %d:%d", finalI, j));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();*/
        }
    }

    public static Object obj = new Object();

    public static void testSynchronized1() {
        synchronized (obj) {
            try {
                for (int j = 0; j < 10; j++) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T3 %d", j));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized2() {
        synchronized (obj) {
            try {
                for (int j = 0; j < 10; j++) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T4 %d", j));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized() {
        for (int i = 0; i < 10; i++) {
            testSynchronized1();
            testSynchronized2();
        }
    }


    public static void testBolockingQueue() {
        BlockingQueue<String> q = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q), "Consumer1").start();
        new Thread(new Consumer(q), "Consumer2").start();

    }

    private static ThreadLocal<Integer> threadLocalUserIds = new ThreadLocal<>();
    private static int userId;

    public static void testThreadLocal() {
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        userId = finalI;
                        threadLocalUserIds.set(finalI);
                        Thread.sleep(500);
                        System.out.println("ThreadLocal:" + threadLocalUserIds.get());
                        System.out.println("UserId:" + userId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void testExcutot() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(1000);
                        System.out.println("Executor1:" + i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(1000);
                        System.out.println("Executor2:" + i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void testExcutot2() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 3; i++) {
                        Thread.sleep(1000);
                        System.out.println("Executor1:" + i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 3; i++) {
                        Thread.sleep(1000);
                        System.out.println("Executor2:" + i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        service.shutdown();
        while(!service.isTerminated()){
            try {
                Thread.sleep(500);
                System.out.println("wait for termination");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 3; i++) {
                        Thread.sleep(1000);
                        System.out.println("Executor3:" + i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        //testThread();
        //testSynchronized();
        //testBolockingQueue();
        //testThreadLocal();
        testExcutot2();
    }
}
