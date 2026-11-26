import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    // Paste your free Finnhub API key here
    private static final String API_KEY = "dc3qjrhr01qk29kd4e00dc3qjrhr01qk29kd4e0g";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // User Input
        System.out.print("Enter stock ticker (e.g., AAPL, TSLA): ");
        String ticker = scanner.nextLine().trim().toUpperCase();

        if (ticker.isEmpty()) {
            System.out.println("Error: Ticker cannot be empty.");
            scanner.close();
            return;
        }

        System.out.println("Fetching live news headlines for " + ticker + "...");

        // Build date range (last 7 days) for Finnhub company-news endpoint
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(7);
        String url = String.format(
            "https://finnhub.io/api/v1/company-news?symbol=%s&from=%s&to=%s&token=%s",
            ticker, weekAgo, today, API_KEY
        );

        List<String> headlines = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                headlines = parseHeadlines(response.body());
                
                if (headlines.isEmpty()) {
                    System.out.println("No recent news headlines found for " + ticker + ".");
                    scanner.close();
                    return;
                }

                System.out.println("Analyzed " + headlines.size() + " recent headlines.\n");
                int score = calculateSentiment(headlines);
                displayResults(ticker, score);
            } else {
                System.out.println("API Error: Received HTTP status " + response.statusCode() + ". Check your API key or ticker.");
            }

        } catch (Exception e) {
            System.out.println("Network error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    // Extract "headline":"..." values from Finnhub JSON without external libraries
    public static List<String> parseHeadlines(String jsonBody) {
        List<String> extracted = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"headline\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(jsonBody);

        while (matcher.find()) {
            // Unescape any escaped quotes inside the headline
            String cleanHeadline = matcher.group(1).replace("\\\"", "\"");
            extracted.add(cleanHeadline);
        }
        return extracted;
    }

    // Expanded Sentiment Scoring Engine
    public static int calculateSentiment(List<String> headlines) {
        List<String> positiveWords = List.of(
            "surge", "growth", "record", "profit", "bullish", "gain",
            "beat", "rally", "upgrade", "soar", "dividend", "strong"
        );
        List<String> negativeWords = List.of(
            "fall", "loss", "fear", "delay", "decline", "bearish", "volatility",
            "miss", "slump", "downgrade", "lawsuit", "drop", "debt", "weak"
        );

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
        System.out.println("--- Sentiment Analysis Report for " + ticker + " ---");
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