package com.android.base.imageCache;


import java.util.LinkedList;

import android.util.Log;

/**
 * 图片任务的执行
 */
public class ImageTaskExecutor {
    
    /**  存放任务的链表，first-in last-out   */
    private LinkedList<ImageTask> mTaskQueue = null;
    
    /**  执行任务的线程  */
    private ThreadUnit mThreadUnit = null;

    /**  执行任务的间隔时长  */
    public static final long  WAIT_PERIOD = 50L;
    
    
    /**
     * 添加新任
     * @param task
     * @return 是否添加成功
     */
    public boolean addNewTask(ImageTask task) {
        if (mThreadUnit == null) {
            mThreadUnit = new ThreadUnit();
            mTaskQueue = new LinkedList<ImageTask>();
            new Thread(mThreadUnit).start();
        }
        return mTaskQueue.offer(task);
    }
    
    
    class ThreadUnit implements Runnable {
        
        public boolean isRunning = false;
        private ImageTask  task = null;
        
        @Override
        public void run() {
            try {
                isRunning = true;
                while (isRunning) {
                    while (mTaskQueue != null && mTaskQueue.isEmpty()) {
                        try {
                            Thread.sleep(WAIT_PERIOD);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (mTaskQueue != null) {
                        task = mTaskQueue.removeLast();  //取出链表中的任务
                        if (task != null) {
                            task.execute();
                        }
                    }
                } //end while
            } catch (Exception e) {
                Log.e("","Error --> ThreadPool$ThreadUnit.run(): e = " + e.toString());
            }
        } // end run
    }
    
    
    /**
     * 终止任务的执行
     */
    public void terminateTaskThread() {
        if (mThreadUnit != null) {
            mThreadUnit.isRunning = false;
        }
        if (mTaskQueue != null) {
            mTaskQueue.clear();
        }
        mThreadUnit = null;
        mTaskQueue = null;
    }
}

