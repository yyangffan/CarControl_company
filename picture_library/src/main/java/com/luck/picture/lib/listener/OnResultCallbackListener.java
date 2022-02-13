package com.luck.picture.lib.listener;

import java.util.List;

/**
 * @author：luck
 * @date：2020-01-14 17:08
 * @describe：onResult Callback Listener
 */
public interface OnResultCallbackListener<T> {
    /**
     * return LocalMedia result
     *
     * @param result
     */
    void onResult(List<T> result);

    /**
     * Cancel
     */
    void onCancel();

    /*自定义拍照的返回*/
    void onCusResult(List<T> result, int pos,int nowImgpos);
}
