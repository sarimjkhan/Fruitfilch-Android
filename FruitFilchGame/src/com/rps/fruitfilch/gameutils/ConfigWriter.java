package com.rps.fruitfilch.gameutils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.rps.fruitfilch.Bar;
import com.rps.fruitfilch.bars.BouncyBar;
import com.rps.fruitfilch.bars.IBouncyBar;
import com.rps.fruitfilch.bars.MagicPot;
import com.rps.fruitfilch.bars.NormalBar;
import com.rps.fruitfilch.bars.SpikyBar;
import com.rps.fruitfilch.bars.StickyBar;

public class ConfigWriter {
	
	public static void writeConfigurationFile(String inPath, final ArrayList<Bar> barCollection) throws IOException {
		
		FileWriter writer;
		BufferedWriter bufferedWriter;
		
		int lastLevelCreatedWas = 0;
				
		Scanner scanner = new Scanner(new File(inPath));
		while(scanner.hasNext()) {
			if(scanner.hasNext("Level"))  {
				++lastLevelCreatedWas;
			}
			scanner.next();
		}
		scanner.close();
		int toCreateLevel = ++lastLevelCreatedWas;
		//---------------Starts Writing from here----------------
		writer = new FileWriter(inPath, true);
		bufferedWriter = new BufferedWriter(writer);
		
		boolean nBar = false, bBar = false, ibBar = false, stBar = false, spBar = false, mBar = false;
		String toWrite = new String();
		toWrite = "Level " + String.valueOf(toCreateLevel) + "\n";
		
		Iterator i = barCollection.iterator();
		while(i.hasNext()) {
			Bar current = (Bar) i.next();
			if(current instanceof NormalBar) {
				if(!nBar) {
					toWrite = toWrite + "NormalBar\n";
					nBar = true;
				}				
			}
			else if(current instanceof BouncyBar) {
				if(!bBar) {
					toWrite = toWrite + "BouncyBar\n";
					bBar = true;
				}
			}
			else if(current instanceof IBouncyBar) {
				if(!ibBar) {
					toWrite = toWrite + "IBouncyBar\n";
					ibBar = true;
				}
			}
			else if(current instanceof StickyBar) {
				if(!stBar) {
					toWrite = toWrite + "StickyBar\n";
					stBar = true;
				}
			}
			else if(current instanceof SpikyBar) {
				if(!spBar) {
					toWrite = toWrite + "SpikyBar\n";
					spBar = true;
				}
			}
			else if(current instanceof MagicPot) {
				if(!mBar) {
					toWrite = toWrite + "MagicPot\n";
					mBar = true;
				}
			}
			float x = current.x; float y = current.y; float rotation = current.rotation;
			toWrite = toWrite + String.valueOf(x) + " " + String.valueOf(y) + " " + String.valueOf(rotation) + "\n";
		}
		bufferedWriter.write(toWrite);
		bufferedWriter.close();
	}
}
