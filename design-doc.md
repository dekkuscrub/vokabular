# Swedish Vocabulary Practice Application

## System Overview
A vocabulary learning application built with Spring Boot and HTMX. Users can search for Swedish 
words, save their definitions from svenska.se, and practice using a simple flashcard system. 
The application stores saved words locally and uses HTMX for smooth, JavaScript-free interactions.

## Technical Stack
- Backend:
  * Java 21
  * Spring Boot 3.x
  * Spring Data JPA
  * PostgreSQL for persistent storage
  * Integration with svenska.se/saol API

- Frontend:
  * HTMX for dynamic interactions
  * Simple CSS for styling (utilizing CSS Grid and Flexbox)
  * Minimal HTML templates
  * No JavaScript required

## Core Features

### 1. Word Search and Definition
- Single input field with HTMX trigger for live search
- Search results update dynamically without page reload
- Definition displayed in-place when word is found
- Save button appears with definition
- System checks local database before calling external API

### 2. Vocabulary Management
- Dynamic list of saved words
- Instant deletion with HTMX triggers
- Optional word type filtering through HTMX requests
- Smooth UI updates when adding/removing words

### 3. Practice Module
- Simple flashcard interface with HTMX-powered card flipping
- Server-side card state management
- Next/Previous navigation without page reloads
- Option to shuffle cards

## Data Models

### Word
```java
class Word {
    Long id
    String term           
    String definition     
    String wordType       // Optional
    String exampleSentence // Optional
    String userNotes      // Optional
    LocalDateTime savedAt
}
```

## API Endpoints & Data Flow

### 1. Word Search
```
GET /api/search
Query params: q (search term)
Returns: HTML fragment containing search results
```
- Triggers on input field changes via HTMX
- Returns a fragment showing matching word(s) with definition
- Shows save button if word isn't already saved

### 2. Word Save
```
POST /api/words
Body: term (string)
Returns: HTML fragment of updated word list
```
- Fetches word details from svenska.se
- Saves word to database
- Returns updated list of saved words

### 3. Word Delete
```
DELETE /api/words/{id}
Returns: HTML fragment of updated word list
```
- Removes word from database
- Returns updated list of saved words

### 4. Word List
```
GET /api/words
Query params: type (optional - for filtering by word type)
Returns: HTML fragment of word list
```
- Returns list of all saved words
- Supports optional filtering by word type

### 5. Flashcard Operations
```
GET /api/flashcards/current
Returns: HTML fragment of current flashcard

POST /api/flashcards/flip
Returns: HTML fragment of flipped card state

POST /api/flashcards/next
Returns: HTML fragment of next card

POST /api/flashcards/previous
Returns: HTML fragment of previous card

POST /api/flashcards/shuffle
Returns: HTML fragment of first card in shuffled deck
```

## Database Schema

```sql
CREATE TABLE words (
    id BIGSERIAL PRIMARY KEY,
    term VARCHAR(255) NOT NULL,
    definition TEXT NOT NULL,
    word_type VARCHAR(50),
    example_sentence TEXT,
    user_notes TEXT,
    saved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_term UNIQUE (term)
);

-- Indexes
CREATE INDEX idx_words_term ON words(term);
CREATE INDEX idx_words_word_type ON words(word_type);
```

## Svenska.se Integration

### API Details
- Base URL: https://svenska.se/tri/f_saol.php
- Query Parameter: sok (search term)
- Response Format: HTML

### HTML Processing
The application needs to transform the svenska.se HTML response into our own HTML fragments. Key elements to process:

1. Main word information:
```html
<span class="grundform">dup·­era</span>        <!-- Base word form -->
<a class="ordklass">verb</a>                   <!-- Word type -->
<span class="def">vilse­leda, lura</span>      <!-- Definition -->
```

2. Conjugation table (if present):
```html
<table class="tabell" id="bojning432937">      <!-- Contains all word forms -->
    <!-- Transform relevant rows into a more concise format for display -->
</table>
```

### HTML Transformation Strategy
Instead of parsing to JSON, we'll:
1. Extract the relevant sections from svenska.se HTML
2. Transform them into our own HTML fragments that match our application's styling
3. Keep only the needed information for display

Example transformed output for search results:
```html
<div class="word-result">
    <h3 class="word-term">dupera</h3>
    <div class="word-type">verb</div>
    <div class="word-definition">vilseleda, lura</div>
    <button class="save-word" hx-post="/api/words" 
            hx-vals='{"term": "dupera"}'>
        Save Word
    </button>
</div>
```


## HTML Templates & Structure

