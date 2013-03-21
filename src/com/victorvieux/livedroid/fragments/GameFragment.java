package com.victorvieux.livedroid.fragments;

import java.io.IOException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.devspark.appmsg.AppMsg;
import com.google.gson.Gson;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.activities.OnRefreshListener;
import com.victorvieux.livedroid.adapters.AchAdapter;
import com.victorvieux.livedroid.api.RestClient;
import com.victorvieux.livedroid.api.data.Achievement.ACH_TYPE;
import com.victorvieux.livedroid.api.endpoints.Achievements;
import com.victorvieux.livedroid.api.endpoints.Catalog;
import com.victorvieux.livedroid.tools.CachedAsyncHttpResponseHandler;
import com.victorvieux.livedroid.tools.Misc;

public  class GameFragment extends Fragment implements OnClickListener, OnItemSelectedListener {
	private AQuery aq = null;
	AchAdapter mAdapter;
	
	public static GameFragment newInstance(int index, String url, String title, String box_small, String box_large, String catalog) {
		GameFragment f = new GameFragment();

		Bundle args = new Bundle();
		args.putInt("index", index);
		args.putString("url", url);
		args.putString("title", title);
		args.putString("box_small", box_small);
		args.putString("box_large", box_large);
		args.putString("catalog", catalog);
		f.setArguments(args);

		return f;
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg2) {
		case 2:
			mAdapter.filter(ACH_TYPE.MISSING);
			break;
		case 1:
			mAdapter.filter(ACH_TYPE.WON);
			break;
		default:
			mAdapter.filter(ACH_TYPE.ALL);
			break;
		}
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	public String getShownUrl() {
		return getArguments().getString("url");
	}
	public int getShownIndex() {
		return getArguments().getInt("index");
	}
	public String getShownTitle() {
		return getArguments().getString("title");
	}
	public String getShownSmallBox() {
		return getArguments().getString("box_small");
	}
	public String getShownLargeBox() {
		return getArguments().getString("box_large");
	}
	public String getShownCatalogUrl() {
		return getArguments().getString("catalog");
	}
	
