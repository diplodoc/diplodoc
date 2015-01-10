package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.client.ProcessRunDataClient
import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.util.concurrent.ListenableFuture

/**
 * @author yaroslav.yermilov
 */
class Diploexec {

    ProcessRunDataClient processRunDataClient
    ThreadPoolTaskExecutor threadPool

    ProcessRun run(ProcessRun processRun) {
        processRunDataClient.create processRun
        threadPool.submitListenable(new ProcessCall(processRun)).addCallback(new ProcessCallback())
    }
}
