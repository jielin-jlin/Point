package com.point.meet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.point.meet.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class Infofragment extends Fragment {

	static TextView firstname,lastname;
	QuickContactBadge icon;
	Usersession session;
	ProgressDialog pDialog;
	AlertDialog.Builder alert;
	HttpClient httpclient;
	HttpGet request;
	HttpResponse response;
	InputStream inputStream;
	String reply;
	public Infofragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		RelativeLayout revlay = (RelativeLayout)inflater.inflate(R.layout.fragment_infofragment, container,
				false);
		session = new Usersession(getActivity().getApplicationContext());
		firstname=(TextView)revlay.findViewById(R.id.textView1);
		lastname=(TextView)revlay.findViewById(R.id.textView2);
		icon=(QuickContactBadge)revlay.findViewById(R.id.quickContactBadge1);
		firstname.setText(session.getinfo("first_name"));
		lastname.setText(session.getinfo("last_name"));
		new getimage().execute();

		
		return revlay;
	}
	class getimage extends AsyncTask<String,String,String>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
			
				List<NameValuePair> list=new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("email",session.getinfo("email").toString().trim()));
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost("http://www.point.web44.net/getimage.php");
				try {
					post.setEntity(new UrlEncodedFormEntity(list));
					
				} catch (UnsupportedEncodingException e) {
					
					e.printStackTrace();
				}
				try {
					HttpResponse response = client.execute(post);
					HttpEntity entity = response.getEntity();
					reply = EntityUtils.toString(entity).trim();
					
				} catch (ClientProtocolException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			turnimage();
			pDialog.dismiss();
		}
	}
	public void turnimage()
	{
		byte[] bytearray = null;
		String image = null;
		image = reply.toString().trim();
		if(!image.isEmpty())
		{
			bytearray=Base64.decode(image, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length);
			icon.setImageBitmap(bitmap);
		}
	}

}
