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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.adapter.ImagePhotoAdapter;
import com.hncd.carcontrol.adapter.JudgeAdapter;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.bean.BaseBean;
import com.hncd.carcontrol.bean.CheckAllBean;
import com.hncd.carcontrol.bean.ImageUpBean;
import com.hncd.carcontrol.dig_pop.BtPopupWindow;
import com.hncd.carcontrol.dig_pop.BtSelectDialog;
import com.hncd.carcontrol.utils.CarHttp;
import com.hncd.carcontrol.utils.GlideEngine;
import com.hncd.carcontrol.utils.HttpBackListener;
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
import com.luck.picture.lib.listener.OnActiBackListener;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.BitmapUtils;
import com.luck.picture.lib.tools.MediaUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;
import com.yalantis.ucrop.view.OverlayView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
    private List<CheckAllBean.DataBean.CheckItemBean> mMapList;         //??????????????????---?????????????????????????????????
    private JudgeAdapter mJudgeAdapter;
    private List<LocalMedia> mImageBeans;                               //??????????????????
    private ImagePhotoAdapter mImagePhotoAdapter;
    private String waterMarks = "";
    private Bitmap mBit_waterMarkers;
    private CheckAllBean mBean;
    private List<CheckItemPhotoBean> mCheckItemPhotoLists;              //????????????????????????
    private BtSelectDialog mbt_Top, mbt_bt;
    private String[][] mStrs_tp = new String[][]{{"??????", "1"}, {"?????????", "3"}, {"?????????", "0"}};
    private String[][] mStrs_bt = new String[][]{{"?????????????????????", "1"}, {"??????????????????", "2"}, {"?????????", "3"}, {"?????????", "0"}};
    private CheckAllBean.DataBean.CheckApprove mCheckApprove;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_check_item;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);
        mSmart.setEnablePureScrollMode(true);//???????????????????????????
        mSmart.setEnableNestedScroll(true);//????????????????????????;
        mSmart.setEnableOverScrollDrag(true);//?????????????????????????????????????????????1.0.4
        mSmart.setEnableOverScrollBounce(true);//????????????????????????
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        String lsh = intent.getStringExtra("lsh");
        mBean = new Gson().fromJson(data, CheckAllBean.class);
        mCheckApprove = mBean.getData().getCheckApprove();
        mCheckApprove.setCheckDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        mCheckApprove.setCheckStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        mCheckApprove.setLsh(lsh);
        mCheckApprove.setCheckPeople(mLoginBean.getData().getUserIdX());
        mCheckApprove.setDeptCode(mLoginBean.getData().getDeptId());
        mBean.getData().setCheckApprove(mCheckApprove);
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

    /*??????????????????????????????*/
    private void toGonext() {
        CheckAllBean.DataBean mBean_data = new CheckAllBean.DataBean();
        boolean canComit = true, isHege = true;
        for (int i = 0; i < mMapList.size(); i++) {
            CheckAllBean.DataBean.CheckItemBean checkItemBean = mMapList.get(i);
            if (checkItemBean.getType() == 0) {//?????????
                int typeone = checkItemBean.getIsOkFlag();
                if (typeone == 3) {//?????????
                    isHege = false;
                    if (TextUtils.isEmpty(checkItemBean.getReason())) {
                        canComit = false;
                        break;
                    }
                } else if (typeone == 0) {
                    ToastShow("????????????????????????");
                    return;
                }
            } else {//????????????
                int typetwo = checkItemBean.getIsOkFlag();
                if (typetwo == 3) {//?????????
                    isHege = false;
                    if (TextUtils.isEmpty(checkItemBean.getReason())) {
                        canComit = false;
                        break;
                    }
                }
            }

        }
        if (mImageBeans.size() == 0) {
            ToastShow("????????????????????????");
            return;
        }

        List<CheckItemPhotoBean> photoLists = new ArrayList<>();//???????????????????????????
        for (int i = 0; i < mBean.getData().getCheckItemPhoto().size(); i++) {
            CheckItemPhotoBean checkItemPhotoBean = mBean.getData().getCheckItemPhoto().get(i);
            if (!TextUtils.isEmpty(checkItemPhotoBean.getPhotoPath())) {
                photoLists.add(checkItemPhotoBean);
            }
        }
        mBean_data.setCheckItem(mBean.getData().getCheckItem());
        mBean_data.setCheckItemRefit(mBean.getData().getCheckItemRefit());
        mBean_data.setCheckApprove(mBean.getData().getCheckApprove());
        mBean_data.setCheckItemPhoto(photoLists);
        mBean_data.setOpreatType(mBean.getData().getOpreatType());//--???????????? 0????????????1?????????

        if (canComit) {
            Intent intent = new Intent(this, CheckEndActivity.class);
            intent.putExtra("data", new Gson().toJson(mBean));//????????????
            intent.putExtra("updata", new Gson().toJson(mBean_data));//????????????
            intent.putExtra("state", isHege);
            startActivity(intent);
        } else {
            ToastShow("??????????????????????????????");
        }
    }


    private CheckAllBean.DataBean.CheckItemBean mTp_bean, mBt_bean;//?????????????????????
    private int clickPos;//???????????????????????????

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
//        mImageBeans = new ArrayList<>();
        mImageBeans = mBean.getData().getImages();
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
                if (type == 0) {//TODO ???????????????tmd??????????????????????????????????????????????????????????????????????????????
                    mTp_bean = map;
                    mbt_Top.setData(map.getCheckItemName(), map.getIsOkFlag());
                    mbt_Top.show();
                } else if (type == 1) {
                    mBt_bean = map;
                    mbt_bt.setData(map.getCheckItemName(), map.getIsOkFlag());
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
        waterMarks = "???????????????123124\n" +
                "??????????????????12???\n" +
                "?????????????????????JLGEIEEONEGEONG\n" +
                "???????????????         10*100";
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
        //??????????????????
      /*  for (int i = 0; i < 6; i++) {
            LocalMedia imageBean = new LocalMedia();
            imageBean.setPath(*//*"https://t7.baidu.com/it/u=963301259,1982396977&fm=193&f=GIF"*//*"");
            imageBean.setTitle("?????????" + i);
            mImageBeans.add(imageBean);
        }
        mImagePhotoAdapter.notifyDataSetChanged();*/
    }

    /*???????????????????????????*/
    private void itemSelectPhoto(int pos) {
        CheckAllBean.DataBean.CheckItemBean itemBean = mMapList.get(pos);
        List<LocalMedia> picLists = itemBean.getPicLists();
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .isUseCustomCamera(true)
                .setSingleBack(true)
                .isCompress(true)//????????????
                .compressQuality(60)//??????????????????????????? 0~ 100
                .setButtonFeatures(CustomCameraType.BUTTON_STATE_ONLY_CAPTURE)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        // ????????????
                        Log.e(TAG, "onResult: " + result.get(0).getPath());
//                        picLists.add(result.get(0));
//                        mJudgeAdapter.notifyItemChanged(pos);
                        upLoadImage(0, result.get(0), pos, 0, null);
                    }

                    @Override
                    public void onCancel() {
                        // ??????
                        Log.e(TAG, "onCancel: ????????????");
                    }

                    @Override
                    public void onCusResult(List<LocalMedia> result, int position, int nowImgpos, OnActiBackListener onActiBackListener) {
                        // ?????????????????????
                        Log.e(TAG, "onResult: " + result.get(0).getPath());
                    }
                });


    }

    /*?????????????????????*/
    private void selectPhoto() {
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .isUseCustomCamera(true)
                .setWaterMark(waterMarks)
                .setLocalMedias(mImageBeans)
                .setPhotoLists(mCheckItemPhotoLists)
                .setSingleBack(false)
                .isCompress(true)//????????????
                .compressQuality(60)//??????????????????????????? 0~ 100
                .setButtonFeatures(CustomCameraType.BUTTON_STATE_ONLY_CAPTURE)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        Log.e(TAG, "onResult: " + result.get(0).getPath());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "onCancel: ????????????");
                    }

                    @Override
                    public void onCusResult(List<LocalMedia> images, int pos, int nowImgpos, OnActiBackListener onActiBackListener) {
                        // ?????????????????????
              /*          CheckItemPhotoBean bean = mCheckItemPhotoLists.get(pos);
                        if (nowImgpos == mImageBeans.size()) {//??????
                            LocalMedia localMedia = images.get(0);
                            localMedia.setTitle(bean.getCheckItemCode() + ":" + bean.getCheckItemName());
                            localMedia.setCode(bean.getCheckItemCode());
                            mImageBeans.add(localMedia);
                            mImagePhotoAdapter.notifyItemInserted(nowImgpos);
                        } else {//??????
                            LocalMedia localMedia = mImageBeans.get(nowImgpos);
                            localMedia.setPath(images.get(0).getPath());
                            mImagePhotoAdapter.notifyItemChanged(nowImgpos);
                        }
                        bean.setHasTake(true);
                        bean.setImgPos(nowImgpos);
                        onActiBackListener.onActiBackListener("???????????????" + images.get(0).getPath());*/
                        upLoadImage(1, images.get(0), pos, nowImgpos, onActiBackListener);
//                        convertBitmap(BitmapFactory.decodeFile(pic_path), pic_path);
//                        throwGlideGetBit(BitmapFactory.decodeFile(pic_path),pic_path);
                    }
                });

    }

    private void upLoadImage(int type, LocalMedia localMedia, int pos, int nowImgpos, OnActiBackListener onActiBackListener) {
        String path = localMedia.getCompressPath();
        if (TextUtils.isEmpty(path)) {
            path = localMedia.getPath();
        }
        File img = new File(path);
        String names = img.getName();
        RequestBody requestFile = RequestBody.create(MediaType.parse(guessMimeType(img.getPath())), img);
        MultipartBody.Part body = null;
        try {
            body = MultipartBody.Part.createFormData("upfile", URLEncoder.encode(names, "UTF-8"), requestFile);
        } catch (UnsupportedEncodingException e) {
            Log.e("ManOneFragment", "toAddClient: ???????????????" + names + e.toString());
        }
//        showLoad();
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().upLoadFile(body), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
//                hideLoad();
                ImageUpBean bean = new Gson().fromJson(result.toString(), ImageUpBean.class);
                if (bean.getCode() == 200) {
                    String result_url = bean.getData().getPath();
                    if (type == 0) {//??????????????????????????????--???????????????????????????????????????????????????????????????????????????
                        CheckAllBean.DataBean.CheckItemBean itemBean = mMapList.get(pos);
                        String photoPath = itemBean.getPhotoPath();
                        photoPath = TextUtils.isEmpty(photoPath) ? result_url : photoPath + "," + result_url;
                        itemBean.setPhotoPath(photoPath);
                        mMapList.get(pos).getPicLists().add(localMedia);
                        mJudgeAdapter.notifyItemChanged(pos);
                    } else {//?????????????????????
//                        mImageBeans.get(pos).setImgUrl(result_url);
                        CheckItemPhotoBean bean_photo = mCheckItemPhotoLists.get(pos);
                        bean_photo.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
                        bean_photo.setPhotoPath(result_url);

                        if (nowImgpos == mImageBeans.size()) {//??????
                            localMedia.setTitle(bean_photo.getCheckItemCode() + ":" + bean_photo.getCheckItemName());
                            localMedia.setCode(bean_photo.getCheckItemCode());
                            mImageBeans.add(localMedia);
                            mImagePhotoAdapter.notifyItemInserted(nowImgpos);
                        } else {//??????
                            LocalMedia localMedia_ready = mImageBeans.get(nowImgpos);
                            localMedia_ready.setPath(localMedia.getPath());
                            mImagePhotoAdapter.notifyItemChanged(nowImgpos);
                        }
                        bean_photo.setHasTake(true);
                        bean_photo.setImgPos(nowImgpos);
                        onActiBackListener.onActiBackListener(true,"???????????????" + localMedia.getPath());

                    }

                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                if (type == 1) {
                    onActiBackListener.onActiBackListener(false, "???????????????" + localMedia.getPath());
                }
//                hideLoad();
            }
        });


    }


    private void throwGlideGetBit(Bitmap srcBitmap, String path) {
        togeBit(srcBitmap, FileUtil.getFile(path));
    }

    /**
     * Bitmap?????????????????????????????????????????????--????????????????????????
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
     * ????????????
     * */
    private void togeBit(Bitmap bitmap, File file) {
        if (!bitmap.isMutable()) {
            //??????????????????????????????
            bitmap = bitmap.copy(Bitmap.Config.ARGB_4444, true);
        }
        Paint paint = new Paint();
        paint.setARGB(255, 210, 177, 240);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(mBit_waterMarkers, 0, 0, paint);//????????????b2
        canvas.save();
        canvas.restore();
        BitmapUtils.saveBitmapFile(bitmap, file);//?????????????????????
    }

    /*?????????????????????Bitmap*/
    private Bitmap getBit() {
        //?????????????????????
        mCheckItemPhotoWatermark.setDrawingCacheEnabled(true);
        //??????????????? ???????????????
        mCheckItemPhotoWatermark.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //???????????????view
        mCheckItemPhotoWatermark.layout(0, 0, mCheckItemPhotoWatermark.getMeasuredWidth(), mCheckItemPhotoWatermark.getMeasuredHeight());
        //?????????bitmap??????
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
                        map.setIsOkFlag(1);
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
                        map.setIsOkFlag(1);
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
                        map.setIsOkFlag(0);
                    } else if (type == 1) {
                        map.setIsOkFlag(0);
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
                mTp_bean.setIsOkFlag(type);
                mJudgeAdapter.notifyItemChanged(clickPos);
            }
        });


        mbt_bt = new BtSelectDialog(this, mStrs_bt);
        mbt_bt.setOnDigBackListener(new BtSelectDialog.OnDigBackListener() {
            @Override
            public void onDigBackListener(int type) {
                mBt_bean.setIsOkFlag(type);
                mJudgeAdapter.notifyItemChanged(clickPos);
            }
        });


    }

    private void shwoLook(List<LocalMedia> selectList, int position) {
        if (selectList.size() > 0) {
            LocalMedia media = selectList.get(position);
            String mimeType = media.getMimeType();
            PictureSelector.create(this)
                    .themeStyle(R.style.picture_default_style) // xml????????????
                    //.setPictureWindowAnimationStyle(animationStyle)// ???????????????????????????
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// ????????????Activity????????????????????????????????????
                    .isNotPreviewDownload(true)// ????????????????????????????????????
                    //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// ???????????????????????????????????????????????????????????????????????????
                    .imageEngine(GlideEngine.createGlideEngine())// ??????????????????????????????????????????
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

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

}
