package com.manga.controller;


import com.manga.exception.BookNotFoundException;
import com.manga.exception.BookUnSupportedFieldPatchException;
import com.manga.model.Book;
import com.manga.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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

    //update author only
//    @PatchMapping("/books/{id}")
//    Book patch(@RequestBody Map<String, String> update, @PathVariable Long id) throws BookNotFoundException{
//        return bookRepository.findById(id)
//                .map(x -> {
//
//                    String author = update.get("author");
//                    if (!StringUtils.isEmpty(author)) {
//                        x.setAuthor(author);
//
//                        // better create a custom method to update a value = :newValue where id = :id
//                        return bookRepository.save(x);
//                    } else {
//                        throw new BookUnSupportedFieldPatchException(update.keySet());
//                    }
//
//                })
//                .orElseGet(() -> {
//                    throw new BookNotFoundException(id);
//                });
//
//    }
    @DeleteMapping("/books/{id}")
    void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }

}
