package android.stalwartgroup.residentguardo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.stalwartgroup.residentguardo.Activity.FlatListActivity;
import android.stalwartgroup.residentguardo.Activity.RegisterActivity;
import android.stalwartgroup.residentguardo.Pojo.Flatlist;
import android.stalwartgroup.residentguardo.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mobileapplication on 9/19/17.
 */

public class FlatAdapter extends BaseAdapter{

    Holder holder;
    Context context;
    ArrayList<Flatlist> array;

    public FlatAdapter(FlatListActivity flatListActivity, ArrayList<Flatlist> flatlist) {
        this.context=flatListActivity;
        this.array=flatlist;
    }


    @Override
    public int getCount()
    {
        return array.size();
    }


    @Override
    public Object getItem(int position) {
        return getItem(position);
    }
    public class Holder{
        TextView tv_name;

    }
    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        final Flatlist unity_pos=array.get(i);

        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.flat,parent, false);
            holder.tv_name=(TextView)convertView.findViewById(R.id.flat_name);


            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_name.setTag(i);

        String name=unity_pos.getFlat_name();
        holder.tv_name.setText(name);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    RegisterActivity.flat_name.setText(unity_pos.getFlat_name());
                    RegisterActivity.flat_id = unity_pos.getPlot_no_id();
                ((Activity)context).finish();// finish the adapter

               /* Intent intent=new Intent(view.getContext(), CheckinActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);*/

            }
        });
        return convertView;
    }
}
