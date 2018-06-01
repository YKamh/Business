package com.myself.vuandroidadsdk.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.myself.vuandroidadsdk.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by Kamh on 2018/6/1.
 * @function    初始化UniversalImageLoader，并用来加载网络图片
 */

public class ImageLoaderManager {

    private static final int THREAD_COUNT = 4;//最大线程数
    private static final int PRIORITY = 2;//优先级
    private static final int DISK_SIZE = 50 * 1024;//最大缓存量
    private static final int CONNECT_TIME_OUT = 5*1000;//连接超时时间
    private static final int READ_TIME_OUT = 30*1000; //读取超时时间

    private static ImageLoader mImageLoader = null;
    private static ImageLoaderManager mInstance = null;

    public static ImageLoaderManager getInstance(Context context){
        if (mInstance == null){
            synchronized (ImageLoaderManager.class){
                if (mInstance == null){
                    mInstance = new ImageLoaderManager(context);
                }
            }
        }
        return mInstance;
    }

    private ImageLoaderManager(Context context) {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(THREAD_COUNT)
                .threadPriority(Thread.NORM_PRIORITY - PRIORITY)
                .denyCacheImageMultipleSizesInMemory()//防止缓存多套尺寸图片到内存中
                .memoryCache(new WeakMemoryCache())//使用弱引用内存缓存
                .diskCacheSize(DISK_SIZE)//分配硬盘缓存大小
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//使用MD5命名缓存文件
                .tasksProcessingOrder(QueueProcessingType.LIFO)//图片下载顺序
                .defaultDisplayImageOptions(getDefaultOptions())//默认的图片加载Option
                .imageDownloader(new BaseImageDownloader(context, CONNECT_TIME_OUT, READ_TIME_OUT))//设置图片下载器
                .writeDebugLogs()//debug环境下会输出日志
                .build();
        ImageLoader.getInstance().init(configuration);
        mImageLoader = ImageLoader.getInstance();
    }

    /**
     * 配置默认的Options
     * @return
     */
    public DisplayImageOptions getDefaultOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.xadsdk_img_error)//图片地址为空的时候加载失败图片
                .showImageOnFail(R.drawable.xadsdk_img_error)//图片下载失败的时候加载失败图片
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)//使用解码类型
                .decodingOptions(new BitmapFactory.Options())//图片解码配置
                .build();
        return options;
    }

    public void displayImage(ImageView imageView, String url){
        displayImage(imageView, url, null);
    }

    public void displayImage(ImageView imageView, String url, ImageLoadingListener loadingListener){
        displayImage(imageView, url, null, loadingListener);
    }

    public void displayImage(ImageView imageView, String url, DisplayImageOptions options,
                             ImageLoadingListener loadingListener){
        if (mImageLoader != null){
            mImageLoader.displayImage(url, imageView, options, loadingListener);
        }
    }
}
