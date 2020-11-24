package com.gomides.api.libraryapi.repositores;

import com.gomides.api.libraryapi.model.entities.BookStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookStoryRepository extends JpaRepository<BookStory, Long> {
    boolean existsByIsbn(String isbn);
}
