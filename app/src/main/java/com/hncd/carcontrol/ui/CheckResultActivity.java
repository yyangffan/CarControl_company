package com.hncd.carcontrol.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.adapter.CheckAdapter;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.bean.BaseBean;
import com.hncd.carcontrol.bean.CheckAllBean;
import com.hncd.carcontrol.bean.EventMessage;
import com.hncd.carcontrol.bean.RegistInforBean;
import com.hncd.carcontrol.utils.CarHttp;
import com.hncd.carcontrol.utils.HttpBackListener;
import com.hncd.carcontrol.utils.ItemRecyDecoration;
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

    private String data_result = "";
    private List<RegistInforBean.DataBean.RegInfoBean> mMapList;
    private CheckAdapter mCheckAdapter;
    private RegistInforBean mBean;
    private String check_item = "";
    private String fanx_data = "";
    private String note = "";
    private boolean data_ready = false;

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

    @OnClick({R.id.check_back, R.id.check_result_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_back:
                finish();
                break;
            case R.id.check_result_start:
                if (TextUtils.isEmpty(note)) {
                    if (data_ready) {
                        Intent intent = new Intent(this, CheckItemActivity.class);
                        intent.putExtra("lsh", data_result);
                        intent.putExtra("data", check_item);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastShow("数据正在准备中,稍后重试");
                    }
                } else {
                    ToastShow(note);
                }
                break;
        }
    }

    private void initView() {
        mSmart.setEnablePureScrollMode(true);//是否启用纯滚动模式
        mSmart.setEnableNestedScroll(true);//是否启用嵌套滚动;
        mSmart.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
        mSmart.setEnableOverScrollBounce(true);//是否启用越界回弹

        RegistInforBean.DataBean data = mBean.getData();
        Integer auditStatus = data.getAuditStatus();//3可查验  4审核中 5查验完成
        switch (auditStatus) {
            case 0:
                mBt_start.setText("开始查验");
                getData(false);
                break;
            case 2:
                mBt_start.setText("查验不合格");
                getData(true);
                break;
            case 3:
                mBt_start.setText("审核失败");
                getData(true);
                break;
            case 1:
                mBt_start.setText("审核中");
                note = "正在审核中";
                break;
            case 4:
                mBt_start.setText("审核通过");
                note = "审核已通过";
                break;
        }

        mMapList = new ArrayList<>();
        mMapList.addAll(data.getRegInfo());
        mCheckAdapter = new CheckAdapter(this, mMapList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mCheckResultRecy.setLayoutManager(linearLayoutManager);
        mCheckResultRecy.addItemDecoration(new ItemRecyDecoration(this, LinearLayoutManager.VERTICAL));
        mCheckResultRecy.setAdapter(mCheckAdapter);


    }

    /*获取配置项目（查验项目、拍照项目、通道）*/
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

    /*反显数据获取   并整合数据*/
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
        CheckAllBean checkAllBean = new Gson().fromJson(check_item, CheckAllBean.class);//总数据
        CheckAllBean check_again = new Gson().fromJson(fanx_data, CheckAllBean.class);//已上传数据
        CheckAllBean.DataBean data_all = checkAllBean.getData();
        CheckAllBean.DataBean data_again = check_again.getData();

        checkAllBean.getData().setCheckApprove(check_again.getData().getCheckApprove());
        checkAllBean.getData().setOpreatType("1");//--操作类型 0：新增，1：更新
        /*--------------------------------------判定项目上方数据整合修改-start--------------------------------------*/
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
        /*--------------------------------------判定项目下方数据整合修改-改装-end--------------------------------------*/
        List<CheckAllBean.DataBean.CheckItemBean> itemRefi_all = data_all.getCheckItemRefit();
        List<CheckAllBean.DataBean.CheckItemBean> itemRefit_again = data_again.getCheckItemRefit();
        for (CheckAllBean.DataBean.CheckItemBean bean : itemRefit_again) {
            String itemCode =(String) bean.getItemCfgRefitId();
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
        /*--------------------------------------检测图片项目集合-及图片集合--------------------------------------*/
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


}
