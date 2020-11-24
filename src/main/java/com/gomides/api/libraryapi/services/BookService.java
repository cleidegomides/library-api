package com.gomides.api.libraryapi.services;

import com.gomides.api.libraryapi.model.entities.BookStory;


public interface BookService {
    BookStory save(BookStory bookStory);
}
