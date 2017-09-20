package android.stalwartgroup.residentguardo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.stalwartgroup.residentguardo.Activity.AppartmentListActivity;
import android.stalwartgroup.residentguardo.Activity.RegisterActivity;
import android.stalwartgroup.residentguardo.Pojo.Apartmentlist;
import android.stalwartgroup.residentguardo.R;
import android.stalwartgroup.residentguardo.Util.Constants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mobileapplication on 9/19/17.
 */

public class ApartmentAdapter extends BaseAdapter{
    Holder holder;
    Context context;
    ArrayList<Apartmentlist> array;
    String page;

    public ApartmentAdapter(AppartmentListActivity appartmentListActivity, ArrayList<Apartmentlist> apartmentlist) {
        this.context=appartmentListActivity;
        this.array=apartmentlist;
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
        TextView tv_name,tv_cmngfrom,tv_entrytime,tv_exittime;
        ImageView iv_exit;

    }
    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        final Apartmentlist unity_pos=array.get(i);

        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.apartment,parent, false);
            holder.tv_name=(TextView)convertView.findViewById(R.id.apartment_name);


            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_name.setTag(i);

        String name=unity_pos.getApartment_name();
        holder.tv_name.setText(name);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    RegisterActivity.apart_name.setText(unity_pos.getApartment_name());
                    RegisterActivity.apartment_id = unity_pos.getApartment_id();
                   String apartment_id1=RegisterActivity.apartment_id;
                SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.APARTMENT_ID, apartment_id1);
                editor.commit();
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
