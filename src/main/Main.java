package main;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import neuralNetwork.Network;

public class Main {
	private static int X, Y;
	static List<LearningUnit> result = new ArrayList<>();
	static Shot shot = new Shot();
	
	public static void main(String[] args) {
	    EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	    });
	    //W - [0, 425]
	    //H - [0, 235]
	}
	
	/*private static LearningUnit makeValues(int x, int y) {
		int wd = 100;
		int wV0 = 0,
			wAng = 0;
		for(int ang = 0; ang <= 90; ang += 10) {
			for(int v0 = 2; v0 <= 20; v0 += 2) {
				int d = shot.makeShot(v0, ang, x, y);
				if(d < wd) {
					wd = d;
					wV0 = v0;
					wAng = ang;
				}
			}
		}
		return new LearningUnit(wAng, wV0, wd, x, y);
	}*/

}
