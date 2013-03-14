package com.victorvieux.livedroid.api.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Achievement implements Serializable{
	public enum ACH_TYPE {ALL, WON, MISSING};

	public long ID;
	public String TileUrl;
	public String Name;
	public String Description;
	public int Score;
	public boolean IsHidden;
	public String  EarnedOn;
	public boolean IsOffline;
	
	public ACH_TYPE getType() {
		return (EarnedOn == null || EarnedOn.compareTo("false") == 0 ? ACH_TYPE.MISSING : ACH_TYPE.WON);
	}
}
