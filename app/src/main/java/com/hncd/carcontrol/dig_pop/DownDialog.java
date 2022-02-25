package com.hncd.carcontrol.dig_pop;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hncd.carcontrol.R;
import com.hncd.carcontrol.utils.ProgressUtil;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.DownloadListener2;
import com.ljy.devring.util.FileUtil;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/********************************************************************
 @version: 1.0.0
 @description: 更新的弹窗Dialog
 @author: admin
 @time: 2022/2/25 11:19
 @变更历史:
 ********************************************************************/
public class DownDialog extends Dialog {
    @BindView(R.id.dig_down_content)
    TextView mDigDownContent;
    @BindView(R.id.dig_down_cancel)
    TextView mDigDownCancel;
    @BindView(R.id.dig_down_update)
    TextView mDigDownUpdate;
    @BindView(R.id.dig_down_progressbar)
    ProgressBar progressBar;


    private OnBtClickListener mOnBtClickListener;
    private Context mContext;
    private String down_url;
    private boolean is_start = false;

    public DownDialog(@NonNull Context context, String down_url) {
        super(context);
        mContext = context;
        this.down_url = down_url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download);
        ButterKnife.bind(this);
        getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.app_color_transparent);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        mDigDownContent.setText("有新版本了，赶快更新吧！");

    }

    public DownDialog setOnBtClickListener(OnBtClickListener onBtClickListener) {
        mOnBtClickListener = onBtClickListener;
        return this;
    }

    @OnClick({R.id.dig_down_cancel, R.id.dig_down_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dig_down_cancel:
                if (mOnBtClickListener != null) {
                    dismiss();
                    mOnBtClickListener.onCancelListener();
                }
                break;
            case R.id.dig_down_update:
                mDigDownContent.setText("下载中...请耐心等待");
                mDigDownCancel.setVisibility(View.INVISIBLE);
                mDigDownUpdate.setVisibility(View.INVISIBLE);
                goUpdate();
                break;
        }
    }


    private AtomicLong progress = new AtomicLong();

    private void goUpdate() {
        progressBar.setVisibility(View.VISIBLE);
        File file = FileUtil.getFile(FileUtil.getCacheDir(mContext) + "carcont");
        DownloadTask build = new DownloadTask.Builder(down_url, file).setMinIntervalMillisCallbackProcess(300).build();
        build.enqueue(new DownloadListener2() {
            @Override
            public void taskStart(@NonNull DownloadTask task) {
                is_start = true;
            }

            @Override
            public void downloadFromBeginning(@NonNull DownloadTask task, @NonNull BreakpointInfo info, @NonNull ResumeFailedCause cause) {
                super.downloadFromBeginning(task, info, cause);
                progress.set(0);
                ProgressUtil.calcProgressToViewAndMark(progressBar, 0, info.getTotalLength());
            }

            @Override
            public void downloadFromBreakpoint(@NonNull DownloadTask task, @NonNull BreakpointInfo info) {
                super.downloadFromBreakpoint(task, info);
                progress.set(info.getTotalOffset());
                ProgressUtil.calcProgressToViewAndMark(progressBar, progress.get(),
                        info.getTotalLength());
            }

            @Override
            public void fetchProgress(@NonNull DownloadTask task, int blockIndex, long increaseBytes) {
                super.fetchProgress(task, blockIndex, increaseBytes);
                final long offset = progress.addAndGet(increaseBytes);
                ProgressUtil.updateProgressToViewWithMark(progressBar, offset);
            }

            @Override
            public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause) {
                Log.i("DownDialog", "taskEnd: 线层结束");
                if (mOnBtClickListener != null) {
                    new CountDownTimer(2000,1000){
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            mOnBtClickListener.onDownFinListener(file);
                            dismiss();
                        }
                    }.start();

                }
            }

            @Override
            public void fetchEnd(@NonNull DownloadTask task, int blockIndex, long contentLength) {
                super.fetchEnd(task, blockIndex, contentLength);
                Log.i("DownDialog", "fetchEnd: 下载结束");

            }
        });
    }

    public interface OnBtClickListener {
        void onCancelListener();

        void onUpdateListener();

        void onDownFinListener(File file);
    }

    @Override
    public void onBackPressed() {
        if (mOnBtClickListener != null && !is_start) {
            dismiss();
            mOnBtClickListener.onCancelListener();
        }
    }
}
