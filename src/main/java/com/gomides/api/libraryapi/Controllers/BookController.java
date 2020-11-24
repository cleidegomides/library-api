package com.gomides.api.libraryapi.Controllers;

import com.gomides.api.libraryapi.dto.BookDto;
import com.gomides.api.libraryapi.exceptions.ApiErrors;
import com.gomides.api.libraryapi.exceptions.BusinessException;
import com.gomides.api.libraryapi.model.entities.BookStory;
import com.gomides.api.libraryapi.services.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService service;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(  @RequestBody @Valid BookDto bookDto){
        BookStory entity = modelMapper.map(bookDto, BookStory.class);
        entity = service.save(entity);
        return modelMapper.map(entity, BookDto.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException exception){
        BindingResult bindingResult = exception.getBindingResult();
        return new ApiErrors(bindingResult);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessExceptions(BusinessException exception){

        return new ApiErrors(exception);
    }

}
