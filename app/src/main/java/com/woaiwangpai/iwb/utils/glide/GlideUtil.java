package com.woaiwangpai.iwb.utils.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.woaiwangpai.iwb.R;
import com.woaiwangpai.iwb.utils.FilterUtils;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 8:58
 * @Email : yiyajing8023@163.com
 * @Description : Glide封装实体类
 */
public class GlideUtil {
    private static int LoadingImg = R.mipmap.icon_load;//brvah_sample_footer_loading_progress;
    private static int ErrorImg = R.mipmap.icon_load_fail;

    /**
     * 设置普通图片
     *
     * @param context
     * @param Url
     * @param iv      glide 4.9.0 可以直接配置圆形和圆角图片 transforms(new CircleCrop())
     *                渐变设置和监听设置有更改 Drawabl
     *                asBitmap() 需要设置在 load(url)之前
     *                缓存 ：DiskCacheStrategy.NONE      不缓存
     *                缓存 ：DiskCacheStrategy.RESOURCE  缓存解码后的图
     *                缓存 ：DiskCacheStrategy.DATA      缓存未解码后的数据
     *                缓存 ：DiskCacheStrategy.AUTOMATIC 默认选项
     *                缓存 ：DiskCacheStrategy.ALL       对于远程图缓存RESOURCE+DATA，对于本地图缓存RESOURCE
     */

    public static void ShowImage(Context context, String Url, ImageView iv) {
        Glide.with(context)
                .asBitmap() // 不显示gif图
                .load(Url)
                .centerCrop()//裁剪
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .placeholder(LoadingImg)
                .error(ErrorImg)
                .fallback(R.mipmap.icon_load)//加载为空显示
                .into(iv);
    }

