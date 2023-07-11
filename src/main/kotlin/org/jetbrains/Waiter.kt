package org.jetbrains

import java.util.concurrent.TimeUnit

class Waiter {
    fun waitForUpdated(isInUpdate: Boolean, timeoutSeconds: Int = 15) {
        val startTime = System.currentTimeMillis()
        val endTime = startTime + TimeUnit.SECONDS.toMillis(timeoutSeconds.toLong())
        do {
            try {
                return assert(isInUpdate.equals(false))
            } catch (e: Throwable) {
                if (e is AssertionError) {
                    Thread.sleep(1000)
                } else {
                    throw e
                }
            }
        } while (System.currentTimeMillis() < endTime)
        return
    }
}