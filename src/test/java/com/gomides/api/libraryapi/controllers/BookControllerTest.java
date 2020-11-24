package com.gomides.api.libraryapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gomides.api.libraryapi.dto.BookDto;
import com.gomides.api.libraryapi.exceptions.BusinessException;
import com.gomides.api.libraryapi.model.entities.BookStory;
import com.gomides.api.libraryapi.services.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

    static String BOOK_API = "/book";

    @Autowired
    MockMvc mvc;

    @MockBean
    BookService service;

    @Test
    @DisplayName("Deve criar um livro com sucesso")
    public void createBookTeste() throws Exception {

        BookDto dto = creatNewBook();
        BookStory savedBook = BookStory.builder().id(10l).autor("Artur").title("As Aventuras").isbn("001").build();
        BDDMockito.given(service.save(Mockito.any(BookStory.class))).willReturn(savedBook);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(10l))
                .andExpect(jsonPath("title").value(dto.getTitle()))
                .andExpect(jsonPath("autor").value(dto.getAutor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()))
        ;


    }

    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficientes para criação do livro")
    public void createInvalidBookTeste() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new BookDto());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect( status().isBadRequest() )
                .andExpect(jsonPath("errors", hasSize(3)));
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar cadastrar um livro com isbne já utilizado por outro")
    public void createBookWithDuplicatedIsbn() throws Exception {

        BookDto dto = creatNewBook();
        String json = new ObjectMapper().writeValueAsString(dto);
        String mensagemErro = "Isbn já cadastrado";
        BDDMockito.given(service.save(Mockito.any(BookStory.class)))
                .willThrow(new BusinessException(mensagemErro));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(mensagemErro));
    }

    private BookDto creatNewBook() {
        return BookDto.builder().autor("Artur").title("As Aventuras").isbn("001").build();
    }
}
