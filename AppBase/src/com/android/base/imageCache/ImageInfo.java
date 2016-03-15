package com.android.base.imageCache;


public class ImageInfo {
    
    /**  图片的Url地址  */
    private String imageUrl = "";
    
    /**  使用Bitmap时的时间戳 */
    private long timeStamp = 0L;

    
    public ImageInfo(String url, long time) {
        this.imageUrl = url;
        this.timeStamp = time;
    }
    
    /**
     * 重写equals方法
     */
    @Override
    public boolean equals(Object o) {
    	if (o == null){
    		return false;
    	}else if (this == o){
    		return true;
    	}else if (this.getClass() != o.getClass()){
    		return false;
    	}else if (!(o instanceof ImageInfo)){
    		return false;
    	}
        ImageInfo imgInfo = (ImageInfo) o;
        if (imageUrl.equals(imgInfo.getImageUrl())) {
            return true;
        }
        return false;
    }


    public String getImageUrl() {
        return imageUrl;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public long getTimeStamp() {
        return timeStamp;
    }


    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    
}
