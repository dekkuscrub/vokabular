package com.practice.vokabular.repository;

import com.practice.vokabular.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {
    // Find a word by its term
    Optional<Word> findByTerm(String term);

    // Find all words of a specific type
    List<Word> findByWordType(String wordType);

    // Check if a word exists by term
    boolean existsByTerm(String term);
}
