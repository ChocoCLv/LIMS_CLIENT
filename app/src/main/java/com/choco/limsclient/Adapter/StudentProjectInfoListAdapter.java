package com.choco.limsclient.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.choco.limsclient.Activities.Student.ProjectInfoActivity;
import com.choco.limsclient.Util.ProjectInfo;

import java.util.List;

/**
 * Created by choco on 2017/4/23.
 */

public class StudentProjectInfoListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private int layoutID;
    private int itemIDs[];
    private String[] tags;
    private List<ProjectInfo> projectInfoList;

    public StudentProjectInfoListAdapter(Context context, List<ProjectInfo> projectInfoList, String[] tags, int layoutID, int itemIDs[]) {
        this.mInflater = LayoutInflater.from(context);
        this.projectInfoList = projectInfoList;
        this.layoutID = layoutID;
        this.itemIDs = itemIDs;
        this.tags = tags;
        this.context = context;
    }

    @Override
    public int getCount() {
        return projectInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(layoutID, null);
        ProjectInfo pi = projectInfoList.get(position);
        for (int i = 0; i < tags.length; i++) {
            TextView tv = (TextView) convertView.findViewById(itemIDs[i]);
            String text = pi.get(tags[i]);
            tv.setText(text);
        }
        setOnClickListener(convertView,position);
        return convertView;
    }

    public void setOnClickListener(View convertView,final int position){
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProjectInfoActivity.class);
                intent.putExtra("PROJECT_INFO",projectInfoList.get(position).toString());
                context.startActivity(intent);
            }
        });
    }
}
