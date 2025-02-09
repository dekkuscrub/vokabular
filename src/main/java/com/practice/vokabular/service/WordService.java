package com.practice.vokabular.service;

import com.practice.vokabular.model.Word;
import com.practice.vokabular.repository.WordRepository;
import com.practice.vokabular.service.svenska.SvenskaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WordService {
    
    private final WordRepository wordRepository;
    private final SvenskaService svenskaService;

    public WordService(WordRepository wordRepository, SvenskaService svenskaService) {
        this.wordRepository = wordRepository;
        this.svenskaService = svenskaService;
    }

    public Word saveWord(String term) {
        // Check if word already exists
        Optional<Word> existingWord = wordRepository.findByTerm(term);
        if (existingWord.isPresent()) {
            return existingWord.get();
        }

        // Fetch word details from svenska.se
        Word wordDetails = svenskaService.fetchWordDetails(term);
        return wordRepository.save(wordDetails);
    }

    public Optional<Word> findById(Long id) {
        return wordRepository.findById(id);
    }

    public Optional<Word> findByTerm(String term) {
        return wordRepository.findByTerm(term);
    }

    public List<Word> findAllWords() {
        return wordRepository.findAll();
    }

    public List<Word> findByWordType(String wordType) {
        return wordRepository.findByWordType(wordType);
    }

    public void deleteWord(Long id) {
        wordRepository.deleteById(id);
    }

    public boolean existsByTerm(String term) {
        return wordRepository.existsByTerm(term);
    }
} 
