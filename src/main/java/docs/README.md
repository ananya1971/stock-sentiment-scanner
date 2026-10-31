# Stock Sentiment Analysis & News Keyword Scanner

## Project Goal
A Java command-line tool designed to fetch recent financial news headlines via a public API, analyze article text against positive and negative keyword dictionaries, and output a clean aggregate market sentiment score (Bullish, Neutral, or Bearish) to the terminal.

## Tech Stack
Language: Java
Libraries:
- java.net.http.HttpClient (for GET requests)
- org.json (parsing JSON response strings)
- VS Code
Data Storage
- In-memory runtime storage (no database or permanent file storage) - ArrayList for the MVP
- Finnhub API / NewsAPI.org / YFinance


## Chunked Development Plan (Git Commit Roadmap)
Commit 1: Initialize Java project and set up GitHub repository
Commit 2: Implement user input prompt and ticker validation (Scanner)
Commit 3: Build the API networking client (HttpClient)
Commit 4: Integrate JSON parsing for news headlines (org.json)
Commit 5: Develop the keyword sentiment scoring engine and dictionary loops
Commit 6: FOrmat final terminal output, add summary ratings and finalize MVP
