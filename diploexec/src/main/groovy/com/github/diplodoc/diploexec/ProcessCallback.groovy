package com.github.diplodoc.diploexec

import org.springframework.util.concurrent.ListenableFutureCallback

/**
 * @author yaroslav.yermilov
 */
class ProcessCallback implements ListenableFutureCallback<ProcessCallResult> {
    
    @Override
    void onFailure(Throwable ex) {
        assert false : 'not implemented yet'
    }

    @Override
    void onSuccess(ProcessCallResult result) {
        assert false : 'not implemented yet'
    }
}
