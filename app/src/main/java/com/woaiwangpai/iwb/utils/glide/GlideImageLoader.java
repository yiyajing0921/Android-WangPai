package com.woaiwangpai.iwb.utils.glide;

//import cn.finalteam.galleryfinal.ImageLoader;
//import cn.finalteam.galleryfinal.widget.GFImageView;

/***
 *
 * sd卡文件路径
 * new File(Environment.getExternalStorageDirectory() + "/GalleryFinal/edittemp/")
 * new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "GalleryFinal/")
 */

//图片加载器   TODO 拦截判断缓存
public class GlideImageLoader{ //implements ImageLoader {
    /**
     *
     * @param activity
     * @param path
     * @param imageView
     * @param defaultDrawable
     * @param width
     * @param height
     *     *  asBitmap() 需要设置在 load(url)之前
     *      *  缓存 ：DiskCacheStrategy.NONE      不缓存
     *      *  缓存 ：DiskCacheStrategy.RESOURCE  缓存解码后的图
     *      *  缓存 ：DiskCacheStrategy.DATA      缓存未解码后的数据
     *      *  缓存 ：DiskCacheStrategy.AUTOMATIC 默认选项
     *      *  缓存 ：DiskCacheStrategy.ALL       对于远程图缓存RESOURCE+DATA，对于本地图缓存RESOURCE
     */
//    @Override
//    public void displayImage(Activity activity, String path, final GFImageView imageView, Drawable defaultDrawable, int width, int height) {
//        Glide.with(activity)
//                .load("file://" + path)
//                .placeholder(defaultDrawable)
//                .error(defaultDrawable)
//                .override(width, height)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .skipMemoryCache(false)//跳过内存缓存
////                .centerInside()//
//                .into(new ImageViewTarget<Drawable>(imageView) {
//            @Override
//            protected void setResource(Drawable resource) {
//                imageView.setImageDrawable(resource);
//            }
//
//        });
//
//        //Glide加载图片
////        Glide.with(activity).load(path).into(imageView);
//
//        //Picasso 加载图片
//        //Picasso.with(context).load(path).into(imageView);
//
//    }
//    //这里是清除缓存的方法,根据需要自己实现
//    @Override
//    public void clearMemoryCache() {
//
//        try {
//            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
//                Glide.get(MyApplication.getInstance()).clearMemory();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            if (Looper.myLooper() == Looper.getMainLooper()) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Glide.get(MyApplication.getInstance()).clearDiskCache();
//                    }
//                }).start();
//            } else {
//                Glide.get(MyApplication.getInstance()).clearDiskCache();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
