package com.victorvieux.livedroid.fragments;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.github.ignition.core.tasks.IgnitedAsyncTask;
import com.github.ignition.support.cache.AbstractCache;
import com.github.ignition.support.http.IgnitedHttp;
import com.github.ignition.support.http.IgnitedHttpResponse;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.adapters.AchAdapter;
import com.victorvieux.livedroid.tools.API_ACHIEVMENTS;
import com.victorvieux.livedroid.tools.SSL;

public  class GameFragment extends Fragment implements OnClickListener {
    private IgnitedHttp http;
    private AQuery aq = null;
    public static GameFragment newInstance(int index, String url, String title, String box) {
    	GameFragment f = new GameFragment();

        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putString("url", url);
        args.putString("title", title);
        args.putString("box", box);
        f.setArguments(args);

        return f;
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
    public String getShownBox() {
        return getArguments().getString("box");
    }
    
    public String getx360aUrl() 
    {
    	return "http://www.xbox360achievements.org/game/"+ getShownTitle().replace(' ', '-').replace(":", "").toLowerCase() + "/guide/";
    }
        
    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
    	aq = new AQuery(getActivity());
   		aq.id(R.id.MainImageViewBox).image(getShownBox());
       	getView().findViewById(R.id.MainImageViewBox).setOnClickListener(this);

    }
    
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
               return null;
        }
        
        http = new IgnitedHttp(getActivity());

        http.enableResponseCache(getActivity(), 3, 60*24, 1,
                AbstractCache.DISK_CACHE_INTERNAL);
        
        http.setHttpClient(SSL.getNewHttpClient());

        HttpTask task = new HttpTask(this, http, getShownUrl());
        task.connect(getActivity());
        task.execute();
        
        
        GuideHttpTask gtask = new GuideHttpTask(this, http, getx360aUrl());
        gtask.connect(getActivity());
        gtask.execute();
        
        
        View root = inflater.inflate(R.layout.game, container, false);
       	((TextView) root.findViewById(R.id.MainTextViewTitle)).setText(getShownTitle());
       	root.findViewById(R.id.imageViewBoxBig).setOnClickListener(this);
   		return root;
    }
    
    
    public static final class HttpTask extends IgnitedAsyncTask<Context, Void, Void, IgnitedHttpResponse> {
    	private GameFragment frag;
		private IgnitedHttp http;
		private String url;
		
		
		public HttpTask(GameFragment frag,  IgnitedHttp http, String url) {
			this.frag = frag;
		    this.http = http;
		    this.url = url;
		}
		
		@Override
		public boolean onTaskStarted(Context context) {
		    return true;
		}
		
		@Override
		public IgnitedHttpResponse run(Void... params) throws Exception {
		    return http.get(url , true).retries(3).expecting(200).send();
		}
		
		@Override
		public boolean onTaskFailed(Context context, Exception error) {
		    super.onTaskFailed(context, error); // prints a stack trace
		    if (frag.getView() != null) {
		    	frag.getView().findViewById(R.id.progressBarLoading).setVisibility(View.GONE);
		    }
		    return true;
		}
		
		@Override
		public boolean onTaskSuccess(Context context, IgnitedHttpResponse response) {
		    if (frag.getView() != null) {
			frag.getView().findViewById(R.id.progressBarLoading).setVisibility(View.GONE);

		    try {
				API_ACHIEVMENTS api_achs = new API_ACHIEVMENTS(response.getResponseBodyAsString());
				if (api_achs.Success()) {
					GridView gal = (GridView) frag.getView().findViewById(R.id.gridView);
					gal.setAdapter(new AchAdapter(context, api_achs.getAchs(), frag.getShownTitle()));	
				}
			} catch (Exception e) {
			}
		    }
		    return true;
		}
	}
    
    
    
    
    public static final class GuideHttpTask extends IgnitedAsyncTask<Context, Void, Void, IgnitedHttpResponse> {
    	private GameFragment frag;
		private IgnitedHttp http;
		private String url;

		
		public GuideHttpTask(GameFragment frag,  IgnitedHttp http, String url) {
			this.frag = frag;
		    this.http = http;
		    this.url = url;
		}
		
		@Override
		public boolean onTaskStarted(Context context) {
		    return true;
		}
		
		@Override
		public IgnitedHttpResponse run(Void... params) throws Exception {
			return http.get(url , true).retries(1).expecting(200).send();
		}
		
		
		@Override
		public boolean onTaskSuccess(Context context, IgnitedHttpResponse response) {
		    if (frag.getView() != null) {
		    	try {
					if (response.getResponseBodyAsBytes().length > 1000)	
				    	frag.getView().findViewById(R.id.ButtonGuide).setVisibility(View.VISIBLE);	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
				frag.getView().findViewById(R.id.ButtonGuide).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
						frag.startActivity(i);
					}
				});
		    }
		    return true;
		}
	}




	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.MainImageViewBox:
			getView().findViewById(R.id.relativeLayoutBox).setVisibility(View.VISIBLE);
	   		aq.id(R.id.imageViewBoxBig).image(getShownBox().replace("small", "large"));			
			break;
		default:
			getView().findViewById(R.id.relativeLayoutBox).setVisibility(View.GONE);
			break;
		}
	}
    
}