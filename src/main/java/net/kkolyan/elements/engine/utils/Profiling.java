package net.kkolyan.elements.engine.utils;

import org.h2.jdbc.JdbcConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author nplekhanov
 */
public class Profiling {
    private static final BlockingQueue<Persistent> tasks = new ArrayBlockingQueue<Persistent>(64*1024);

    private static Connection connection;

    public static void enable() {
        try {
            connection = new JdbcConnection("jdbc:h2:~/.elements/perf", new Properties());
            Database.execute(connection, "drop table if exists watch");
            Database.execute(connection, "" +
                    "create table watch (" +
                    "  name varchar," +
                    "  time number" +
                    ")");

            Database.execute(connection, "drop table if exists num");
            Database.execute(connection, "" +
                    "create table num (" +
                    "  name varchar," +
                    "  num number" +
                    ")");
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Persistent task = tasks.poll(1000L, TimeUnit.MILLISECONDS);
                            if (task == null) {
                                continue;
                            }
                            try {
                                task.persist();
                            } catch (Exception e) {
                                System.out.println("failed to persist "+task);
                                e.printStackTrace(System.out);
                                Thread.sleep(1000L);
                            }
                        } catch (InterruptedException e) {
                            System.out.println(Thread.currentThread().getName()+" interrupted");
                            return;
                        }
                    }
                }
            });
            thread.setName("StopWatch Persistence");
            thread.setDaemon(true);
            thread.start();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static StopWatch startStopWatch(String name) {
        return new StopWatchImpl(name);
    }

    public static void addNumber(String name, double number) {
        if (connection != null) {
            tasks.offer(new Unit(name, number));
        }
    }

    private static class Unit implements Persistent {

        private final String name;
        private final double number;

        public Unit(String name, double number) {
            this.name = name;
            this.number = number;
        }

        @Override
        public void persist() {
            Database.update(connection, "insert into num (name, num) values (?,?)", name, number);
        }
    }

    private static class StopWatchImpl implements StopWatch,Persistent {

        private long startTimeNanos;
        private String name;
        private long time;

        private StopWatchImpl(String name) {
            this.name = name;
            startTimeNanos = System.nanoTime();
        }

        @Override
        public void stop() {
            time = System.nanoTime() - startTimeNanos;
            if (connection != null) {
                tasks.offer(this);
            }
        }

        @Override
        public void persist() {
            Database.update(connection, "insert into watch (name, time) values (?,?)", name, time * 1e-6);
        }

        @Override
        public String toString() {
            return "StopWatch{" +
                    "startTimeNanos=" + startTimeNanos +
                    ", name='" + name + '\'' +
                    ", time=" + time +
                    '}';
        }
    }

    private interface Persistent {
        void persist();
    }
}