	public String getx360aUrl() 
	{
		return "http://www.xbox360achievements.org/game/"+ getShownTitle().replace(' ', '-').replace(":", "").toLowerCase() + "/guide/";
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		aq = new AQuery(getActivity());
		if (getView().findViewById(R.id.MainImageViewBox) != null) {
			aq.id(R.id.MainImageViewBox).image(getShownSmallBox());
			getView().findViewById(R.id.MainImageViewBox).setOnClickListener(this);
		}

		onRefresh(false);

		RestClient.get(getActivity(), new CachedAsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				if (GameFragment.this.getView() != null) {
					if (response.length() > 1000)	
						GameFragment.this.getView().findViewById(R.id.ButtonGuide).setVisibility(View.VISIBLE);

					GameFragment.this.getView().findViewById(R.id.ButtonGuide).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(getx360aUrl()));
							GameFragment.this.startActivity(i);
						}
					});
				}
			}
		}, getx360aUrl());
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null)
			return null;

		View root = inflater.inflate(R.layout.fragment_game, container, false);
		
		GridView gal = (GridView) root.findViewById(R.id.gridView);
		gal.setEmptyView(root.findViewById(R.id.loading_ref));
		return root;
	}




	private ACH_TYPE getFilter() {
		switch (((Spinner)getView().findViewById(R.id.spinnerType)).getSelectedItemPosition()) {
		case 1:
			return ACH_TYPE.WON;
		case 2:
			return ACH_TYPE.MISSING;
		default:
			return ACH_TYPE.ALL;
		}
	}



	public void onRefresh(final boolean b) {
		RestClient.get(getActivity(), new CachedAsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				
				if (getActivity() instanceof OnRefreshListener)
					((OnRefreshListener) getActivity()).setRefresh(true);

				if (!b) {
					String cache = getCache();
					if (cache == null) return;
					onWork(true, cache);
				}
			}
			
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
					onWork(false, response);
			}

			@Override
			public void onFailure(Throwable error) {
				AppMsg.makeText(getActivity(), R.string.api_error, AppMsg.STYLE_ALERT).show();
			}
			
			@Override
			public void onFinish() {
				if (getActivity() != null && getActivity() instanceof OnRefreshListener)
					((OnRefreshListener) getActivity()).setRefresh(false);
			}
			
			public void onWork(boolean cached, String response) {
				Gson gson = new Gson();
				Achievements achs = gson.fromJson(response, Achievements.class);
				if (getActivity() != null)
					PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("API_LIMIT", achs.API_Limit).commit();
				if (achs != null && achs.Success && GameFragment.this.getView() != null) {
					GridView gal = (GridView) GameFragment.this.getView().findViewById(R.id.gridView);
					mAdapter = new AchAdapter(GameFragment.this.getActivity(), achs.Achievements, GameFragment.this.getShownTitle());
					mAdapter.filter(getFilter());
					gal.setAdapter(mAdapter);	
					getView().findViewById(R.id.spinnerType).setVisibility(View.VISIBLE);
					((Spinner)getView().findViewById(R.id.spinnerType)).setOnItemSelectedListener(GameFragment.this);
				}
			}
		}, getShownUrl());
		
		getCatalog();
	}
	
	
	private void getCatalog() {
		RestClient.get(getActivity(), new CachedAsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				
				if (getActivity() instanceof OnRefreshListener)
					((OnRefreshListener) getActivity()).setRefresh(true);

				String cache = getCache();
				if (cache == null) return;
				Gson gson = new Gson();
				Catalog catalog = gson.fromJson(cache, Catalog.class);
				if (catalog != null && catalog.success) {
					if (getView().findViewById(R.id.imageViewBanner) != null) {
						aq.id(R.id.imageViewBanner).image(catalog.data.images.banner);
						if (getView().findViewById(R.id.MainImageViewBox) == null)
							getView().findViewById(R.id.imageViewBanner).setOnClickListener(GameFragment.this);
					}
					if (getView().findViewById(R.id.ButtonWallpaper) != null){
						getView().findViewById(R.id.ButtonWallpaper).setTag(catalog.data.images.background);
						getView().findViewById(R.id.ButtonWallpaper).setOnClickListener(GameFragment.this);
						getView().findViewById(R.id.ButtonWallpaper).setVisibility(View.VISIBLE);
					}
				}
				
			}
			
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				Gson gson = new Gson();
				Catalog catalog = gson.fromJson(response, Catalog.class);
				if (catalog != null && catalog.success) {
					aq.id(R.id.imageViewBanner).image(catalog.data.images.banner);
					if (getView().findViewById(R.id.MainImageViewBox) == null)
						getView().findViewById(R.id.imageViewBanner).setOnClickListener(GameFragment.this);
				}
				if (getView().findViewById(R.id.ButtonWallpaper) != null){
					getView().findViewById(R.id.ButtonWallpaper).setTag(catalog.data.images.background);
					getView().findViewById(R.id.ButtonWallpaper).setOnClickListener(GameFragment.this);
					getView().findViewById(R.id.ButtonWallpaper).setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onFailure(Throwable error) {
				AppMsg.makeText(getActivity(), R.string.api_error, AppMsg.STYLE_ALERT).show();
			}
			
			@Override
			public void onFinish() {
				if (getActivity() != null && getActivity() instanceof OnRefreshListener)
					((OnRefreshListener) getActivity()).setRefresh(false);
			}
		}, getShownCatalogUrl());
	}

	@Override
	public void onClick(View arg0) {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle(getShownTitle());
		View alert = LayoutInflater.from(getActivity()).inflate(R.layout.alert_picture, null);

		switch (arg0.getId()) {
		case R.id.MainImageViewBox:
		case R.id.imageViewBanner:
			aq.id(alert.findViewById(R.id.imageViewBoxBig)).image(getShownLargeBox());
			builder.setView(alert);
			builder.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.show();
			break;
			
		case R.id.ButtonWallpaper:
			final String url = (String)arg0.getTag();
			aq.id(alert.findViewById(R.id.imageViewBoxBig)).image(url);
			builder.setView(alert);
			builder.setPositiveButton(R.string.set_wallpaper, new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new getBackGroundTask().execute(url);
					}
			});
			builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.show();
			break;
		default:
			break;
		}
	}
	
	
	class getBackGroundTask extends AsyncTask<String, Void, Void> {
	    ProgressDialog progressDialog;
	
	    @Override
	    protected void onPreExecute() {
	      progressDialog = new ProgressDialog(getActivity());
	      progressDialog.setIndeterminate(true);
	      progressDialog.setMessage(getString(R.string.loading));
	      progressDialog.setCancelable(false);
	      progressDialog.show();
	    }
	    
	    @Override
	    protected Void doInBackground(String... params) {
	      try {
	        WallpaperManager.getInstance(getActivity()).setBitmap(Misc.loadBitmap(params[0]));
	      } catch (IOException e) {
		      AppMsg.makeText(getActivity(), R.string.wallpaper_error, AppMsg.STYLE_ALERT).show();
	      }
	      return null;
	    }
	    
	    @Override
	    protected void onPostExecute(Void result) {
	      progressDialog.dismiss();
	      AppMsg.makeText(getActivity(), R.string.wallpaper_updated, AppMsg.STYLE_INFO).show();
	    }
	    
	  }
}