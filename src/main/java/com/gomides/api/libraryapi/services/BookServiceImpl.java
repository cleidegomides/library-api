package com.gomides.api.libraryapi.services;

import com.gomides.api.libraryapi.exceptions.BusinessException;
import com.gomides.api.libraryapi.model.entities.BookStory;
import com.gomides.api.libraryapi.repositores.BookStoryRepository;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {


    private BookStoryRepository bookStoryRepository;

    public BookServiceImpl(BookStoryRepository bookStoryRepository) {
        this.bookStoryRepository = bookStoryRepository;
    }

    @Override
    public BookStory save(BookStory bookStory) {
        if (bookStoryRepository.existsByIsbn(bookStory.getIsbn())){
            throw new BusinessException("Isbn já cadastrado");
        }
        return bookStoryRepository.save(bookStory);
    }
}
