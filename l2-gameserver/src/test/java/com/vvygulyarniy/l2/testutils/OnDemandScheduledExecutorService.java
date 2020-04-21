package com.vvygulyarniy.l2.testutils;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static org.mockito.Mockito.mock;

/**
 * Phoen-X on 18.03.2017.
 */
public class OnDemandScheduledExecutorService implements ScheduledExecutorService {
    private List<Runnable> scheduledTasks = new ArrayList<>();

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        scheduledTasks.add(command);
        return mock(ScheduledFuture.class);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void shutdown() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public List<Runnable> shutdownNow() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public boolean isShutdown() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public boolean isTerminated() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Future<?> submit(Runnable task) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
                                         long timeout,
                                         TimeUnit unit) throws InterruptedException {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks,
                           long timeout,
                           TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void execute(Runnable command) {
        throw new RuntimeException("Not implemented yet");
    }

    public void runScheduledTasks() {
        scheduledTasks.forEach(Runnable::run);
    }
}
