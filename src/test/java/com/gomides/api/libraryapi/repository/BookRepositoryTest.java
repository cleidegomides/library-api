package com.gomides.api.libraryapi.repository;

import com.gomides.api.libraryapi.model.entities.BookStory;
import com.gomides.api.libraryapi.repositores.BookStoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;   //É utilizado para criar um cenário, simulando um EntityManager

    @Autowired
    BookStoryRepository bookStoryRepository;

    @Test
    @DisplayName("Dever retornar verdadeiro quando existir um livro na base com o isbn informado.")
    public void returnTrueWhenIsbnExists(){
        //cenario
        String isbn = "123";
        BookStory bookStory = BookStory.builder().title("Aventuras").autor("Fulando").isbn(isbn).build();
        entityManager.persist(bookStory);

        //execução
        boolean exist = bookStoryRepository.existsByIsbn(isbn);

        //verificação
        assertThat(exist).isTrue();
    }

    @Test
    @DisplayName("Dever retornar falso quando não existir um livro na base com o isbn informado.")
    public void returnFalseWhenIsbnDoesntExists(){
        //cenario
        String isbn = "123";

        //execução
        boolean exist = bookStoryRepository.existsByIsbn(isbn);

        //verificação
        assertThat(exist).isFalse();
    }
}
