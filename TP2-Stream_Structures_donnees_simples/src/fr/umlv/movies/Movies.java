package fr.umlv.movies;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Movies {
	public static void main(String[] args) throws IOException {
		/**
		 * qui associe des objets de type A à B.
		 * Map<B, A>
		 */
		var path = Path.of("data/movies.txt");	
		
		displayFirst50Actors(path);
		System.out.println(countTotalActors(path));
		
	}
	/**
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	/*
	public Map<String, List<String>> actorsByMovie(Path path) throws IOException {
		try(Stream<String> lines = Files.lines(path)){
			var moviesMap = new HashMap<String, List<String>>();
			lines.forEach(line  -> {
				// tableau de stream
				var tab = line.split(";");
				
				 //Je conserve le tableau
				 //je split.
				 //je construis un stream avec tout le tableau
				 //je "skip" pour garder tout sauf le premier élément.
				 //je re-transforme en List
				 
				moviesMap.put(tab[0], Arrays.stream(tab).skip(1).collect(Collectors.toList()));
				
				
				 //args -> (clé, valeur)
				 //type film - liste acteurs associés
				 
			});
			return moviesMap;
		}
		
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	
	public static Map<String, List<String>> actorsByMovie(Path path) throws IOException {
		try(Stream<String> lines = Files.lines(path)){
			
			return lines.map(line -> line.split(";"))
					.collect(
							Collectors.toUnmodifiableMap(
									tab -> tab[0], tab -> Arrays.stream(tab).skip(1).collect(Collectors.toList())));
		}
		
	}
	
	/**
	 * display the 50 first actors which had played at least into 1 movie
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static void displayFirst50Actors(Path path) throws IOException {
		try(Stream<String> lines = Files.lines(path)){
			
			lines.flatMap(line -> Arrays.stream(line.split(";")).skip(1)).limit(50).forEach(System.out::println);
		}
	}
	
	/**
	 * count the total number of players who played in at least one film
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Long countTotalActors(Path path) throws IOException {
		try(Stream<String> lines = Files.lines(path)){
			
			return lines.flatMap(line -> Arrays.stream(line.split(";")).skip(1)).count();
				
		}
	}
	/**
	 * associates with a film list actors and returns the total number of different actors who 
	 * starred in the films of the map
	 * @param map
	 * @return long
	 */
	public static long numberOfUniqueActors(Map<String, List<String>> map) {
		return map.values()
		.stream()
		.flatMap(Collection::stream)
		.distinct() 
		.collect(Collectors.toList())
		.size();
	}
	
	/**
	 * displays the number of movies in which an actor played
	 * @param map
	 * @return Map<String, Long>
	 */
	public static Map<String, Long> numberOfMoviesByActor(Map<String, List<String>> map) {
		 return map.values()
		 .stream()
		 .flatMap(Collection::stream)
		 .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}
	
	/**
	 * returns a pair containing the actor who played in the movie over and the number of films in which he played
	 * @param map
	 * @return
	 */
	public static Optional<Entry<String, Long>> actorInMostMovies(Map<String, Long> map) {
		
		return map.entrySet()
				.stream()
				.collect(Collectors.maxBy((x1, x2) -> {
					return x1.getValue().compareTo(x2.getValue());
				}));
	}
	
}
