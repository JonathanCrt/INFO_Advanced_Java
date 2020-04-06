package fr.umlv.healthcheck;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Objects;


public class HealthCheck {
	
	/**
	 * check if URI exist.
	 * @param uri
	 * @return true if result of request is 200, false otherwise.
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	public static boolean healthCheck(URI uri) throws InterruptedException {
		Objects.requireNonNull(uri);
		if(!uri.getScheme().equals("http")) {
			throw new IllegalArgumentException("URI scheme is not HTTP!"); 
		}
		try {
			HttpClient client = HttpClient.newBuilder()
					.version(Version.HTTP_1_1)
					.connectTimeout(Duration.ofSeconds(20))
				    .build();
				
			HttpRequest req = HttpRequest.newBuilder()
					.uri(uri)
					.build();
				
			HttpResponse<String> response = client.send(req, BodyHandlers.ofString());
			if(response.statusCode() == 200) {
				return true;
			}

		} catch (IOException e) {
			System.err.println(e);
		}
		return false;
	
	}
	

	public static void main(String[] args) throws IOException, InterruptedException  {
			
		/*
		var uri = URI.create("http://www.google.fr");
		System.out.println(healthCheck(uri));
		
		var map = Map.of("1", "http://www.google.fr", "2", "http://www.u-pem.fr");
        var uriFinder = URIFinder.fromMapGetLike("HEALTH_CHECK_URI", System::getenv);
		
		*/
		 
		
	}
	
}
