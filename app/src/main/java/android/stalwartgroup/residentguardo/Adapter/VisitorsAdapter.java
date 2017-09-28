package android.stalwartgroup.residentguardo.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.stalwartgroup.residentguardo.Pojo.Visitors;
import android.stalwartgroup.residentguardo.R;
import android.stalwartgroup.residentguardo.Util.CheckInternet;
import android.stalwartgroup.residentguardo.Util.Constants;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mobileapplication on 9/28/17.
 */

public class VisitorsAdapter extends BaseAdapter {
    Context _context;
    ArrayList<Visitors> v_list;
    Holder holder;
    Holder holder1;
    Holder vHolder;
    int linpos;
    String curr_date,user_id;
    public VisitorsAdapter(Context context, ArrayList<Visitors> list_visitors) {
        this._context=context;
        this.v_list=list_visitors;
    }

    @Override
    public int getCount() {
        return v_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder{
        TextView tv_name,tv_cmngfrom;
        ImageView visitorsPic;
        Button approve,reject;
        public Holder() {
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Visitors _pos=v_list.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.allvisitors, parent, false);
            holder.tv_name=(TextView)convertView.findViewById(R.id.visitors_name);
            holder.tv_cmngfrom=(TextView)convertView.findViewById(R.id.tv_phone_comingfrm);
            holder.visitorsPic=(ImageView) convertView.findViewById(R.id.visitorsPic);
            holder.approve=(Button)convertView.findViewById(R.id.approve);
            holder.reject=(Button)convertView.findViewById(R.id.reject);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.tv_name.setTag(position);
        holder.tv_cmngfrom.setTag(position);
        holder.visitorsPic.setTag(holder);


        String name=_pos.getName();
        String mobile=_pos.getMobile();
        String comingfrom=_pos.getComing_from();
        String is_verified=_pos.getVerification();

        holder.tv_name.setText(name);
        holder.tv_cmngfrom.setText(mobile+" "+","+" "+comingfrom);
        if(!_pos.getPhoto().isEmpty()) {
            Picasso.with(_context).load(_pos.getPhoto()).into(holder.visitorsPic);
        }
        else {
            holder.visitorsPic.setImageResource(R.drawable.logo_without_name);
        }

        if(is_verified.contentEquals("Not Verified")){

            holder.approve.setVisibility(View.VISIBLE);
            holder.reject.setVisibility(View.VISIBLE);

        }

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vHolder=(Holder)v.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                builder.setMessage("Do you want to approve the visitor?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                              /*  DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mma");
                                dateFormatter.setLenient(false);
                                Date today = new Date();
                                curr_date = dateFormatter.format(today);*/
                                String verification_status="App Verified";
                                verifyVisitors(_pos.getId(),verification_status);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vHolder=(Holder)v.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                builder.setMessage("Do you want to reject the visitor?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                              /*  DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mma");
                                dateFormatter.setLenient(false);
                                Date today = new Date();
                                curr_date = dateFormatter.format(today);*/
                                String verification_status="Rejected";
                                verifyVisitors(_pos.getId(),verification_status);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        return convertView;
    }

    private void verifyVisitors(String id, String verification_status) {
        if (CheckInternet.getNetworkConnectivityStatus(_context)) {
            new verifyVisitor().execute(id,verification_status);
        }
        else{
            Toast.makeText(_context,"No internet",Toast.LENGTH_SHORT);
        }
    }

    /*
* VERIFY VISITORS*/
    public class verifyVisitor extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;
        int server_status;
        String server_response;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(_context, "Please Wait",
                    "Verifying...", true);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                String _listid = params[0];
                String _verify = params[1];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.ADDVISITORS;
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setAllowUserInteraction(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", _listid)
                        .appendQueryParameter("verification_status",_verify);

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = conn.getInputStream();
                }
                if (in == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "", data = "";

                while ((data = reader.readLine()) != null) {
                    response += data + "\n";
                }

                Log.i(TAG, "Response : " + response);

                /*
                *
                * {
    "status": 1,
    "message": "Successfully inserted"
}
                    },*/

                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    server_status=res.getInt("status");
                    if(server_status==1) {
                        server_response=res.getString("message");
                    }
                    else{
                        server_response="Error";
                    }

                }

                return null;


            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
                server_response="Error in Exit";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progress.dismiss();
            if(server_status==1){
           holder.approve.setVisibility(View.GONE);
           holder.reject.setVisibility(View.GONE);

            }
            else {
                Toast.makeText(_context,server_response,Toast.LENGTH_LONG).show();
            }

   /*         if(server_status==1) {
                mAdapter = new VisitorsAdapter(getContext(), list_visitors);
                visitorsList.setAdapter(mAdapter);
                progress.dismiss();
            }
            else{
                visitorsList.setVisibility(View.GONE);
                et_no_visitors.setVisibility(View.VISIBLE);
            }*/

        }
    }
}
