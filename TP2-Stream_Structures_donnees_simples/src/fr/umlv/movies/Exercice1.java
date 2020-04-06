package fr.umlv.movies;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;


public class Exercice1 {

	public static void main(String[] args) throws IOException {
		
		var path = Path.of("data/movies.txt");
		
		
		/*
		Stream<String> st = Files.lines(path);
		
		try {
			// Créer un stream des lignes du fichier
			st = Files.lines(path);
			
		} catch (IOException e) {
			
			return;
		}
	
		try {
			st.forEach(System.out::println);
		}
		finally{
			st.close();
		}
		*/
		
		
		try(Stream<String> st = Files.lines(path)){
			st.forEach(System.out::println);
		}
		
		
	
	}
	
	
	
	
	
	
}
