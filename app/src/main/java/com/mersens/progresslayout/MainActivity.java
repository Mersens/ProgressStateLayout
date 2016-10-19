package com.mersens.progresslayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private ProgressStateLayout progressStateView;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    public void init() {
        final List<String> mList=new ArrayList<>();
        for(int i=0;i<15;i++){
            mList.add("Mersens "+i);
        }
        inflater=LayoutInflater.from(this);
        mListView = (ListView) findViewById(R.id.listView);
        progressStateView = (ProgressStateLayout) findViewById(R.id.progressStateView);
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Object getItem(int position) {
                return mList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder=null;
                if(convertView==null){
                    holder=new ViewHolder();
                    convertView=inflater.inflate(R.layout.item,null);
                    holder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
                    convertView.setTag(holder);

                }else {
                    holder=(ViewHolder) convertView.getTag();
                }
                holder.tv_name.setText(mList.get(position));
                return convertView;
            }
        });
        progressStateView.showContent();
    }
    public static class ViewHolder{
        public TextView tv_name;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_loading) {
            progressStateView.showLoading();
            return true;
        } else if (id == R.id.action_empty) {
            progressStateView.showEmpty();
            return true;
        } else if (id == R.id.action_error) {
            progressStateView.showError(new ProgressStateLayout.ReloadListener() {
                @Override
                public void onClick() {
                    Toast.makeText(MainActivity.this, "重新加载", Toast.LENGTH_SHORT).show();

                }
            });
            return true;
        } else if (id == R.id.action_content) {
            progressStateView.showContent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
