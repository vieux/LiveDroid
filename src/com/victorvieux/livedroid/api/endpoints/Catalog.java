package com.victorvieux.livedroid.api.endpoints;

import java.io.Serializable;
import java.util.List;

import com.victorvieux.livedroid.api.data.Game_catalog;
import com.victorvieux.livedroid.api.data.Images_Picture;

@SuppressWarnings("serial")
public class Catalog implements Serializable{
	public boolean success;
	public Data data;
	
	public class Data implements Serializable {
		public Game_catalog game;
		public Images_Picture images;
		public List<String> categories;
		public boolean cached;
	}

}
