package com.test.graphql.controller;

import com.test.graphql.dao.BookDetails;
import com.test.graphql.entity.Book;
import com.test.graphql.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

//@RestController
//@RequestMapping("/api/books")
@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // Get all books
//    @GetMapping
    @QueryMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get book by ID
    //@GetMapping("/{id}")
   @QueryMapping
    public Book getBookById(@Argument Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return book.get();
        } else {
            return null;
        }
    }

    // Create a new book
    @MutationMapping
    public Book createBook(@Argument BookDetails bookDetails) {
        if (bookDetails != null) {
            Book book = new Book();
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            Book updatedBook = bookRepository.save(book);
            return updatedBook;
        } else {
            return null;
        }

    }

    // Update a book
    @MutationMapping
    public Book updateBook(@Argument Long id, @Argument BookDetails bookDetails) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            Book updatedBook = bookRepository.save(book);
            return updatedBook;
        } else {
            return null;
        }
    }

    // Delete a book
    @MutationMapping
    public  String deleteBook(@Argument Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            Book bookFetched = book.get();
            bookRepository.delete(bookFetched);
            return "Deleted book ID: "+id+" successfully";
        } else {
            return null;
        }
    }

}
