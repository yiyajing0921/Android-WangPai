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

import android.app.Activity;

/**
 * Created by Gabriel on 2018/10/19.
 * Email 17600284843@163.com
 */
public interface IPresenter<T extends IView> {

    /**
     * 做一些初始化操作
     */
    void onStart();

    /**
     * 在框架中 {@link Activity# onDestroy()} 时会默认调用 {@link IPresenter#onDestroy()}
     */
    void onDestroy();

    void attachView(T view);

    T getView();

}
