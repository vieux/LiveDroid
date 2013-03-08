package com.victorvieux.livedroid.tools;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.victorvieux.livedroid.data.Game;
import com.victorvieux.livedroid.data.Player;

public class API_GAMES extends JSONObject {
	public enum GAME_TYPE {ALL, RETAIL, ARCADE};

	public API_GAMES(String str) throws JSONException {
		super(str);
	}
	
	public String getApiLimit(){
		return optString("API_Limit", "");
	}

	public Player getPlayer() {
		JSONObject jplayer = optJSONObject("Player");
		if (jplayer != null) {
			Player p = new Player();
			
			p.Gamertag = jplayer.optString("Gamertag", "error");
			
			JSONObject jstatus = jplayer.optJSONObject("Status");
			if (jstatus != null) {
				p.Status_Tier = jstatus.optString("Tier", "");
				p.Status_Online_Status = jstatus.optString("Online_Status", "");
			}
			
			JSONObject javatar = jplayer.optJSONObject("Avatar");
			if (javatar != null) {
				
				JSONObject jgamertile = javatar.optJSONObject("Gamertile");
				if (jgamertile != null)
					p.Avatar_Gamertile = jgamertile.optString("Large", "");
				
				JSONObject jgamerpic = javatar.optJSONObject("Gamerpic");
				if (jgamerpic != null)
					p.Avatar_Gamerpic = jgamerpic.optString("Large", "");
				
				p.Avatar_Body = javatar.optString("Body", "");
			}
			
			p.Gamerscore = jplayer.optString("Gamerscore", "");
			p.GameCount = jplayer.optString("GameCount", "");

			p.games  = getGames();
			return p;
		}
		return null;
	}
	
	
	public List<Game> getGames() {
		JSONArray jgames = optJSONArray("Games");
		if (jgames != null) {
			List<Game> gs = new ArrayList<Game>();
			
			Game g;
			JSONObject jgame;
			for (int i = 0; i < jgames.length(); i++) {
				jgame = jgames.optJSONObject(i);
				g = new Game();
				
				g.ID = jgame.optInt("ID", 0);
				g.Name = jgame.optString("Name", "");
				
				JSONObject jboxart = jgame.optJSONObject("BoxArt");
				if (jboxart != null) {
					g.BoxArt_Small = jboxart.optString("Small", "");
					g.BoxArt_Large = jboxart.optString("Large", "");
				}
				g.PossibleScore = jgame.optInt("PossibleScore", 0);
				if (g.PossibleScore == 0)
					continue;
				g.GameType = (g.PossibleScore > 500 ? GAME_TYPE.RETAIL : GAME_TYPE.ARCADE);
				g.PossibleAchievements = jgame.optInt("PossibleAchievements", 0);
				
				JSONObject jprogress = jgame.optJSONObject("Progress");
				if (jprogress != null) {
					g.Progress_Score = jprogress.optInt("Score", 0);
					g.Progress_Achievements = jprogress.optInt("Achievements", 0);
				}
				g.AchievementInfo = jgame.optString("AchievementInfo", "");
				
				gs.add(g);
			}
			return gs;
		}
		return null;
	}
	
	public boolean Success() {
		return optBoolean("Success", false);
	}
}
