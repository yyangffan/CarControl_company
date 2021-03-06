package com.hncd.carcontrol.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.adapter.CheckAdapter;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.bean.BaseBean;
import com.hncd.carcontrol.bean.CheckAllBean;
import com.hncd.carcontrol.bean.DevlicenBean;
import com.hncd.carcontrol.bean.EventMessage;
import com.hncd.carcontrol.bean.RegistInforBean;
import com.hncd.carcontrol.dig_pop.Digshow;
import com.hncd.carcontrol.utils.CarHttp;
import com.hncd.carcontrol.utils.GlideEngine;
import com.hncd.carcontrol.utils.HttpBackListener;
import com.hncd.carcontrol.utils.ItemRecyDecoration;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.bean.CheckItemPhotoBean;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CheckResultActivity extends CarBaseActivity {

    @BindView(R.id.check_result_recy)
    RecyclerView mCheckResultRecy;
    @BindView(R.id.check_result_smart)
    SmartRefreshLayout mSmart;
    @BindView(R.id.check_result_start)
    Button mBt_start;
    @BindView(R.id.check_result_imgv)
    ImageView mImgvResult;

    private String data_result = "";
    private List<RegistInforBean.DataBean.RegInfoBean> mMapList;
    private CheckAdapter mCheckAdapter;
    private RegistInforBean mBean;
    private String check_item = "";
    private String fanx_data = "";
    private String note = "";
    private boolean data_ready = false;
    private String license_photo = "";
    private String pic_start = "data:image/jpg;base64,";

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_check_result;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        data_result = extras.getString("data");
        String result_bean = extras.getString("bean");
        mBean = new Gson().fromJson(result_bean, RegistInforBean.class);
        initView();
    }

    @OnClick({R.id.check_back, R.id.check_result_start, R.id.check_result_imgv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_back:
                finish();
                break;
            case R.id.check_result_start:
                if (TextUtils.isEmpty(note)) {
                    if (data_ready) {
                        startCheck();
                    } else {
                        ToastShow("?????????????????????,????????????");
                    }
                } else {
                    ToastShow(note);
                }
                break;
            case R.id.check_result_imgv:
                if (license_photo.equals(pic_start) || TextUtils.isEmpty(license_photo)) {
                    ToastShow("???????????????...");
                } else {
                    new Digshow(this, license_photo).show();
                }
                break;
        }
    }

    private void initView() {
        mSmart.setEnablePureScrollMode(true);//???????????????????????????
        mSmart.setEnableNestedScroll(true);//????????????????????????;
        mSmart.setEnableOverScrollDrag(true);//?????????????????????????????????????????????1.0.4
        mSmart.setEnableOverScrollBounce(true);//????????????????????????

        RegistInforBean.DataBean data = mBean.getData();

        Integer auditStatus = data.getAuditStatus();//3?????????  4????????? 5????????????
        switch (auditStatus) {
            case 0:
                mBt_start.setText("????????????");
                getData(false);
                break;
            case 2:
                mBt_start.setText("???????????????");
                getData(true);
                break;
            case 3:
                mBt_start.setText("????????????");
                getData(true);
                break;
            case 1:
                mBt_start.setText("?????????");
                note = "???????????????";
                break;
            case 4:
                mBt_start.setText("????????????");
                note = "???????????????";
                break;
            case 5:
            case 6:
                mBt_start.setText("????????????");
                note = "???????????????";
                break;
        }

        mMapList = new ArrayList<>();
        mMapList.addAll(data.getRegInfo());
        mCheckAdapter = new CheckAdapter(this, mMapList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mCheckResultRecy.setLayoutManager(linearLayoutManager);
        mCheckResultRecy.addItemDecoration(new ItemRecyDecoration(this, LinearLayoutManager.VERTICAL));
        mCheckResultRecy.setAdapter(mCheckAdapter);

        getItemInfo();

    }

    /*????????????*/
    private void startCheck() {
        Map<String, Object> map = new HashMap<>();
        map.put("serialNumber", data_result);
        map.put("userId", mUser_id);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().startCheck(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean bean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (bean.getCode() == 200 && !isFinishing()) {
                    Intent intent = new Intent(CheckResultActivity.this, CheckItemActivity.class);
                    intent.putExtra("lsh", data_result);
                    intent.putExtra("data", check_item);
                    startActivity(intent);
                    finish();
                } else {
                    ToastShow(bean.getMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });


    }

    /*????????????*/
    private void getItemInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("serialNumber", data_result);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().getDrvingLicense(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                DevlicenBean bean = new Gson().fromJson(result.toString(), DevlicenBean.class);
                if (bean.getCode() == 200 && !isFinishing()) {
                    setItemInfo(bean);
                } else {
                    ToastShow(bean.getMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });

    }

    private void setItemInfo(DevlicenBean bean) {
        license_photo = pic_start + bean.getData().getDrivingLicenseImg();
        RequestOptions options = new RequestOptions().placeholder(R.drawable.default_pic).error(R.drawable.default_pic);
        Glide.with(this).load(license_photo).apply(options).into(mImgvResult);

    }


    /*????????????????????????????????????????????????????????????*/
    private void getData(boolean more) {
        Map<String, Object> map = new HashMap<>();
        map.put("serialNumber", data_result);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().getSetItem(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean bean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (bean.getCode() == 200) {
                    check_item = result.toString();
                    if (more) {
                        getMore();
                    } else {
                        data_ready = true;
                    }
                } else {
                    ToastShow(bean.getMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    /*??????????????????   ???????????????*/
    private void getMore() {
        Map<String, Object> map = new HashMap<>();
        map.put("serialNumber", data_result);
        String result = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().getCheckInfo(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                BaseBean bean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (bean.getCode() == 200) {
                    fanx_data = result.toString();
                    ZhengheData();
                } else {
                    ToastShow(bean.getMsg());
                }
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
            }
        });
    }

    private void ZhengheData() {
        CheckAllBean checkAllBean = new Gson().fromJson(check_item, CheckAllBean.class);//?????????
        CheckAllBean check_again = new Gson().fromJson(fanx_data, CheckAllBean.class);//???????????????
        CheckAllBean.DataBean data_all = checkAllBean.getData();
        CheckAllBean.DataBean data_again = check_again.getData();

        checkAllBean.getData().setCheckApprove(check_again.getData().getCheckApprove());
        checkAllBean.getData().setOpreatType("1");//--???????????? 0????????????1?????????
        /*--------------------------------------????????????????????????????????????-start--------------------------------------*/
        List<CheckAllBean.DataBean.CheckItemBean> checkItem_all = data_all.getCheckItem();
        List<CheckAllBean.DataBean.CheckItemBean> checkItem_again = data_again.getCheckItem();
        for (CheckAllBean.DataBean.CheckItemBean bean : checkItem_again) {
            String itemCfgId = (String) bean.getItemCfgId();
            for (int i = 0; i < checkItem_all.size(); i++) {
                CheckAllBean.DataBean.CheckItemBean itemBean = checkItem_all.get(i);
                if ((itemCfgId.equals(itemBean.getItemCfgId()))) {
                    itemBean.setEdcCheckLogOutId(bean.getEdcCheckLogOutId());
                    itemBean.setLsh(bean.getLsh());
                    itemBean.setIsOkFlag(bean.getIsOkFlag());
                    itemBean.setReason(bean.getReason());
                    itemBean.setPhotoPath(bean.getPhotoPath());
                    String photoPath = bean.getPhotoPath();
                    if (!TextUtils.isEmpty(photoPath)) {
                        List<LocalMedia> mpics = new ArrayList<>();
                        String[] split = photoPath.split(",");
                        for (int j = 0; j < split.length; j++) {
                            LocalMedia localMedia = new LocalMedia();
                            localMedia.setPath(split[j]);
                            mpics.add(localMedia);
                        }
                        itemBean.setPicLists(mpics);
                    }
                    itemBean.setCreateTime(bean.getCreateTime());
                }
            }

        }
        /*--------------------------------------????????????????????????????????????-??????-end--------------------------------------*/
        List<CheckAllBean.DataBean.CheckItemBean> itemRefi_all = data_all.getCheckItemRefit();
        List<CheckAllBean.DataBean.CheckItemBean> itemRefit_again = data_again.getCheckItemRefit();
        for (CheckAllBean.DataBean.CheckItemBean bean : itemRefit_again) {
            String itemCode = (String) bean.getItemCfgRefitId();
            for (int i = 0; i < itemRefi_all.size(); i++) {
                CheckAllBean.DataBean.CheckItemBean itemBean = itemRefi_all.get(i);
                if (itemCode.equals(itemBean.getItemCfgId())) {
                    itemBean.setEdcCheckLogOutRefitId(bean.getEdcCheckLogOutRefitId());
                    itemBean.setLsh(bean.getLsh());
                    itemBean.setIsOkFlag(bean.getIsOkFlag());
                    itemBean.setReason(bean.getReason());
                    itemBean.setPhotoPath(bean.getPhotoPath());
                    String photoPath = bean.getPhotoPath();
                    if (!TextUtils.isEmpty(photoPath)) {
                        List<LocalMedia> mpics = new ArrayList<>();
                        String[] split = photoPath.split(",");
                        for (int j = 0; j < split.length; j++) {
                            LocalMedia localMedia = new LocalMedia();
                            localMedia.setPath(split[j]);
                            mpics.add(localMedia);
                        }
                        itemBean.setPicLists(mpics);
                    }
                    itemBean.setCreateTime(bean.getCreateTime());
                }
            }

        }
        /*--------------------------------------????????????????????????-???????????????--------------------------------------*/
        List<LocalMedia> mImageBeans = new ArrayList<>();

        for (int i = 0; i < data_again.getCheckItemPhoto().size(); i++) {
            CheckItemPhotoBean checkItemPhotoBean = data_again.getCheckItemPhoto().get(i);
            String itemCode = checkItemPhotoBean.getItemCfgPhotoId();
            for (CheckItemPhotoBean bean : data_all.getCheckItemPhoto()) {
                if (itemCode.equals(bean.getItemPotoCfgId())) {
                    bean.setEdcCheckLogOutPhotoId(checkItemPhotoBean.getEdcCheckLogOutPhotoId());
                    bean.setLsh(checkItemPhotoBean.getLsh());
                    bean.setHasTake(true);
                    bean.setImgPos(i);
                    bean.setPhotoPath(checkItemPhotoBean.getPhotoPath());
                    bean.setCreateTime(checkItemPhotoBean.getCreateTime());
                }
            }
            LocalMedia localMedia = new LocalMedia();
            localMedia.setPath(checkItemPhotoBean.getPhotoPath());
            localMedia.setTitle(checkItemPhotoBean.getItemCode() + ":" + checkItemPhotoBean.getItemName());
            localMedia.setCode(checkItemPhotoBean.getItemCode());
            mImageBeans.add(localMedia);
        }
        checkAllBean.getData().setImages(mImageBeans);
        check_item = new Gson().toJson(checkAllBean);
        data_ready = true;
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


}
