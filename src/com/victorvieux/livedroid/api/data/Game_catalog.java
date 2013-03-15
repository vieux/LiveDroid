package com.victorvieux.livedroid.api.data;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Game_catalog implements Serializable{

	public String id;
	public ArrayList<String> id_clean;
	public long titleid;
	public String mediatype;
	public String title;
	public String nicetitle;
	public String desc;
	public String nicedesc;
	public String vaildate;
	public String releasedate;
	public String developer;
	public String publisher;
	public float rating;
}
