package com.manga.exception;

public class BookNotFoundException extends Exception {
    public BookNotFoundException(Long id) {
        super("This book id not found: "+id);
    }

}
