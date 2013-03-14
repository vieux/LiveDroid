package com.victorvieux.livedroid.api.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Player implements Serializable{

	public String Gamertag;
	public int Gamerscore;
	public int PercentComplete;
	public Avatar Avatar;
	
	public class Avatar implements Serializable{
		public Picture Gamertile;
		public Picture Gamerpic;
		public String Body;
	}
}