    /**
     * 设置圆角图片
     *
     * @param context
     * @param url
     * @param iv
     * @param rudius
     */
    public static void ShowRoundCornerImg(Context context, String url, ImageView iv, int rudius) {
        Glide.with(context)
                .load(url)
                .placeholder(LoadingImg)//占位符 加载中显示  or (new ColorDeawable(Color.BLACK))
                .error(ErrorImg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .transform(
                        new GlideRoundTransform(rudius)
                        // new RoundedCorners(rudius)  //Glide 4.9
                ).into(iv);
    }

    //绘制上边俩角圆角
    public static void ToRoundCornerTopImg(Context context, String url, ImageView iv, int rudius) {
        CornerTransform transformation = new CornerTransform(context, FilterUtils.dip2px(context, rudius));
        //只是绘制左上角和右上角圆角
        transformation.setExceptCorner(false, false, true, true);
//      Glide4.0+上的使用
        RequestOptions options = new RequestOptions()
                .error(ErrorImg)
//                .placeholder(LoadingImg)
                .transform(transformation);
//       .transforms(transformation);

        Glide.with(context).asBitmap().load(url).apply(options).into(iv);


//        Glide.with(context)
//                .load(url)
//                .placeholder(LoadingImg)//占位符 加载中显示  or (new ColorDeawable(Color.BLACK))
//                .error(ErrorImg)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
//                .transform(
//                new GlideRoundTransform(rudius)
//              // new RoundedCorners(rudius)  //Glide 4.9
//        ).into(iv);
    }

    /**
     * 设置圆形图片
     * //不带边框的圆形图片
     * Glide.with(getApplicationContext()).asBitmap().placeholder(R.mipmap.ic_userdefault).load(ownerUrl).apply(RequestOptions.circleCropTransform()).into(mIvUserIcon);
     */
    public static void ShowCircleImg(Context context, String url, ImageView iv) {
        Glide.with(context)
                .load(url)
                .placeholder(LoadingImg)
                .error(ErrorImg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .transform(
                        new GlideCircleTransform()
//                        new CircleCrop()   //Glide 4.9
                )
                .into(iv);
    }

    //圆形 自定义impic  .error(imgPic)
    public static void ShowCircleImg(Context context, String url, ImageView iv, int imgPic) {
        Glide.with(context)
                .load(url)
                .placeholder(imgPic)
                .error(imgPic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .transform(
                        new GlideCircleTransform()
//        new CircleCrop()   //Glide 4.9
                )
                .into(iv);
    }

    public static void setLoadingImg(int loadingImgId) {
        LoadingImg = loadingImgId;
    }

    //模糊
    public static void ShowBlurImg(Context context, String url, ImageView iv, int imgPic) {
        Glide.with(context)
                .load(url)
                .placeholder(imgPic)
                .error(imgPic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .transform(new BlurTransformation(15))
                .into(iv);
    }

    //多重变换
    public static void ShowMultiImg(Context context, String url, ImageView iv, int imgPic) {
        Glide.with(context)
                .load(url)
                .placeholder(imgPic)
                .error(imgPic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .transform(new MultiTransformation(new RoundedCornersTransformation(15, 5), new BlurTransformation(15)))
                .into(iv);
    }
    //加载网络图截图用
    public static void SreenImg(Context context, String url, ImageView iv) {
        Glide.with(context)
                .asBitmap().load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        iv.setImageBitmap(resource);
                    }
                });
    }

    // 设置背景
    public static void loadImageInbackGround(Context context, String url, final View view) {

        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                view.setBackground(resource);
            }
        });
    }

    //固定高宽
    public static void ShowFinalImg(Context context, String url, ImageView iv, int imgPic) {
        Glide.with(context)
                .load(url)
                .placeholder(imgPic)
                .error(imgPic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .override(100, 100)
                .into(iv);
    }

    //监听器
    public static void ShowListenerImg(Context context, String url, ImageView iv, int imgPic) {
        Glide.with(context)
                .load(url)
                .placeholder(imgPic)
                .error(imgPic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .listener(new RequestListener<Drawable>() {
                    //加载失败  false 未消费 继续 into(imgview)   // true 已消费  不再走into()
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    //加载成功  false 未消费 继续 into(imgview)   //true 已消费  不再走into()
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(iv);
    }

    /***
     * 完整设置示例
     */
    public static void ShowAll(Context context, String imageUrl, ImageView imageView, @NonNull TransitionOptions<?, ?> transitionOptions) {

        Glide.with(/*imageView.getContext()*/context)
                .asBitmap() // 不显示gif图
                .load(imageUrl)
//                .transition(DrawableTransitionOptions.withCrossFade(500)) // 渐变
                .placeholder(LoadingImg)// 加载中图片
                .error(ErrorImg) // 加载失败图片
                .transforms(new CircleCrop()) // 圆形图片
                .transforms(new RoundedCorners(20)) // 圆角图片
                // 高斯模糊，参数1：模糊度；参数2：图片缩放x倍后再进行模糊
                .apply(bitmapTransform(new BlurTransformation(50, 8)))
//                .listener(new RequestListener<Drawable>() { // 加载监听
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        return false;
//                    }
//                })
                .into(imageView);
    }


    /**
     *
     //带白色边框的圆形图片
     Glide.with(getApplicationContext()).asBitmap().placeholder(R.mipmap.ic_userdefault)
     .load(ownerUrl)
     .apply(RequestOptions.bitmapTransform(new CircleCrop()))
     .transform(new GlideCircleWithBorder(getApplicationContext(), 2, Color.parseColor("#ffffff")))
     .into(mIvUserIcon);
     */
    /**
     * @param context
     * @param imageView
     * @param imageUrl
     * @param borderColor #2422552
     */
    public static void showCircleWithBorder(Context context, ImageView imageView, String imageUrl, String borderColor) {
        Glide.with(context).asBitmap()
                .load(imageUrl)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .transform(new GlideCircleWithBorder(context, 3, Color.parseColor(borderColor)))
                .into(imageView);
    }

    public static void showCircleWithBorder(Context context, ImageView imageView, @RawRes @DrawableRes @Nullable Integer imageId, String borderColor) {
        Glide.with(context).asBitmap()
                .load(imageId)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .transform(new GlideCircleWithBorder(context, 3, Color.parseColor(borderColor) /*R.color.wriht*//*Color.parseColor("#ffffff")*/))
                .into(imageView);
    }

    public static void showCircleWithBorder(Context context, ImageView imageView, String imageUrl, @ColorRes int borderColor) {
        Glide.with(context).asBitmap()
                .load(imageUrl)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .transform(new GlideCircleWithBorder(context, 3, context.getResources().getColor(borderColor) /*R.color.wriht*//*Color.parseColor("#ffffff")*/))
                .into(imageView);
    }

    public static void showCircleWithBorder(Context context, ImageView imageView, @RawRes @DrawableRes @Nullable Integer imageId, @ColorRes int borderColor) {
        Glide.with(context).asBitmap()
                .load(imageId)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .transform(new GlideCircleWithBorder(context, 3, ContextCompat.getColor(context, borderColor) /*R.color.wriht*//*Color.parseColor("#ffffff")*/))
                .into(imageView);
    }

}
