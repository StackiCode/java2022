import static javax.sound.sampled.AudioSystem.*;

import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import org.json.simple.*;
import org.json.simple.parser.*;

/*
    To compile: javac SpotifyLikeApp.java
    To run: java SpotifyLikeApp
 */

// declares a class for the app
public class App {

  // global variables for the app
  String status;
  Long position;
  static Clip audioClip;

  /*
    *** IMPORTANT NOTE FOR ALL STUDENTS *******

    This next line of code is a "path" that students will need to change in order to play music on their
    computer.  The current path is for my laptop, not yours.
    
    If students who do not understand whre files are located on their computer or how paths work on their computer, 
    should immediately complete the extra credit on "Folders and Directories" in the canvas modules.  
    
    Knowing how paths work is fundamental knowledge for using a computer as a technical person.

    Students who do not know what a path are often not able complete this assignment succesfullly.  Please
    do the extra credit if you are confused. :)  
    
    Thank you!  -Gabriel

    */

  private static String basePath = new File("").getAbsolutePath();

  // "main" makes this class a java app that can be executed
  public static void main(final String[] args) {
    System.out.println(basePath);
    // reading audio library from json file
    JSONArray library = readAudioLibrary();

    // create a scanner for user input
    Scanner input = new Scanner(System.in);

    String userInput = "";
    while (!userInput.equals("q")) {
      menu();

      // get input
      userInput = input.nextLine();

      // accept upper or lower case commands
      userInput = userInput.toLowerCase();

      // do something
      handleMenu(userInput, library);
    }

    // close the scanner
    input.close();
  }

  /*
   * displays the menu for the app
   */
  public static void menu() {
    System.out.println("---- SpotifyLikeApp ----");
    System.out.println("[H]ome");
    System.out.println("[S]earch by title");
    System.out.println("[L]ibrary");
    System.out.println("[P]lay");
    System.out.println("[Q]uit");

    System.out.println("");
    System.out.print("Enter q to Quit:");
  }

  public static void displayLibrary(JSONArray library) {
		//read files from Music files
		JSONArray Js = readAudioLibrary();
    for(int i =0; i < Js.size(); i++) {
      System.out.print(i + "/");
			System.out.println(Js.get(i));
		}
    System.out.println("Choose which music file to play by number(0-9):");
		Scanner sc = new Scanner(System.in);
    play(library, sc.nextInt());
	}

  public static void homeDisplay(String userName) {
		String[] splitName = userName.split("");
		String[] wl = "WelcomeTo|MuZicApp|".split("");
		String fLine = "";
		String hLine = "";
		String mLine = "";
		String wLine = "";
		//update fLine
		for(int w =0; w < 40; w++) {
			fLine+="*";
		}
		//update hLine
		for(int w =0; w < 40; w++) {
			if(w==0||w==39)
				hLine+="*";
			else
				hLine+=" ";
		}
		//update wLine
		for(int w =0; w < 40; w++) {
			if(w >= 12 && w < (wl.length + 12))
				wLine += wl[w - 12];
			else if(w == 0||w==39)
				wLine+="*";
			else
				wLine += " ";
		}
		//update mLine
		for(int w =0; w < 40; w++) {
			if(w >= 15 && w < (splitName.length + 15))
				mLine += splitName[w - 15];
			else if(w == 0||w==39)
				mLine+="*";
			else
				mLine += " ";
		} 
		for(int h = 0; h < 19; h++) {
			if(h == 0 || h == 18)
				System.out.println(fLine);
			else if(h == 8)
				System.out.println(wLine);
			else if(h == 9)
				System.out.println(mLine);
			else
				System.out.println(hLine);
		}		
	}


  /*
   * handles the user input for the app
   */
  public static void handleMenu(String userInput, JSONArray library) {
    switch (userInput) {
      case "h":
        System.out.println("-->Home<--");
        System.out.println("Enter User Name(NoSpace):");
        Scanner sc1 = new Scanner(System.in);						
        homeDisplay(sc1.next());
        break;
      case "s":
        System.out.println("-->Search by title<--");
        System.out.println("Enter Song Name(NoSpace):");
        Scanner sc2 = new Scanner(System.in);						
        searchLibrary(sc2.nextLine());
        break;
      case "l":
        System.out.println("-->Library<--");
        displayLibrary(library);
        break;
      case "p":
        System.out.println("-->Play<--");
        play(library, 3);
        break;
      case "q":
        System.out.println("-->Quit<--");
        break;
      default:
        break;
    }
  }
  public static void searchLibrary(String s) {
    JSONArray Js = readAudioLibrary();
    for(int i =0; i < Js.size(); i++) {
      if(Js.get(i).toString().contains(s))
			  System.out.println(Js.get(i));
		}
  }
  /*
   * plays an audio file
   */
  public static void play(JSONArray library,int songIndex) {
    // open the audio file

    // get the filePath and open a audio file
    
    JSONObject obj = (JSONObject) library.get(songIndex);
    final String filename = (String) obj.get("filename");
    final String filePath = basePath + "/wav/" + filename;
    final File file = new File(filePath);

    // stop the current song from playing, before playing the next one
    if (audioClip != null) {
      audioClip.close();
    }

    try {
      // create clip
      audioClip = AudioSystem.getClip();

      // get input stream
      final AudioInputStream in = AudioSystem.getAudioInputStream(file);

      audioClip.open(in);
      audioClip.setMicrosecondPosition(0);
      audioClip.loop(Clip.LOOP_CONTINUOUSLY);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //
  // Func: readJSONFile
  // Desc: Reads a json file storing an array and returns an object
  // that can be iterated over
  //
  public static JSONArray readJSONArrayFile(String fileName) {
    // JSON parser object to parse read file
    JSONParser jsonParser = new JSONParser();

    JSONArray dataArray = null;

    try (FileReader reader = new FileReader(fileName)) {
      // Read JSON file
      Object obj = jsonParser.parse(reader);

      dataArray = (JSONArray) obj;
      // System.out.println(dataArray);

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return dataArray;
  }

  // read the audio library of music
  public static JSONArray readAudioLibrary() {
    final String jsonFileName = "audio-library.json";
    final String filePath = basePath + "/" + jsonFileName;

    JSONArray jsonData = readJSONArrayFile(filePath);

    System.out.println("Reading the file " + filePath);

    return jsonData;
  }
}
