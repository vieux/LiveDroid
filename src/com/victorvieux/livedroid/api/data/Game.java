package com.victorvieux.livedroid.api.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Game implements Serializable{
	public enum GAME_TYPE {ALL, RETAIL, ARCADE, APP};

	public long ID;
	public String Name;
	public String MarketplaceURL;
	public Picture BoxArt;
	public String CatalogLink;
	public String AchievementInfo;
	public int PossibleScore;
	public int PossibleAchievements;
	public Progress Progress;
	
	public class Progress implements Serializable{
		public int Score;
		public int Achievements;
		public String LastPlayed;
	}
	
	public GAME_TYPE getType() {
		return (PossibleScore  == 0 ? GAME_TYPE.APP : PossibleScore > 500 ? GAME_TYPE.RETAIL : GAME_TYPE.ARCADE);
	}

}
