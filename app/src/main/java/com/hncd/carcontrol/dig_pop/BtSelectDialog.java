package com.hncd.carcontrol.dig_pop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hncd.carcontrol.R;
import com.hncd.carcontrol.adapter.DigbtAdapter;
import com.hncd.carcontrol.utils.ItemRecyDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BtSelectDialog extends Dialog {

    @BindView(R.id.dig_bt_title)
    TextView mDigBtTitle;
    @BindView(R.id.dig_bt_recy)
    RecyclerView mDigBtRecy;
    private Context mContext;
    private int type;
    private String title;
    private String[][] mStrings;
    private List<Map<String, Object>> mLists;
    private DigbtAdapter mDigbtAdapter;
    private OnDigBackListener mOnDigBackListener;


    public BtSelectDialog(@NonNull Context context, String[][] strings) {
        super(context);
        mContext = context;
        mStrings = strings;
    }

    public void setOnDigBackListener(OnDigBackListener onDigBackListener) {
        mOnDigBackListener = onDigBackListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dig_bt_select);
        ButterKnife.bind(this);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.app_color_transparent);
        mLists = new ArrayList<>();
        for (int i = 0; i < mStrings.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", mStrings[i][0]);
            map.put("type", Integer.parseInt(mStrings[i][1]));
            mLists.add(map);
        }
        initViews();
    }

    private void initViews() {
        mDigBtTitle.setText(title);
        mDigbtAdapter = new DigbtAdapter(mContext, mLists);
        mDigbtAdapter.setType(type);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mDigBtRecy.setLayoutManager(layoutManager);
        mDigBtRecy.addItemDecoration(new ItemRecyDecoration(mContext, LinearLayoutManager.VERTICAL));
        mDigBtRecy.setAdapter(mDigbtAdapter);

        mDigbtAdapter.setOnItemClickListener(new DigbtAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                int type = (int) mLists.get(pos).get("type");
                if (mOnDigBackListener != null) {
                    mOnDigBackListener.onDigBackListener(type);
                }
                dismiss();
            }
        });


    }


    /*设置弹窗标题以及列表默认选中*/
    public void setData(String title, int type) {
        this.title = title;
        this.type = type;
        if (mDigBtTitle != null && mDigbtAdapter != null) {
            mDigBtTitle.setText(title);
            mDigbtAdapter.setType(type);
        }
    }

    @Override
    public void show() {
        super.show();
        if(mDigbtAdapter!=null)
        mDigbtAdapter.notifyDataSetChanged();
    }

    public interface OnDigBackListener {
        void onDigBackListener(int type);
    }

}
