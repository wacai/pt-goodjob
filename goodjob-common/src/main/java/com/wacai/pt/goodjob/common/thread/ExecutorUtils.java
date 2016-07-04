package com.wacai.pt.goodjob.common.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xuanwu on 2015/4/7.
 */
public class ExecutorUtils {
    private static Logger logger = LoggerFactory.getLogger(ExecutorUtils.class);

    public static <T> List<T> batchExecute(Executor executor, Collection<Callable<T>> tasks) {
        return batchExecute(executor, tasks, false, false, false);
    }

    public static <T> List<T> batchExecute(Executor executor, Collection<Callable<T>> tasks,
                                           long singleTaskTimeout) {
        CompletionService<SequencedObject<T>> service = new ExecutorCompletionService<SequencedObject<T>>(
            executor);
        List<Future<SequencedObject<T>>> futures = submitSequencedTasks(tasks, service);
        int taskSize = tasks.size();
        List<SequencedObject<T>> unsequencedResults = new ArrayList<SequencedObject<T>>(taskSize);

        long begin = System.currentTimeMillis();
        for (int i = 0; i < taskSize; i++) {
            try {
                long remainingTime = singleTaskTimeout - (System.currentTimeMillis() - begin);
                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Future<SequencedObject<T>> future = service.poll(remainingTime,
                    TimeUnit.MILLISECONDS);
                if (future == null) {
                    if (logger.isWarnEnabled()) {
                        logger
                            .warn("Timout occoured in task execution, a null result will be added to results");
                    }
                    unsequencedResults.add(null);
                } else {
                    futures.remove(future);
                    unsequencedResults.add(future.get());
                }
            } catch (Throwable e) {
                if (logger.isErrorEnabled()) {
                    logger
                        .error(
                            "Exception occured during task execution, a null result will be added to results",
                            e);
                }
                unsequencedResults.add(null);
            }
        }

        cancelTimeouted(futures);
        return collect(resequence(unsequencedResults));
    }

    private static <T> List<Future<SequencedObject<T>>> submitSequencedTasks(Collection<Callable<T>> tasks,
                                                                             CompletionService<SequencedObject<T>> service) {

        final AtomicInteger sequence = new AtomicInteger(-1);
        List<Future<SequencedObject<T>>> futures = new ArrayList<Future<SequencedObject<T>>>();

        for (final Callable<T> task : tasks) {
            final int sequenceNumber = sequence.incrementAndGet();

            Future<SequencedObject<T>> future = service.submit(new Callable<SequencedObject<T>>() {
                @Override
                public SequencedObject<T> call() throws Exception {
                    T result = task.call();
                    return new SequencedObject<T>(sequenceNumber, result);
                }
            });

            futures.add(future);
        }

        return futures;
    }

    private static <T> void cancelTimeouted(List<Future<SequencedObject<T>>> futures) {
        for (Future<SequencedObject<T>> future : futures) {
            future.cancel(false);
        }
    }

    private static <T> List<SequencedObject<T>> resequence(List<SequencedObject<T>> unsequencedResults) {
        int size = unsequencedResults.size();
        List<SequencedObject<T>> resequenced = new ArrayList<SequencedObject<T>>(size);

        for (int index = 0; index < size; index++) {
            SequencedObject<T> found = null;
            for (SequencedObject<T> result : unsequencedResults) {
                if (result != null && result.getSequenceNumber() == index) {
                    found = result;
                    break;
                }
            }
            resequenced.add(found);
            unsequencedResults.remove(found);
        }

        return resequenced;
    }

    private static <T> List<T> collect(List<SequencedObject<T>> sequenced) {
        List<T> collected = new ArrayList<T>(sequenced.size());
        for (SequencedObject<T> element : sequenced) {
            if (element != null) {
                collected.add(element.getResult());
            } else {
                collected.add(null);
            }
        }
        return collected;
    }

    private static final class SequencedObject<T> {

        private static final long serialVersionUID = 7756056501654415367L;
        private final int         sequenceNumber;
        private final T           result;

        public SequencedObject(int sequenceNumber, T result) {
            this.sequenceNumber = sequenceNumber;
            this.result = result;
        }

        public T getResult() {
            return result;
        }

        public int getSequenceNumber() {
            return sequenceNumber;
        }
    }

    /**
     * Batch execute given tasks in given executor
     */
    public static <T> List<T> batchExecute(Executor executor, Collection<Callable<T>> tasks,
                                           boolean eliminateNullResult,
                                           boolean ignoreInterruptedException,
                                           boolean ignoreExecutionException) {

        Collection<Future<T>> futures = submitTasks(executor, tasks);
        List<T> results = new ArrayList<T>(tasks.size());

        for (Future<T> future : futures) {
            try {
                T result = future.get();
                if (eliminateNullResult && result == null) {
                    continue;
                }
                results.add(result);
            } catch (InterruptedException e) {
                if (ignoreInterruptedException) {
                    if (logger.isErrorEnabled()) {
                        logger.error(e.getMessage(), e);
                    }
                } else {
                    Thread.currentThread().interrupt();
                }
            } catch (ExecutionException e) {
                if (ignoreExecutionException) {
                    if (logger.isErrorEnabled()) {
                        logger.error(e.getMessage(), e);
                    }
                } else {
                    throw new RuntimeException("Execution Exception [" + e.getMessage() + "]", e);
                }
            }
        }

        return results;
    }

    private static <T> List<Future<T>> submitTasks(Executor executor, Collection<Callable<T>> tasks) {
        CompletionService<T> service = new ExecutorCompletionService<T>(executor);
        List<Future<T>> futures = new ArrayList<Future<T>>(tasks.size());

        for (Callable<T> task : tasks) {
            Future<T> future = service.submit(task);
            futures.add(future);
        }

        return futures;
    }
}
