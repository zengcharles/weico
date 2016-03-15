package com.android.base.imageCache;

/**
 * 图片任务
 */
public abstract class ImageTask {
    
    /**  任务 ID 与图片的Url 相同   */
    public String taskId = "";
    
    
    public ImageTask(String taskId) {
        this.taskId = taskId;
    }
    
    
    public String getTaskId() {
        return this.taskId;
    }
    
    
    @Override
    public boolean equals(Object o) {
        ImageTask obj = (ImageTask) o;
        if (taskId.equals(obj.getTaskId())) {
            return true;
        }
        return false;
    }
    
    
    /**  执行任务   */
    public abstract void execute();
    
}