package com.gomides.api.libraryapi.services;

import com.gomides.api.libraryapi.exceptions.BusinessException;
import com.gomides.api.libraryapi.model.entities.BookStory;
import com.gomides.api.libraryapi.repositores.BookStoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookStoryServiceTest {



    BookService service;

    @MockBean
    BookStoryRepository bookStoryRepository;

    @BeforeEach
    public void setUp(){
        this.service = new BookServiceImpl(bookStoryRepository);
    }

    @Test
    @DisplayName("Dever salvar um livro")
    public void saveBookTest(){
        //Cenário
        BookStory bookStory = createValidBook();
        Mockito.when(bookStoryRepository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(bookStoryRepository.save(bookStory)).thenReturn(
                        BookStory.builder()
                            .id(1l)
                            .isbn("123456")
                            .title("As Aventuras")
                            .autor("Fulano")
                            .build()
                 );

        //Ação
        BookStory savedBook = service.save(bookStory);

        //Verificação
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123456");
        assertThat(savedBook.getAutor()).isEqualTo("Fulano");
        assertThat(savedBook.getTitle()).isEqualTo("As Aventuras");
    }

    private BookStory createValidBook() {
        return BookStory.builder().isbn("123456").autor("Fulano").title("As Aventuras").build();
    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar salvar com o isbn dupicado")
    public void shouldNotABookWithDuplicatedISBN(){
        //Cenário
        BookStory bookStory = createValidBook();
        Mockito.when(bookStoryRepository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        // Ação
        Throwable exception = Assertions.catchThrowable(() -> service.save(bookStory) );

        //Verificação
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado");

        Mockito.verify(bookStoryRepository, Mockito.never()).save(bookStory);
    }
}
