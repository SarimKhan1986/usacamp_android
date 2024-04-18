package com.usacamp.net;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolUtils {

	private static Runnable task;

	private ThreadPoolUtils() {
	}

	private static final int CORE_POOL_SIZE = 3;

	private static final int MAX_POOL_SIZE = 200;

	private static final int KEEP_ALIVE_TIME = 3000;

	private static final BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(
			10);
	
	private static final ThreadFactory threadFactory = new ThreadFactory() {

		private final AtomicInteger ineger = new AtomicInteger();

		@Override
		public Thread newThread(Runnable arg0) {
			return new Thread(arg0, "MyThreadPool thread:"
					+ ineger.getAndIncrement());
		}
	};

	private static final RejectedExecutionHandler handler = new RejectedExecutionHandler() {
		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			executor.shutdown();
			// 进行重启操作
		}

	};
	// 线程池ThreadPoolExecutor java自带的线程池
	private static final ThreadPoolExecutor threadpool;
	
	static {
		threadpool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
				KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, threadFactory,
				handler);
	}

	public static void execute(Runnable runnable) {
		task = runnable;
		threadpool.execute(runnable);
	}

	public static void clearThreads()
	{
		handler.rejectedExecution(task, threadpool);
		threadpool.remove(task);
	}
}