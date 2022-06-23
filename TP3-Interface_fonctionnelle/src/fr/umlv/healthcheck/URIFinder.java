package fr.umlv.healthcheck;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

@FunctionalInterface
public interface URIFinder {
	// implicite abstract
	Optional<URI> find();
	
	/**
	 * build and return Optional of URI Object
	 * @param s
	 * @return 
	 */
	static Optional<URI> buildAnURI(String s) {
		try {
			return Optional.of(new URI(s));
		} catch (URISyntaxException e) {
			return Optional.empty();
		}
	}
	
	/**
	 * return an URIFinder from an array of string passed in parameters
	 * @param args
	 * @return
	 */
	static URIFinder fromArguments(String[] args) {
		Objects.requireNonNull(args);
		return () -> Optional.of(args)
				.filter(a -> a.length != 0)
				// Optional de Optional
				.flatMap(a -> buildAnURI(a[0]));
	}
	
	/**
	 * return an URIFinder from a string passed in parameter
	 * @param st
	 * @return
	 */
	public static URIFinder fromURI (String st) {
		Objects.requireNonNull(st);
		return () -> buildAnURI(st);
	}
	
	/*
	 * combines two URIFinder so that if the first is not URIFinder URI, while the second is trying to find his URI.
	 * @param uri
	 * @return
	 */
	default URIFinder or(URIFinder uri){
		Objects.requireNonNull(uri);
		return () -> {
			return find().or(() -> uri.find());
		};
	}
	
	/**
	 * return an URIfinder to find the key value in the map.
	 * @param key
	 */
	public static<E> URIFinder fromMapGetLike(E key , Function<? super E, String> get) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(get);
		return () -> {
			return Optional.ofNullable(get.apply(key))
					.flatMap(URIFinder::buildAnURI);
		};
	}
	
	/**
	 * returns URI partner to the key in the properties file if the association exists that is well worth a combined validated URI.
	 * @param path
	 * @param key
	 * @return URIFinder
	 */
	public static URIFinder fromPropertyFile(Path path, String key) {
		Objects.requireNonNull(path);
		Objects.requireNonNull(key);
		var file = path.toFile();
		
		try {
			FileReader reader = new FileReader(file);
			BufferedReader buffer = new BufferedReader(reader);
			Properties ppt = new Properties();
			ppt.load(buffer);
			ppt.forEach((x, value) -> System.out.println("Key : " + x + ", Value : " + value));
			var value =  ppt.getProperty(key);
			buffer.close();
			/*
			if(value != null) {
				return () -> buildAnURI(value);
			}
			else {
				return () -> Optional.empty();
			}
			*/
			
			return () -> {
				return Optional.ofNullable(value)
						.flatMap(elt -> buildAnURI(value));
			};
		}
		catch (IOException io) {
			io.printStackTrace();
			return () -> Optional.empty();
		}
		
	}
	
	
}
