package com.manga.controller;


import com.manga.exception.BookNotFoundException;
import com.manga.model.Book;
import com.manga.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    //find all
    @GetMapping(value = "/books")
    List<Book> findAll() {
        return bookRepository.findAll();
    }

    //save
    @PostMapping(value = "/books")
    @ResponseStatus(HttpStatus.CREATED)
    Book newBook(@Valid @RequestBody Book book) {
        return bookRepository.save(book);
    }

    //find by id
    @GetMapping(value = "/books/{id}")
    Book findOne(@PathVariable Long id) throws BookNotFoundException {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }


    //save or update
    @PutMapping(value = "/books/{id}")
    Book saveOrUpdate(@RequestBody Book newBook, @PathVariable Long id) {
        return bookRepository.findById(id)
                .map(x -> {
                    x.setName(newBook.getName());
                    x.setAuthor(newBook.getAuthor());
                    x.setPrice(newBook.getPrice());
                    return bookRepository.save(x);
                })
                .orElseGet(() -> {
                    newBook.setId(id);
                    return bookRepository.save(newBook);
                });
    }

}
