package com.victorvieux.livedroid.data;

import java.io.Serializable;

public class Achievement implements Serializable{
	private static final long serialVersionUID = -1220168069432402922L;
	
	public int ID;
	public String TileUrl;
	public String Name;
	public String Description;
	public int Score;
	public boolean IsHidden;
	public boolean EarnedOn;
	public boolean IsOffline;
}
