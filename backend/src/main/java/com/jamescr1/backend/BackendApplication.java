package com.jamescr1.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}

//
//package com.jamescr1.backend;
//
//import org.springframework.web.client.RestClient;
//
//public class BackendApplication {
//
//	public static void main(String[] args) {
//		String apiKey = System.getenv("ALPHA_VANTAGE_API_KEY");
//
//		if (apiKey == null || apiKey.isBlank()) {
//			System.out.println("Missing ALPHA_VANTAGE_API_KEY environment variable.");
//			return;
//		}
//
//		RestClient restClient = RestClient.builder()
//				.baseUrl("https://www.alphavantage.co")
//				.build();
//
//		String response = restClient.get()
//				.uri("query?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol={ticker}&apikey={apiKey}",
//						"VUSA.LON", apiKey)
//				.retrieve()
//				.body(String.class);
//
//		System.out.println(response);
//	}
//}

