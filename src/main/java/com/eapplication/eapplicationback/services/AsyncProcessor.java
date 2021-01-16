package com.eapplication.eapplicationback.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * This class provides an async process
 *
 */
@Service
public class AsyncProcessor {

    @Async("threadPoolExecutor")
    public CompletableFuture asyncTask(Callable task) throws Exception {

        return CompletableFuture.completedFuture(task.call());
    }

}
