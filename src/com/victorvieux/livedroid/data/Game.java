package com.victorvieux.livedroid.data;

import java.io.Serializable;

import com.victorvieux.livedroid.tools.API_GAMES.GAME_TYPE;

public class Game implements Serializable{

	private static final long serialVersionUID = 6933892229418626505L;

	public int ID;
	public String Name;
	public String BoxArt_Large;
	public String BoxArt_Small;
	public int PossibleScore;
	public int PossibleAchievements;
	public int Progress_Score;
	public int Progress_Achievements;
	
	public String AchievementInfo;
	public GAME_TYPE GameType;
}
