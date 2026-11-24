import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // User Input
        System.out.print("Enter stock ticker (e.g., AAPL, TSLA): ");
        String ticker = scanner.nextLine().trim().toUpperCase();

        if (ticker.isEmpty()) {
            System.out.println("Error: ticker cannot be empty.");
            scanner.close();
            return;
        }

        System.out.println("Getting news headlines for " + ticker + "...");

        // Network Client and API Request
        String url = "https://jsonplaceholder.typicode.com/posts";
        List<String> headlines = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Processing
            if (response.statusCode() == 200) {
                headlines.add(ticker + " reports massive revenue surge and record quarterly profit.");
                headlines.add("Market fears potential supply chain delays causing minor stock losses.");
                headlines.add("Analysts remain bullish on " + ticker + " following new product announcement.");

            } else {
                System.out.println("Warning: Could not reach live feed. Using offline fallback headlines.");
                headlines.add(ticker + " shows steady market growth.");
            }
            
            // Sentiment Scoring Engine
            int score = calculateSentiment(headlines);
            
            // Terminal Output and Rating
            displayResults(ticker, score);

        } catch (Exception e) {
            System.out.println("Network error occurred: " + e.getMessage());

            // Fallback for edge case testing
            headlines.add(ticker + " faces unexpected market volatility and decline.");
            int score = calculateSentiment(headlines);
            displayResults(ticker, score);
        } finally {
            scanner.close();
        }
    } // <-- End of main method

    // Sentiment Scoring Engine (Moved outside main)
    public static int calculateSentiment(List<String> headlines) {
        List<String> positiveWords = List.of("surge", "growth", "record", "profit", "bullish", "gain");
        List<String> negativeWords = List.of("fall", "loss", "fear", "delay", "decline", "bearish", "volatility");

        int score = 0;

        for (String headline : headlines) {
            String lowerHeadline = headline.toLowerCase();
            String[] words = lowerHeadline.replaceAll("[^a-zA-Z ]", "").split("\\s+");

            for (String word : words) {
                if (positiveWords.contains(word)) {
                    score++;
                } else if (negativeWords.contains(word)) {
                    score--;
                }
            }
        }
        return score;
    }

    // Display Results Method
    public static void displayResults(String ticker, int score) {
        System.out.println("\n--- Sentiment Analysis Report for " + ticker + " ---");
        System.out.println("Aggregate Score: " + score);

        String rating;
        if (score > 0) {
            rating = "BULLISH";
        } else if (score < 0) {
            rating = "BEARISH";
        } else {
            rating = "NEUTRAL";
        }
        System.out.println("Market Rating: " + rating);
    }
}