package com.example.point;

import java.io.IOException;
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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Activitymain extends ActionBarActivity implements TabListener{

	ProgressDialog pDialog;
	AlertDialog.Builder alert;
	HttpClient httpclient;
	HttpGet request;
	HttpResponse response;
	ViewPager viewpager;
	ActionBar actionbar;
	String reply;
	Usersession session;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitymain);
		session = new Usersession(getApplicationContext());
		if(!session.checklogin())
		{
			Intent login = new Intent(Activitymain.this,Login.class);
			startActivity(login);
		}
		
		
		
		viewpager=(ViewPager) findViewById(R.id.pager);
		viewpager.setAdapter(new MainpageAdapter(getSupportFragmentManager()));
		viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				actionbar.setSelectedNavigationItem(arg0);
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		actionbar=getActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		ActionBar.Tab ongoingtab = actionbar.newTab();
		ongoingtab.setIcon(R.drawable.ongoingselecttab);
		ongoingtab.setTabListener(this);
		
		ActionBar.Tab pasttab = actionbar.newTab();
		pasttab.setIcon(R.drawable.pastselecttab);
		pasttab.setTabListener(this);
		
		ActionBar.Tab eventstab = actionbar.newTab();
		eventstab.setIcon(R.drawable.eventtabselect);
		eventstab.setTabListener(this);
		
		actionbar.addTab(ongoingtab);
		actionbar.addTab(pasttab);
		actionbar.addTab(eventstab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activitymain, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id==R.id.profile)
		{
			Intent profile = new Intent(Activitymain.this,Profile.class);
			startActivity(profile);
		}
		if(id==R.id.logout)
		{
			new logout().execute();
		}
		if(id==R.id.refresh)
		{
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewpager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	public void restart()
	{
		session.remove("first_name");
		session.remove("last_name");
		session.remove("zipcode");
		session.remove("email");
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}
	class logout extends AsyncTask<String,String,String>
	{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Activitymain.this);
			pDialog.setMessage("Loging Out...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> list=new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("email",session.getinfo("email")));
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://www.point.web44.net/logout.php");
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
		protected void onPostExecute(String file_url)
		{
			pDialog.dismiss();
			restart();
		}
		
	}
}

class MainpageAdapter extends FragmentPagerAdapter
{

	public MainpageAdapter(FragmentManager fm) {
		super(fm);
		
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment fragment = null;
		if(arg0==0)
		{
			fragment = new Ongoingfragment();
		}
		if(arg0==1)
		{
			fragment = new Pastfragment();
		}
		if(arg0==2)
		{
			fragment = new Eventfragment();
		}
		return fragment;
	}

	@Override
	public int getCount() {
		
		return 3;
	}
	
}
