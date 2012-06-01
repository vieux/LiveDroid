/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.victorvieux.livedroid.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.android.ex.carousel.CarouselView;
import com.android.ex.carousel.CarouselViewHelper;
import com.androidquery.AQuery;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.data.Player;
import com.victorvieux.livedroid.tools.Network;

public class CarouselActivity extends BaseActivty {
    private static final int CARD_SLOTS = 56;
    private static final int SLOTS_VISIBLE = 7;

    protected static final boolean DBG = false;
    private static final int DETAIL_TEXTURE_WIDTH = 200;
    private static final int DETAIL_TEXTURE_HEIGHT = 80;
    private static final int VISIBLE_DETAIL_COUNT = 3;
    private static boolean INCREMENTAL_ADD = false; // To debug incrementally adding cards
    private CarouselView mView;
    private Paint mPaint = new Paint();
    private CarouselViewHelper mHelper;
    private Bitmap mBorder;
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu_carousel, menu);
        return true;
    } 

    class LocalCarouselViewHelper extends CarouselViewHelper {
        private DetailTextureParameters mDetailTextureParameters
                = new DetailTextureParameters(5.0f, 5.0f, 3.0f, 10.0f);
    	final AQuery aq;

        LocalCarouselViewHelper(Context context) {
            super(context);
    		aq = new AQuery(context);

        }

        @Override
        public void onCardSelected(final int id) {
        	 Intent intent = new Intent();
             intent.setClass(CarouselActivity.this, GameActivity.class);
             intent.putExtra("index", id);
             intent.putExtra("forced", true);
             intent.putExtra("box", getPlayer().games.get(id).BoxArt_Small);
             intent.putExtra("url", getPlayer().games.get(id).AchievementInfo);
             intent.putExtra("title", getPlayer().games.get(id).Name);
             startActivity(intent);	
        }

        @Override
        public void onDetailSelected(final int id, int x, int y) {
        	onCardSelected(id);
        }

        @Override
        public void onCardLongPress(int n, int touchPosition[], Rect detailCoordinates) {
            //postMessage("Selection", "Long press on card " + n);
        }

        @Override
        public DetailTextureParameters getDetailTextureParameters(int id) {
            return mDetailTextureParameters;
        }
        
        

        @Override
        public Bitmap getTexture(int n) {
            Bitmap bitmap =  aq.getCachedImage(getPlayer().games.get(n).BoxArt_Large);
            if (bitmap == null)
            	bitmap = Network.loadBitmap(getPlayer().games.get(n).BoxArt_Large, aq);
            return bitmap;
        }

        @Override
        public Bitmap getDetailTexture(int n) {
            Bitmap bitmap = Bitmap.createBitmap(DETAIL_TEXTURE_WIDTH, DETAIL_TEXTURE_HEIGHT, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawARGB(32, 10, 10, 10);
            mPaint.setTextSize(15.0f);
            mPaint.setTextAlign(Align.CENTER);
            mPaint.setAntiAlias(true);
            canvas.drawText(getPlayer().games.get(n).Name, DETAIL_TEXTURE_WIDTH/2, DETAIL_TEXTURE_HEIGHT/2+4, mPaint);
            return bitmap;
        }
    };

    private Runnable mAddCardRunnable = new Runnable() {
        public void run() {
            if (mView.getCardCount() < getPlayer().games.size()) {
                mView.createCards(mView.getCardCount() + 1);
                mView.postDelayed(mAddCardRunnable, 2000);
            }
        }
    };

    void postMessage(final CharSequence title, final CharSequence msg) {
        runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(CarouselActivity.this)
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPlayer((Player) getIntent().getExtras().getSerializable("player"));

        setContentView(R.layout.carousel);
        mView = (CarouselView) findViewById(R.id.carousel);
        mView.getHolder().setFormat(PixelFormat.RGBA_8888);
        mPaint.setColor(0xffffffff);
        final Resources res = getResources();

        mHelper = new LocalCarouselViewHelper(this);
        mHelper.setCarouselView(mView);
        mView.setSlotCount(CARD_SLOTS);
        mView.createCards(INCREMENTAL_ADD ? 1: getPlayer().games.size());
        mView.setVisibleSlots(SLOTS_VISIBLE);
        mView.setStartAngle((float) -(2.0f*Math.PI * 5 / CARD_SLOTS));
        mBorder = BitmapFactory.decodeResource(res, R.drawable.border);
        mView.setDefaultBitmap(mBorder);
        mView.setLoadingBitmap(mBorder);
        mView.setBackgroundColor(0.8f, 0.8f, 0.8f, 0.5f);
        mView.setRezInCardCount(3.0f);
        mView.setFadeInDuration(250);
        mView.setVisibleDetails(VISIBLE_DETAIL_COUNT);
        mView.setDragModel(CarouselView.DRAG_MODEL_PLANE);
        if (INCREMENTAL_ADD) {
            mView.postDelayed(mAddCardRunnable, 2000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHelper.onPause();
    }

}
