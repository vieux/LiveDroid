package com.victorvieux.livedroid.fragments;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;

import com.androidquery.AQuery;
import com.devspark.appmsg.AppMsg;
import com.google.gson.Gson;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.activities.OnRefreshListener;
import com.victorvieux.livedroid.adapters.AchAdapter;
import com.victorvieux.livedroid.api.RestClient;
import com.victorvieux.livedroid.api.data.Achievement.ACH_TYPE;
import com.victorvieux.livedroid.api.endpoints.Achievements;
import com.victorvieux.livedroid.tools.CachedAsyncHttpResponseHandler;

public  class GameFragment extends Fragment implements OnClickListener, OnItemSelectedListener {
	private AQuery aq = null;
	AchAdapter mAdapter;
	
	public static GameFragment newInstance(int index, String url, String title, String box_small, String box_large) {
		GameFragment f = new GameFragment();

		Bundle args = new Bundle();
		args.putInt("index", index);
		args.putString("url", url);
		args.putString("title", title);
		args.putString("box_small", box_small);
		args.putString("box_large", box_large);
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

	public String getx360aUrl() 
	{
		return "http://www.xbox360achievements.org/game/"+ getShownTitle().replace(' ', '-').replace(":", "").toLowerCase() + "/guide/";
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		aq = new AQuery(getActivity());
		aq.id(R.id.MainImageViewBox).image(getShownSmallBox());
		getView().findViewById(R.id.MainImageViewBox).setOnClickListener(this);

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
		((TextView) root.findViewById(R.id.MainTextViewTitle)).setText(getShownTitle());
		root.findViewById(R.id.imageViewBoxBig).setOnClickListener(this);
		
		GridView gal = (GridView) root.findViewById(R.id.gridView);
		gal.setEmptyView(root.findViewById(R.id.textViewLoading));
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
					Gson gson = new Gson();
					Achievements achs = gson.fromJson(cache, Achievements.class);
					PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("API_LIMIT", achs.API_Limit).commit();
					if (achs != null && achs.Success) {
						GridView gal = (GridView) GameFragment.this.getView().findViewById(R.id.gridView);
						mAdapter = new AchAdapter(GameFragment.this.getActivity(), achs.Achievements, GameFragment.this.getShownTitle());
						mAdapter.filter(getFilter());
						gal.setAdapter(mAdapter);	
						getView().findViewById(R.id.spinnerType).setVisibility(View.VISIBLE);
						((Spinner)getView().findViewById(R.id.spinnerType)).setOnItemSelectedListener(GameFragment.this);
					}
				}
			}
			
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
					Gson gson = new Gson();
					Achievements achs = gson.fromJson(response, Achievements.class);
					if (achs != null && achs.Success) {
						GridView gal = (GridView) GameFragment.this.getView().findViewById(R.id.gridView);
						mAdapter = new AchAdapter(GameFragment.this.getActivity(), achs.Achievements, GameFragment.this.getShownTitle());
						mAdapter.filter(getFilter());
						gal.setAdapter(mAdapter);	
						getView().findViewById(R.id.spinnerType).setVisibility(View.VISIBLE);
						((Spinner)getView().findViewById(R.id.spinnerType)).setOnItemSelectedListener(GameFragment.this);
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

		}, getShownUrl());
	}


	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.MainImageViewBox:
			getView().findViewById(R.id.relativeLayoutBox).setVisibility(View.VISIBLE);
			aq.id(R.id.imageViewBoxBig).image(getShownLargeBox());			
			break;
		default:
			getView().findViewById(R.id.relativeLayoutBox).setVisibility(View.GONE);
			break;
		}
	}
}