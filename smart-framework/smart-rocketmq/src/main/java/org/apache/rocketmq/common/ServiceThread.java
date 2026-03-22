package org.apache.rocketmq.common;

import org.apache.rocketmq.logging.org.slf4j.Logger;
import org.apache.rocketmq.logging.org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 覆盖重写ServiceThread，解决运行时log访问失效的问题
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-02 15:53
 * @since 5.0.0
 */
public abstract class ServiceThread implements Runnable {
    protected static final Logger log = LoggerFactory.getLogger("RocketmqCommon");
    private static final long JOIN_TIME = 90000L;
    protected Thread thread;
    protected final CountDownLatch2 waitPoint = new CountDownLatch2(1);
    protected volatile AtomicBoolean hasNotified = new AtomicBoolean(false);
    protected volatile boolean stopped = false;
    protected boolean isDaemon = false;
    private final AtomicBoolean started = new AtomicBoolean(false);

    public abstract String getServiceName();

    public void start() {
        log.info("Try to start service thread:{} started:{} lastThread:{}", new Object[]{this.getServiceName(), this.started.get(), this.thread});
        if (this.started.compareAndSet(false, true)) {
            this.stopped = false;
            this.thread = new Thread(this, this.getServiceName());
            this.thread.setDaemon(this.isDaemon);
            this.thread.start();
            log.info("Start service thread:{} started:{} lastThread:{}", new Object[]{this.getServiceName(), this.started.get(), this.thread});
        }
    }

    public void shutdown() {
        this.shutdown(false);
    }

    public void shutdown(boolean interrupt) {
        log.info("Try to shutdown service thread:{} started:{} lastThread:{}", new Object[]{this.getServiceName(), this.started.get(), this.thread});
        if (this.started.compareAndSet(true, false)) {
            this.stopped = true;
            log.info("shutdown thread[{}] interrupt={} ", this.getServiceName(), interrupt);
            this.wakeup();

            try {
                if (interrupt) {
                    this.thread.interrupt();
                }

                long beginTime = System.currentTimeMillis();
                if (!this.thread.isDaemon()) {
                    this.thread.join(this.getJoinTime());
                }

                long elapsedTime = System.currentTimeMillis() - beginTime;
                log.info("join thread[{}], elapsed time: {}ms, join time:{}ms", new Object[]{this.getServiceName(), elapsedTime, this.getJoinTime()});
            } catch (InterruptedException e) {
                log.error("Interrupted", e);
            }

        }
    }

    public long getJoinTime() {
        return 90000L;
    }

    public void makeStop() {
        if (this.started.get()) {
            this.stopped = true;
            log.info("makestop thread[{}] ", this.getServiceName());
        }
    }

    public void wakeup() {
        if (this.hasNotified.compareAndSet(false, true)) {
            this.waitPoint.countDown();
        }

    }

    protected void waitForRunning(long interval) {
        if (this.hasNotified.compareAndSet(true, false)) {
            this.onWaitEnd();
        } else {
            this.waitPoint.reset();

            try {
                this.waitPoint.await(interval, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                log.error("Interrupted", e);
            } finally {
                this.hasNotified.set(false);
                this.onWaitEnd();
            }

        }
    }

    protected void onWaitEnd() {
    }

    public boolean isStopped() {
        return this.stopped;
    }

    public boolean isDaemon() {
        return this.isDaemon;
    }

    public void setDaemon(boolean daemon) {
        this.isDaemon = daemon;
    }
}
