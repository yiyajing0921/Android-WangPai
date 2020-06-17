package com.woaiwangpai.iwb.utils.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 8:53
 * @Email : yiyajing8023@163.com
 * @Description :这是一个线程池的工具类，在用到线程的时候可以直接类名加方法名使用
 * Timer timer;
 * timer = new Timer();
 * // timer.schedule(new TimerTask() {
 * // @Override
 * // public void run() {
 * // handler.sendEmptyMessage(1);
 * // }
 * // },0,500);//第二个参数是隔多少秒之后开始显示，第三个是隔多久显示下一个
 * ThreadPoolManager
 * .getInstance()
 * .getScheduledExcutorService()
 * .scheduleAtFixedRate(new Runnable() {
 *
 * public void run() {
 * handler.sendEmptyMessage(1);
 * }
 * },0,500, TimeUnit.MILLISECONDS);//第二个参数是隔多少秒之后开始显示，第三个是隔多久显示下一个
 * }
 *
 * protected void onDestroy() {
 * timer.cancel();
 * super.onDestroy();
 * }
 * }
 */

public class ThreadPoolManager {
    /**
     * 线程执行器
     **/
    private static ExecutorService executorService = null;
    /**
     * 固定5个线程
     **/
    private static int nThreads = 5;
    /**
     * 单例
     **/
    private static ThreadPoolManager taskExecutorPool = null;

    /** 初始化线程池 **/
    static {
        taskExecutorPool = new ThreadPoolManager(nThreads * getNumCores());
    }

    /**
     * 构造函数
     **/
    private ThreadPoolManager(int threads) {
    //executorService = Executors.newFixedThreadPool(threads);
        executorService =Executors.newScheduledThreadPool(threads);
    }

    /**  runAble()
     *    pool.execute(new Runnable() {
     *             @Override
     *             public void run() {
     *                 String threadName = Thread.currentThread().getName();
     *                 for (Task2Bean.DataBean.TaskScreenPushBean dataBean : taskScreenPushBeans) {
     *                     //在子线程当中发送数据给主线程
     *                     EventBus.getDefault().post(dataBean);
     *                     try {
     *                         Thread.sleep(3000);//3秒发送一个
     *                     } catch (InterruptedException e) {
     *                         e.printStackTrace();
     *                     }
     *                 }
     *                 LogUtils.e("mhy", "线程：" + threadName + ",正在执行任务");
     *             }
     *         });
     * @return
     */
    public  static ExecutorService runAble(){
        //maximumPoolSize设置为2 ，拒绝策略为AbortPolic策略，直接抛出异常
        ExecutorService pool = new ThreadPoolExecutor(
                1,
                2,
                1000L,
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        return pool;
    }
    /**
     * 取得单例
     *
     * @return
     */
    public static ThreadPoolManager getInstance() {
        return taskExecutorPool;
    }

    /**
     * 取得线程执行器
     *
     * @return
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * 取得周期性线程执行器
     *
     * @return
     */
    public ScheduledExecutorService getScheduledExcutorService() {
        return (ScheduledExecutorService) executorService;
    }

    /**
     * 获得手机cup个数
     *
     * @return
     */
    public static int getNumCores() {
        int threadCount = Runtime.getRuntime().availableProcessors();
        return threadCount;
    }
}
