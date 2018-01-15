/* WordCloud
 * 
 * Henry Berger and Emmanuel Enabuelele
 * 
 * 12/07/17
 * 
 * This program creates a WordCloud. It takes words from a inputted txt file, and excludes words from the
 * StopWords txt file. It then uses JavaFX to display the words, and the more times a word comes up, the
 * larger it is on the display.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map.Entry;
import javafx.geometry.*;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Font;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WordCloud extends Application
{
	static  FlowPane flowPane = new FlowPane();
	
	public static void main(String[] args) 
	{
		
		Scanner stopFile = null;
		Scanner wordFile = new Scanner(System.in);
        
        boolean gotFile = false;
        while (!gotFile) 
        {
    		File file = new File("stop-words.txt");
	        try {
				stopFile = new Scanner(file);
				gotFile = true;
			} catch (FileNotFoundException e) {
				System.out.println("The stop words file was not found.");
			}
        }
        
        //Put all the stop words from the file into a hashset
        Set<String> stopWords = setOfStopWords(stopFile);
        
		//Take in file until it is in .txt form
        gotFile = false;
        while (!gotFile) 
        {
        	System.out.println("Enter your file");
    		File file = new File(wordFile.next());
	        try {
				stopFile = new Scanner(file);
				gotFile = true;
			} catch (FileNotFoundException e) {
				System.out.println("The file was not found");
			}
        }
        
        //Put all the words except for the stop words into a hashmap
        Map<String, Integer> allWords = createAllWordsHashMap(stopFile, stopWords);
        
        //sort the words from most frequent to least frequent
        Map<String, Integer> wordsSorted = allWords;
        
        displayWords(wordsSorted);     
        
        //turn the words from the file into a tree map	
        wordsSorted = makeTreeMap(allWords);
        
        //launch FX window
        Application.launch(args);
	}
	
	//puts all stop words into a hashset and returns it
	private static Set<String> setOfStopWords(Scanner file) 
	{
		Set<String> stopWords = new HashSet<String>();
		
		if (file == null) 
		{
			return stopWords;
		}
		
		while(file.hasNext()) 
		{		
			stopWords.add(file.next());
		}
		
		return stopWords;
	}
	
	//Put all words into a hashmap and increment their frequency, does not put in stop words and returns it
	private static Map<String, Integer> createAllWordsHashMap(Scanner file, Set<String> stopWords) 
	{
		Map<String, Integer> allWords = new HashMap<String, Integer>();
		
		if (file == null)
		{
			return allWords;
		}
		
		while(file.hasNext()) 
		{
			String indv = file.next();
			
			//does not allow punctuation
			indv = indv.replaceAll("[^\\x00-\\x7F]", "");	
			
			//only letters and all to lower case
			indv = indv.replaceAll("[^a-zA-Z ]", "").toLowerCase();		
			
			//no two letter words
			if (indv.length() < 3) 
			{									
				continue;
			} 
			else if (stopWords.contains(indv)) 
			{						
				continue;
			} 
			else if (allWords.containsKey(indv))
			{
				//increments frequency
				int count = allWords.get(indv);
				allWords.put(indv, count+1);
			} 
			else 
			{
				//puts word into hashmap
				allWords.put(indv, 1);									
			}
		}
		
		return allWords;
	}
	
	//versions a treemap version of the map
	public static <K extends Comparable<? super K>,V extends Comparable<? super V>> Map<K, V> makeTreeMap(Map<K, V> map) 
	{
        List<Entry<K, V>> words = new LinkedList<Entry<K, V>>(map.entrySet());		
        
        Map<K, V> sortedWords = new TreeMap<K, V>();										
      
        //adds words to treemap
        for(Entry<K,V> word: words)
        {
        	sortedWords.put(word.getKey(), word.getValue());
        }
      
        return sortedWords;
	}
	
	@SuppressWarnings("restriction")
	public static void displayWords(Map<String, Integer> map)
	{
		int highest = 0;
		int lowest = 0;
		
		//finds highest and lowest occurences of words
		boolean firstWord = true;
		for (Map.Entry<String, Integer> word : map.entrySet()) 
		{
			if (firstWord == true) 
			{
				highest = word.getValue();
				lowest = word.getValue();
				firstWord = false;
			}
			if (word.getValue() > highest)
			{
				highest = word.getValue();
			}
			if (word.getValue() < lowest)
			{
				lowest = word.getValue();
			}
		}
		
		//only allows fonts to go up to size 50
		int highestFont = 50;
				
		for (Map.Entry<String, Integer> word : map.entrySet())
		{
			//calculates how large the text should be based on its number of occurences
			int fontNum = highestFont*(word.getValue()-lowest)/(highest - lowest);
			
			Label wordLabel = new Label(word.getKey());
			
			//sets words apart from each other
			wordLabel.setLineSpacing(10);
			
			//sets the color to each word randomly 
			int x = (int)(Math.random()*200)+25;
			int y = (int)(Math.random()*200)+25;
			int z = (int)(Math.random()*200)+25;
			wordLabel.setTextFill(Color.rgb(x,y,z));
			
		
			//sets wordLabel's font to correct size
			if (fontNum <= 5) 
			{
				wordLabel.setFont(Font.font("Veranda", FontWeight.EXTRA_BOLD,15));
				wordLabel.setRotate(90);
			} 
			else if (fontNum <= 10) 
			{
				wordLabel.setFont(Font.font("Veranda", FontWeight.EXTRA_BOLD,20));
			} 
			else if (fontNum <= 15)
			{
				wordLabel.setFont(Font.font("Veranda", FontWeight.EXTRA_BOLD,25));
			}
			else if (fontNum <= 20) 
				
			{
				wordLabel.setFont(Font.font("Veranda", FontWeight.EXTRA_BOLD,30));
				wordLabel.setRotate(270);
			} 
			else if (fontNum <= 25) 
			{
				wordLabel.setFont(Font.font("Veranda", FontWeight.EXTRA_BOLD,35));

			} else if (fontNum <= 30) 
			{
				wordLabel.setFont(Font.font("Veranda", FontWeight.EXTRA_BOLD,37));

			} else if (fontNum <= 35) 
			{
				wordLabel.setFont(Font.font("Veranda", FontWeight.EXTRA_BOLD,40));

			} else if (fontNum <= 40) 
			{
				wordLabel.setFont(Font.font("Veranda", FontWeight.EXTRA_BOLD,45));
			} 
			else 
			{
				wordLabel.setFont(Font.font("Veranda", FontWeight.EXTRA_BOLD,50));
			}
			
			wordLabel.setPadding(new Insets(10));

			//adds all words to the flowPane
			flowPane.getChildren().add(wordLabel);
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		primaryStage.setTitle("Word Cloud");
	        
        Scene scene = new Scene(flowPane, 700, 500);
        
        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
	}
}