### Main Layout
```html
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Swedish Vocabulary Learning</title>
    <link rel="stylesheet" href="/css/styles.css">
    <script src="https://unpkg.com/htmx.org@1.9.10"></script>
</head>
<body>
    <div class="container">
        <nav class="main-nav">
            <a href="/" class="nav-item">Search</a>
            <a href="/words" class="nav-item">My Words</a>
            <a href="/practice" class="nav-item">Practice</a>
        </nav>
        
        <main class="content">
            <!-- Content will be inserted here -->
        </main>
    </div>
</body>
</html>
```

### Search View
```html
<div class="search-container">
    <input type="text" 
           name="search" 
           placeholder="Search for a word..."
           hx-get="/api/search"
           hx-trigger="keyup changed delay:500ms"
           hx-target="#search-results"
           class="search-input">
    
    <div id="search-results" class="search-results">
        <!-- Search results will be inserted here -->
    </div>
</div>
```

### Search Results Fragment
```html
<div class="word-result">
    <h3 class="word-term">{term}</h3>
    <div class="word-type">{type}</div>
    <div class="word-definition">{definition}</div>
    <!-- Only show if word isn't saved -->
    <button class="save-word" 
            hx-post="/api/words"
            hx-vals='{"term": "{term}"}'
            hx-target="#word-list">
        Save Word
    </button>
</div>
```

### Word List Fragment
```html
<div class="word-list">
    <!-- For each word -->
    <div class="word-item">
        <h3>{term}</h3>
        <div class="word-details">
            <span class="word-type">{type}</span>
            <p class="definition">{definition}</p>
        </div>
        <button class="delete-word"
                hx-delete="/api/words/{id}"
                hx-target="#word-list">
            Delete
        </button>
    </div>
</div>
```

### Flashcard Fragment
```html
<div class="flashcard-container">
    <div class="flashcard" 
         hx-post="/api/flashcards/flip"
         hx-trigger="click"
         hx-target="this">
        <!-- Front or back content based on state -->
        <div class="card-content">
            {content}
        </div>
    </div>
    
    <div class="card-controls">
        <button hx-get="/api/flashcards/previous"
                hx-target=".flashcard-container">Previous</button>
        <button hx-get="/api/flashcards/next"
                hx-target=".flashcard-container">Next</button>
        <button hx-post="/api/flashcards/shuffle"
                hx-target=".flashcard-container">Shuffle</button>
    </div>
</div>
```

## Error Handling & User Feedback

### Error States HTML Fragment
```html
<div class="error-message" role="alert">
    <p class="error-text">{error message}</p>
    <button class="retry-button" 
            hx-{original-request-type}="{original-url}"
            hx-target="{original-target}">
        Try Again
    </button>
</div>
```

### Error Scenarios
1. Search Errors
   - Word not found: Show "No matches found for '{term}'"
   - Svenska.se API error: Show "Unable to search right now. Please try again"

2. Save/Delete Errors
   - Save failure: Show "Unable to save word. Please try again"
   - Delete failure: Show "Unable to delete word. Please try again"
   - Duplicate word: Show "This word is already saved"

3. Flashcard Errors
   - No cards available: Show "No words saved yet. Add some words to practice!"
   - Navigation error: Show "Unable to load next card. Please try again"

## HTMX Configuration

### Global Settings
```html
<body hx-boost="true">
```

### Common Attributes Usage
1. Search Input:
   ```html
   hx-trigger="keyup changed delay:500ms"
   ```
   - Delays search for 500ms after typing stops
   - Only triggers if value has changed

2. Save/Delete Operations:
   ```html
   hx-swap="outerHTML transition:true"
   ```
   - Replaces entire target element
   - Enables smooth transitions

3. Flashcard Flipping:
   ```html
   hx-swap="innerHTML swap:500ms"
   ```
   - Allows time for flip animation

### Loading States
All dynamic elements should include:
```html
<div class="htmx-indicator">Loading...</div>
```

## Out of Scope for POC

1. User Management
   - No user accounts
   - No authentication
   - No user-specific data

2. Advanced Features
   - No spaced repetition
   - No progress tracking
   - No export/import functionality
   - No word categories or tags
   - No example sentence management
   - No pronunciation features

3. Performance Optimizations
   - No caching layer
   - No rate limiting
   - No pagination for word lists

4. Additional Integrations
   - No additional dictionary APIs
   - No translation services
   - No image associations

5. Advanced Practice Features
   - No scoring system
   - No practice history
   - No difficulty levels
   - No custom practice modes

6. Mobile-Specific Features
   - No offline support
   - No native app features
   - Basic responsive design only
