INSERT INTO words (term, definition, word_type, example_sentence, saved_at) 
VALUES 
    ('hej', 'ett vardagligt hälsningsord', 'interjektion', 'Hej, hur mår du?', CURRENT_TIMESTAMP),
    ('springa', 'förflytta sig snabbt till fots med steg där båda fötterna temporärt lämnar marken', 'verb', 'Han springer i parken varje morgon.', CURRENT_TIMESTAMP),
    ('bok', 'tryckt skrift med pärmar, bestående av hopfogade blad', 'substantiv', 'Jag läser en intressant bok.', CURRENT_TIMESTAMP),
    ('vacker', 'som ger ett behagligt intryck genom sitt utseende; skön', 'adjektiv', 'Det är en vacker dag idag.', CURRENT_TIMESTAMP),
    ('snabbt', 'som sker med hög hastighet; fort, hastigt', 'adverb', 'Hon springer mycket snabbt.', CURRENT_TIMESTAMP)
ON CONFLICT (term) DO NOTHING; 
