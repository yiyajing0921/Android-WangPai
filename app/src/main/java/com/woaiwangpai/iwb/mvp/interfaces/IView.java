/**
 * Copyright 2017 JessYan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.woaiwangpai.iwb.mvp.interfaces;

/**
 * Created by Gabriel on 2018/10/19.
 * Email 17600284843@163.com
 */
public interface IView {

    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 关闭Activity
     */
    void finishActivity();

    /**
     * 接口请求完成
     */
    void onComplete();

    /**
     * 接口请求出错
     */
    void onException();

    /**
     * 列表刷新错误更改page
     */
    void onPageMinus();

}
