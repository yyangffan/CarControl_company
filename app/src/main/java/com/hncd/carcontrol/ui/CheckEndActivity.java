package com.hncd.carcontrol.ui;

import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hncd.carcontrol.R;
import com.hncd.carcontrol.base.CarBaseActivity;
import com.hncd.carcontrol.bean.BaseBean;
import com.hncd.carcontrol.bean.CheckAllBean;
import com.hncd.carcontrol.dig_pop.TongdPopWindow;
import com.hncd.carcontrol.utils.CarHttp;
import com.hncd.carcontrol.utils.HttpBackListener;
import com.ljy.devring.DevRing;
import com.ljy.devring.logger.RingLog;
import com.ljy.devring.util.FileUtil;
import com.luck.picture.lib.bean.CheckItemPhotoBean;
import com.superc.yyfflibrary.utils.titlebar.TitleUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CheckEndActivity extends CarBaseActivity {


    @BindView(R.id.check_end_img)
    ImageView mCheckEndImg;
    @BindView(R.id.check_end_tvresult)
    TextView mCheckEndTvresult;
    @BindView(R.id.check_end_name)
    TextView mCheckEndName;
    @BindView(R.id.check_end_haopai)
    TextView mCheckEndHaopai;
    @BindView(R.id.check_end_ywtype)
    TextView mCheckEndYwtype;
    @BindView(R.id.check_end_cytype)
    TextView mCheckEndCytype;
    @BindView(R.id.check_end_td)
    TextView mCheckEndTd;
    @BindView(R.id.check_end_remarks)
    EditText mCheckEndRemarks;
    @BindView(R.id.check_end_date)
    TextView mCheckEndDate;
    @BindView(R.id.check_end_rbno)
    RadioButton mCheckEndRbno;
    @BindView(R.id.check_end_rbyes)
    RadioButton mCheckEndRbyes;
    @BindView(R.id.check_ll)
    LinearLayout mCheckEndLL;

    private String mnowDate;
    private TongdPopWindow mTongdPopWindow;
    private List<CheckAllBean.DataBean.CheckLineBean> mTongd;
    private CheckAllBean mBean;          //上一界面所填数据以及通道数据的填充
    private boolean mState = true;       //查验是否合格
    private CheckAllBean.DataBean mUpBean;
    private CheckAllBean.DataBean.CheckApprove mCheckApprove;
    private String td_id = "";
    private Map<String, Object> map_up;
    private Map<String, Object> map_aprove;


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_check_end;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        TitleUtils.setStatusTextColor(false, this);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mnowDate = simpleDateFormat.format(new Date());
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        String updata = intent.getStringExtra("updata");
        mState = intent.getBooleanExtra("state", true);
        mBean = new Gson().fromJson(data, CheckAllBean.class);
        mUpBean = new Gson().fromJson(updata, CheckAllBean.DataBean.class);
        mCheckApprove = mUpBean.getCheckApprove();
        map_aprove = new HashMap<>();
        Log.e(TAG, "init: " + updata);
        initView();
        getData();
    }


    @OnClick({R.id.check_end_back, R.id.check_end_submit, R.id.check_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_end_back:
                finish();
                break;
            case R.id.check_end_submit:
                toComit();
                break;
            case R.id.check_ll:
                initTdPop();
                break;
        }
    }

    private void initView() {
        mCheckEndName.requestFocus();
        mTongd = new ArrayList<>();
        mTongd.addAll(mBean.getData().getCheckLine());
        mCheckEndTd.setText(mTongd.get(0).getLineNo());
        td_id = mTongd.get(0).getId();
    }

    private void getData() {
        mCheckEndImg.setImageResource(mState ? R.drawable.check_success : R.drawable.check_failed);
        mCheckEndTvresult.setText(mState ? "查验合格" : "查验不合格");
        mCheckEndName.setText(mLoginBean.getData().getName());
  /*      mCheckEndCytype.setText("机动车（校车）");
        mCheckEndYwtype.setText("补领登记证书");
        mCheckEndHaopai.setText("大型汽车/你想象不到");*/
        mCheckEndDate.setText(mnowDate);
        if ("1".equals(mUpBean.getOpreatType())) {//如果是更新
            CheckAllBean.DataBean.CheckApprove checkApprove = mUpBean.getCheckApprove();
            mCheckEndRemarks.setText(checkApprove.getCheckRemark());
            mCheckEndRbno.setChecked(checkApprove.getNewEnergyFlag().equals("1") ? true : false);
            mCheckEndRbyes.setChecked(checkApprove.getNewEnergyFlag().equals("1") ? false : true);
            td_id = checkApprove.getNvrLineId();
            for (CheckAllBean.DataBean.CheckLineBean bean : mTongd) {
                if (td_id.equals(bean.getId())) {
                    mCheckEndTd.setText(bean.getLineNo());
                    break;
                }
            }

        }
        map_up = zhegnHdata();
    }

    private void toComit() {
        map_aprove.put("checkRemark", mCheckEndRemarks.getText().toString());
        map_aprove.put("checkStatus", mState ? "0" : "1");//查验状态 0：合格 1：不合格
        map_aprove.put("nvrLineId", td_id);//通道id
        map_aprove.put("newEnergyFlag", mCheckEndRbno.isChecked() ? "1" : "0");//是否新能源 0:是  1：否
        map_up.put("checkApprove", map_aprove);
        showLoad();
        String result_data = new Gson().toJson(map_up);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), result_data);
        CarHttp.getInstance().toGetData(CarHttp.getInstance().getApiService().saveInfo(requestBody), new HttpBackListener() {
            @Override
            public void onSuccessListener(Object result) {
                super.onSuccessListener(result);
                hideLoad();
                BaseBean bean = new Gson().fromJson(result.toString(), BaseBean.class);
                if (bean.getCode() == 200) {
                    startActivity(new Intent(CheckEndActivity.this, MainActivity.class));
                    DevRing.activityListManager().killAllExclude(MainActivity.class);
                }else{
                    toSaveLog(result_data,bean.getMsg());
                }
                ToastShow(bean.getMsg());
            }

            @Override
            public void onErrorLIstener(String error) {
                super.onErrorLIstener(error);
                hideLoad();
                toSaveLog(result_data,error);
            }
        });
    }

    private void toSaveLog(String result, String msg) {
        String out_str = msg + ": " + result;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            String time = simpleDateFormat.format(new Date());
            String fileName = time + ".txt";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dirTemp = FileUtil.getDirectory(FileUtil.getExternalCacheDir(this), "carcontrol_log");
                File fileOutput = FileUtil.getFile(dirTemp, fileName);

                if (fileOutput == null) {
                    Log.e(TAG, "toSaveLog: 文件创建失败");
                    return;
                }
                FileOutputStream fos = new FileOutputStream(fileOutput);
                fos.write(out_str.getBytes());
                fos.close();
                Log.e(TAG, "toSaveLog: 文件已保存");
            }
        } catch (Exception e) {
            Log.e(TAG, "toSaveLog: 文件创建失败" + e.toString());
        }

    }

    private void initTdPop() {
        mTongdPopWindow = new TongdPopWindow(this, mTongd, mCheckEndTd.getText().toString(), mCheckEndLL.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        mTongdPopWindow.setOnAdapterClickListener(new TongdPopWindow.OnAdapterClickListener() {
            @Override
            public void onAdapterListener(String name, String id) {
                mCheckEndTd.setText(name);
                td_id = id;
            }
        });
        mTongdPopWindow.showAsDropDown(mCheckEndLL, 0, 10);
    }

    /*数据整合-不是我想这样的（设计问题）我也不想这样*/
    private Map<String, Object> zhegnHdata() {
        CheckAllBean.DataBean.CheckApprove checkApprove = mUpBean.getCheckApprove();
        String checkDate = checkApprove.getCheckDate();
        String lsh = mCheckApprove.getLsh();
        map_aprove.put("checkLogOutApproveId", checkApprove.getCheckLogOutApproveId());
        map_aprove.put("lsh", lsh);
        map_aprove.put("deptCode", checkApprove.getDeptCode());
        map_aprove.put("checkNum", checkApprove.getCheckNum());
        map_aprove.put("checkDate", checkApprove.getCheckDate());
        map_aprove.put("checkPeople", checkApprove.getCheckPeople());
        map_aprove.put("checkStartDate", checkApprove.getCheckStartDate());


        Map<String, Object> map_result = new HashMap<>();
        map_result.put("opreatType", mUpBean.getOpreatType());

        List<CheckAllBean.DataBean.CheckItemBean> checkItem = mUpBean.getCheckItem();//判定项目
        List<Map<String, Object>> list_item = new ArrayList<>();
        for (CheckAllBean.DataBean.CheckItemBean bean : checkItem) {
            Map<String, Object> map_item = new HashMap<>();
            map_item.put("edcCheckLogOutId", bean.getEdcCheckLogOutId());
            map_item.put("lsh", lsh);
            map_item.put("itemCode", bean.getCheckItemCode());
            map_item.put("itemName", bean.getCheckItemName());
            map_item.put("isOkFlag", bean.getIsOkFlag());//{"合格", "1"}, {"不合格", "3"}, {"未判定", "0"}
            map_item.put("reason", bean.getIsOkFlag() == 3 ? bean.getReason() : "");
            map_item.put("photoPath", bean.getIsOkFlag() == 3 ? bean.getPhotoPath() : "");
            map_item.put("itemCfgId", bean.getItemCfgId());
            map_item.put("createTime", checkDate);
            list_item.add(map_item);
        }
        map_result.put("checkItem", list_item);

        List<CheckAllBean.DataBean.CheckItemBean> checkItemRefit = mUpBean.getCheckItemRefit();//改装项目
        List<Map<String, Object>> list_refit = new ArrayList<>();
        for (CheckAllBean.DataBean.CheckItemBean bean : checkItemRefit) {
            Map<String, Object> map_refit = new HashMap<>();
            map_refit.put("edcCheckLogOutRefitId", bean.getEdcCheckLogOutId());
            map_refit.put("lsh", lsh);
            map_refit.put("itemCode", bean.getCheckItemCode());
            map_refit.put("itemName", bean.getCheckItemName());
            map_refit.put("isOkFlag", bean.getIsOkFlag());//{"合格（未改装）", "1"}, {"合格（改装）", "2"}, {"不合格", "3"}, {"未判定", "0"}
            map_refit.put("reason", bean.getIsOkFlag() == 3 ? bean.getReason() : "");
            map_refit.put("photoPath", bean.getIsOkFlag() == 3 ? bean.getPhotoPath() : "");
            map_refit.put("itemCfgRefitId", bean.getItemCfgId());
            map_refit.put("createTime", checkDate);
            list_refit.add(map_refit);
        }
        map_result.put("checkItemRefit", list_refit);

        List<CheckItemPhotoBean> checkItemPhoto = mUpBean.getCheckItemPhoto();//拍照项目
        List<Map<String, Object>> list_photo = new ArrayList<>();
        for (CheckItemPhotoBean bean : checkItemPhoto) {
            Map<String, Object> map_photo = new HashMap<>();
            map_photo.put("edcCheckLogOutPhotoId", bean.getEdcCheckLogOutPhotoId());
            map_photo.put("lsh", lsh);
            map_photo.put("itemCode", bean.getCheckItemCode());
            map_photo.put("itemName", bean.getCheckItemName());
            map_photo.put("photoPath", bean.getPhotoPath());
            map_photo.put("itemCfgPhotoId", bean.getItemPotoCfgId());
            map_photo.put("createTime", bean.getCreateTime());
            list_photo.add(map_photo);
        }
        map_result.put("checkItemPhoto", list_photo);

        return map_result;
    }


}
