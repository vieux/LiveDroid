package com.victorvieux.livedroid.api.endpoints;

import java.io.Serializable;
import java.util.List;

import com.victorvieux.livedroid.api.data.Achievement;
import com.victorvieux.livedroid.api.data.Player;

@SuppressWarnings("serial")
public class Achievements extends Header implements Serializable {
	public Player Player;
	//public Achievements_Game Game;
	public List<Achievement> Achievements;
}
