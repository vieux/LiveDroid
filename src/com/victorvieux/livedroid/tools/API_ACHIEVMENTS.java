package com.victorvieux.livedroid.tools;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.victorvieux.livedroid.data.Achievement;

public class API_ACHIEVMENTS extends JSONObject {
	
	public API_ACHIEVMENTS(String str) throws JSONException {
		super(str);
	}

	
	public List<Achievement> getAchs() {
		JSONArray jachs = optJSONArray("Achievements");
		if (jachs != null) {
			List<Achievement> as = new ArrayList<Achievement>();
			
			Achievement a;
			JSONObject jach;
			for (int i = 0; i < jachs.length(); i++) {
				jach = jachs.optJSONObject(i);
				a = new Achievement();
				
				a.ID = jach.optInt("ID", 0);
				a.Name = jach.optString("Name", "");
				a.TileUrl = jach.optString("TileUrl", "");
				a.Description = jach.optString("Description", "");
				a.Score = jach.optInt("Score", 0);
				a.IsHidden = jach.optBoolean("IsHidden",false);
				a.EarnedOn = jach.optBoolean("EarnedOn",true);
				a.IsOffline = jach.optBoolean("IsOffline",false);
				
				if (a.IsHidden)
					continue;
				
				as.add(a);
			}
			return as;
		}
		return null;
	}
	
	public boolean Success() {
		return optBoolean("Success", false);
	}
}
