package com.hncd.carcontrol.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.adapter.ImagePhotoAdapter;
import com.hncd.carcontrol.adapter.JudgeAdapter;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.bean.CheckAllBean;
import com.hncd.carcontrol.dig_pop.BtPopupWindow;
import com.hncd.carcontrol.dig_pop.BtSelectDialog;
import com.hncd.carcontrol.utils.GlideEngine;
import com.hncd.carcontrol.utils.ItemRecyDecoration;
import com.ljy.devring.util.FileUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.app.PictureAppMaster;
import com.luck.picture.lib.bean.CheckItemPhotoBean;
import com.luck.picture.lib.camera.CustomCameraType;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.MediaExtraInfo;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.BitmapUtils;
import com.luck.picture.lib.tools.MediaUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.yalantis.ucrop.view.OverlayView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckItemActivity extends CarBaseActivity {


    @BindView(R.id.check_item_panding)
    TextView mCheckItemPanding;
    @BindView(R.id.check_item_pic)
    TextView mCheckItemPic;
    @BindView(R.id.check_item_yjpanding)
    TextView mCheckItemYjpd;
    @BindView(R.id.check_item_line)
    View mCheckItemLine;
    @BindView(R.id.check_item_smart)
    SmartRefreshLayout mSmart;
    @BindView(R.id.checki_item_judge)
    View mCheckItemJudge;
    @BindView(R.id.checki_item_photo)
    View mCheckItemPhoto;
    @BindView(R.id.check_judge_recy)
    RecyclerView mCheckItemJudgeRecy;
    @BindView(R.id.check_photo_recy)
    RecyclerView mCheckItemPhotoRecy;
    //    @BindView(R.id.check_item_fram)
//    ConstraintLayout mCheckItemPhotoFram;
//    @BindView(R.id.check_photo_imgv)
//    ImageView mCheckItemPhotoImgv;
    @BindView(R.id.check_photo_watermark)
    TextView mCheckItemPhotoWatermark;

    private BtPopupWindow mBtPopupWindow;
    private List<CheckAllBean.DataBean.CheckItemBean> mMapList;
    private JudgeAdapter mJudgeAdapter;
    private List<LocalMedia> mImageBeans;
    private ImagePhotoAdapter mImagePhotoAdapter;
    private String waterMarks = "";
    private Bitmap mBit_waterMarkers;
    private CheckAllBean mBean;
    private List<CheckItemPhotoBean> mCheckItemPhotoLists;
    private BtSelectDialog mbt_Top, mbt_bt;
    private String[][] mStrs_tp = new String[][]{{"合格", "1"}, {"不合格", "3"}, {"未判定", "0"}};
    private String[][] mStrs_bt = new String[][]{{"合格（未改装）", "1"}, {"合格（改装）", "2"}, {"不合格", "3"}, {"未判定", "0"}};

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_check_item;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);
        mSmart.setEnablePureScrollMode(true);//是否启用纯滚动模式
        mSmart.setEnableNestedScroll(true);//是否启用嵌套滚动;
        mSmart.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
        mSmart.setEnableOverScrollBounce(true);//是否启用越界回弹
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        mBean = new Gson().fromJson(data, CheckAllBean.class);
        mCheckItemPhotoLists = mBean.getData().getCheckItemPhoto();
        Log.e(TAG, "init: " + new Gson().toJson(mBean));
        initViews();
        getData();
    }

    @OnClick({R.id.check_back, R.id.check_item_panding, R.id.check_item_pic, R.id.check_item_yjpanding, R.id.check_item_pz, R.id.check_item_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_back:
                finish();
                break;
            case R.id.check_item_yjpanding:
                mBtPopupWindow.showAsDropDown(mCheckItemYjpd, 0, -8);
                break;
            case R.id.check_item_panding:
                toGoAnima(mCheckItemPanding);
                mCheckItemPic.setTextColor(getResources().getColor(R.color.black));
                mCheckItemJudge.setVisibility(View.VISIBLE);
                mCheckItemPhoto.setVisibility(View.GONE);
                break;
            case R.id.check_item_pic:
                toGoAnima(mCheckItemPic);
                mCheckItemPanding.setTextColor(getResources().getColor(R.color.black));
                mCheckItemJudge.setVisibility(View.GONE);
                mCheckItemPhoto.setVisibility(View.VISIBLE);
                break;
            case R.id.check_item_pz:
                selectPhoto();
                break;
            case R.id.check_item_next:
                toGonext();
//                statActivity(CheckEndActivity.class);
                break;
        }
    }

    /*不合格的需要填写原因*/
    private void toGonext() {
        boolean canComit = true, isHege = true;
        for (int i = 0; i < mMapList.size(); i++) {
            CheckAllBean.DataBean.CheckItemBean checkItemBean = mMapList.get(i);
            if (checkItemBean.getType() == 0) {
                int typeone = checkItemBean.getTypeone();
                if (typeone == 3) {//不合格
                    isHege = false;
                    if (TextUtils.isEmpty(checkItemBean.getReason())) {
                        canComit = false;
                        break;
                    }
                } else if (typeone == 0) {
                    isHege = false;
                }
            } else {
                int typetwo = checkItemBean.getTypetwo();
                if (typetwo == 3) {//不合格
                    isHege = false;
                    if (TextUtils.isEmpty(checkItemBean.getReason())) {
                        canComit = false;
                        break;
                    }
                } else if (typetwo == 0) {
                    isHege = false;
                }
            }

        }
        if (canComit) {
            mBean.getData().setImageBeans(mImageBeans);
            Intent intent = new Intent(this, CheckEndActivity.class);
            intent.putExtra("data", new Gson().toJson(mBean));
            intent.putExtra("state", isHege);
            startActivity(intent);
        } else {
            ToastShow("请填写不合格项目原因");
        }
    }


    private CheckAllBean.DataBean.CheckItemBean mTp_bean, mBt_bean;//判定项目的实例
    private int clickPos;//判定项目点击的位置

    private void initViews() {
        initPop();
        initBtDig();
        mMapList = new ArrayList<>();
        mMapList.addAll(mBean.getData().getCheckItem());
        for (int i = 0; i < mBean.getData().getCheckItemRefit().size(); i++) {
            CheckAllBean.DataBean.CheckItemBean checkItemBean = mBean.getData().getCheckItemRefit().get(i);
            checkItemBean.setType(1);
            mMapList.add(checkItemBean);
        }
        mImageBeans = new ArrayList<>();
        mJudgeAdapter = new JudgeAdapter(this, mMapList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mCheckItemJudgeRecy.setLayoutManager(linearLayoutManager);
        mCheckItemJudgeRecy.addItemDecoration(new ItemRecyDecoration(this, LinearLayoutManager.VERTICAL));
        mCheckItemJudgeRecy.setAdapter(mJudgeAdapter);
        mJudgeAdapter.setOnItemClickListener(new JudgeAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                CheckAllBean.DataBean.CheckItemBean map = mMapList.get(pos);
                clickPos = pos;
                int type = map.getType();
                if (type == 0) {//TODO 这里还得改tmd应该跟改装那个一样下方会有一个显示合格、不合格的区分
                    mTp_bean = map;
                    mbt_Top.setData(map.getCheckItemName(), map.getTypeone());
                    mbt_Top.show();
                } else if (type == 1) {
                    mBt_bean = map;
                    mbt_bt.setData(map.getCheckItemName(), map.getTypetwo());
                    mbt_bt.show();
                }
            }

            @Override
            public void onPicAddListener(int pos) {
                itemSelectPhoto(pos);
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mCheckItemPhotoRecy.setLayoutManager(gridLayoutManager);
        mImagePhotoAdapter = new ImagePhotoAdapter(this, mImageBeans);
        mCheckItemPhotoRecy.setAdapter(mImagePhotoAdapter);
        mImagePhotoAdapter.setOnItemClickListener(new ImagePhotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                shwoLook(mImageBeans, pos);
            }
        });
        setImgeData();
    }

    private void getData() {
        waterMarks = "发动机号：123124\n" +
                "发动机型号：12型\n" +
                "车辆识别代码：JLGEIEEONEGEONG\n" +
                "轮胎规格：         10*100";
        mCheckItemPhotoWatermark.setText(waterMarks);
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                mBit_waterMarkers = getBit();
            }
        }.start();


    }

    private void setImgeData() {
        //示例图片展示
      /*  for (int i = 0; i < 6; i++) {
            LocalMedia imageBean = new LocalMedia();
            imageBean.setPath(*//*"https://t7.baidu.com/it/u=963301259,1982396977&fm=193&f=GIF"*//*"");
            imageBean.setTitle("前照灯" + i);
            mImageBeans.add(imageBean);
        }
        mImagePhotoAdapter.notifyDataSetChanged();*/
    }

    private void itemSelectPhoto(int pos) {
        CheckAllBean.DataBean.CheckItemBean itemBean = mMapList.get(pos);
        List<LocalMedia> picLists = itemBean.getPicLists();
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .isUseCustomCamera(true)
                .setSingleBack(true)
                .setButtonFeatures(CustomCameraType.BUTTON_STATE_ONLY_CAPTURE)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        // 结果回调
                        Log.e(TAG, "onResult: " + result.get(0).getPath());
                        picLists.add(result.get(0));
                        mJudgeAdapter.notifyItemChanged(pos);
                    }

                    @Override
                    public void onCancel() {
                        // 取消
                        Log.e(TAG, "onCancel: 拍照取消");
                    }

                    @Override
                    public void onCusResult(List<LocalMedia> result, int position, int nowImgpos) {
                        // 自定义结果回调
                    }
                });


    }


    private void selectPhoto() {
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .isUseCustomCamera(true)
                .setWaterMark(waterMarks)
                .setLocalMedias(mImageBeans)
                .setPhotoLists(mCheckItemPhotoLists)
                .setSingleBack(false)
                .setButtonFeatures(CustomCameraType.BUTTON_STATE_ONLY_CAPTURE)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        // 结果回调
                        Log.e(TAG, "onResult: " + result.get(0).getPath());
                    }

                    @Override
                    public void onCancel() {
                        // 取消
                        Log.e(TAG, "onCancel: 拍照取消");
                    }

                    @Override
                    public void onCusResult(List<LocalMedia> images, int pos, int nowImgpos) {
                        // 自定义结果回调
                        CheckItemPhotoBean bean = mCheckItemPhotoLists.get(pos);
                        if (nowImgpos == mImageBeans.size()) {//新增
                            LocalMedia localMedia = images.get(0);
                            localMedia.setTitle(bean.getCheckItemCode() + ":" + bean.getCheckItemName());
                            mImageBeans.add(localMedia);
                            mImagePhotoAdapter.notifyItemInserted(nowImgpos);
                        } else {//修改
                            LocalMedia localMedia = mImageBeans.get(nowImgpos);
                            localMedia.setPath(images.get(0).getPath());
                            mImagePhotoAdapter.notifyItemChanged(nowImgpos);
                        }
                        bean.setHasTake(true);
                        bean.setImgPos(nowImgpos);
//                        convertBitmap(BitmapFactory.decodeFile(pic_path), pic_path);
//                        throwGlideGetBit(BitmapFactory.decodeFile(pic_path),pic_path);
                    }
                });

    }

    private void throwGlideGetBit(Bitmap srcBitmap, String path) {
        togeBit(srcBitmap, FileUtil.getFile(path));
    }

    /**
     * Bitmap进行镜像（前置摄像头图片会镜像--后置不会）有问题
     */
    private void convertBitmap(Bitmap srcBitmap, String path) {
        int width = srcBitmap.getWidth();
        int height = srcBitmap.getHeight();
        Canvas canvas = new Canvas();
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1);
        Bitmap newBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, width, height, matrix, true);
        canvas.drawBitmap(newBitmap, new Rect(0, 0, width, height), new Rect(0, 0, width, height), null);
        togeBit(newBitmap, FileUtil.getFile(path));
    }

    /*
     * 图片整合
     * */
    private void togeBit(Bitmap bitmap, File file) {
        if (!bitmap.isMutable()) {
            //设置图片为背景为透明
            bitmap = bitmap.copy(Bitmap.Config.ARGB_4444, true);
        }
        Paint paint = new Paint();
        paint.setARGB(255, 210, 177, 240);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(mBit_waterMarkers, 0, 0, paint);//叠加新图b2
        canvas.save();
        canvas.restore();
        BitmapUtils.saveBitmapFile(bitmap, file);//保存图片到本地
    }

    /*获取水印图片的Bitmap*/
    private Bitmap getBit() {
        //打开图片的缓存
        mCheckItemPhotoWatermark.setDrawingCacheEnabled(true);
        //图片的大小 固定的语句
        mCheckItemPhotoWatermark.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //将位置传给view
        mCheckItemPhotoWatermark.layout(0, 0, mCheckItemPhotoWatermark.getMeasuredWidth(), mCheckItemPhotoWatermark.getMeasuredHeight());
        //转化为bitmap文件
        Bitmap bitmap = mCheckItemPhotoWatermark.getDrawingCache();
        return bitmap;
    }

    private void initPop() {
        mBtPopupWindow = new BtPopupWindow(this);
        mBtPopupWindow.setOnItemClickListener(new BtPopupWindow.OnItemClickListener() {
            @Override
            public void onOkClickListener() {
                for (int i = 0; i < mMapList.size(); i++) {
                    CheckAllBean.DataBean.CheckItemBean map = mMapList.get(i);
                    int type = map.getType();
                    if (type == 0) {
                        map.setTypeone(1);
                    }
                }
                mJudgeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNogazClickListener() {
                for (int i = 0; i < mMapList.size(); i++) {
                    CheckAllBean.DataBean.CheckItemBean map = mMapList.get(i);
                    int type = map.getType();
                    if (type == 1) {
                        map.setTypetwo(1);
                    }
                }
                mJudgeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRecoverClickListener() {
                for (int i = 0; i < mMapList.size(); i++) {
                    CheckAllBean.DataBean.CheckItemBean map = mMapList.get(i);
                    int type = map.getType();
                    if (type == 0) {
                        map.setTypeone(0);
                    } else if (type == 1) {
                        map.setTypetwo(0);
                    }
                }
                mJudgeAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initBtDig() {
        mbt_Top = new BtSelectDialog(this, mStrs_tp);
        mbt_Top.setOnDigBackListener(new BtSelectDialog.OnDigBackListener() {
            @Override
            public void onDigBackListener(int type) {
                mTp_bean.setTypeone(type);
                mJudgeAdapter.notifyItemChanged(clickPos);
            }
        });


        mbt_bt = new BtSelectDialog(this, mStrs_bt);
        mbt_bt.setOnDigBackListener(new BtSelectDialog.OnDigBackListener() {
            @Override
            public void onDigBackListener(int type) {
                mBt_bean.setTypetwo(type);
                mJudgeAdapter.notifyItemChanged(clickPos);
            }
        });


    }

    private void shwoLook(List<LocalMedia> selectList, int position) {
        if (selectList.size() > 0) {
            LocalMedia media = selectList.get(position);
            String mimeType = media.getMimeType();
            PictureSelector.create(this)
                    .themeStyle(R.style.picture_default_style) // xml设置主题
                    //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                    .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                    //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义播放回调控制，用户可以使用自己的视频播放界面
                    .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                    .openExternalPreview(position, selectList);
        }
    }


    private int line_startDis = 0;

    private void toGoAnima(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.appColor));
        int left_line = mCheckItemLine.getLeft() + mCheckItemLine.getWidth() / 2;
        int left_tvview = textView.getLeft() + textView.getWidth() / 2;
        int go_distance = left_tvview - left_line;
        ObjectAnimator anim = ObjectAnimator.ofFloat(mCheckItemLine, "translationX", line_startDis, go_distance);
        anim.setDuration(165);
        anim.start();
        line_startDis = go_distance;
    }

}
