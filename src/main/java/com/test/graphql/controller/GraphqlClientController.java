package com.test.graphql.controller;


import com.test.graphql.dao.BookDetails;
import com.test.graphql.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class GraphqlClientController {

    @Autowired
    private HttpGraphQlClient httpGraphQlClient;

    @GetMapping("/getbooks")
    public List<Book> getAllBooks(){
      String graphqlQuery = String.format("query GetAllBooks {\n" +
              "    getAllBooks {\n" +
              "        id\n" +
              "        title\n" +
              "        author\n" +
              "    }\n" +
              "}");
        Mono<List<Book>> mono = httpGraphQlClient.document(graphqlQuery).retrieve("getAllBooks").toEntityList(Book.class);
        return mono.block();
    }

    @GetMapping("/getbook/{bookId}")
    public Book getBook(@PathVariable String bookId){
        String graphqlQuery = String.format("query GetBookById {\n" +
                "    getBookById(id: \"%s\") {\n" +
                "        id\n" +
                "        title\n" +
                "        author\n" +
                "    }\n" +
                "}", bookId);
        Mono<Book> mono = httpGraphQlClient.document(graphqlQuery).retrieve("getBookById").toEntity(Book.class);
        return mono.block();
    }

    @PostMapping("/createbook")
    public BookDetails createBook(@RequestBody Book book){
        String graphqlQuery = String.format("mutation CreateBook {\n" +
                "    createBook(bookDetails: { title: \"%s\", author: \"%s\" }) {\n" +
                "        id\n" +
                "        title\n" +
                "        author\n" +
                "    }\n" +
                "}", book.getTitle(), book.getAuthor());
        Mono<BookDetails> mono = httpGraphQlClient.document(graphqlQuery).retrieve("createBook").toEntity(BookDetails.class);
        return mono.block();
    }


    @PutMapping("/updatebook")
    public BookDetails updateBook( @RequestBody Book book){
        String graphqlQuery = String.format("mutation UpdateBook {\n" +
                "    updateBook(id: \"%s\", bookDetails: { title: \"%s\", author: \"%s\" }) {\n" +
                "        id\n" +
                "        title\n" +
                "        author\n" +
                "    }\n" +
                "}", book.getId(), book.getTitle(), book.getAuthor());
        Mono<BookDetails> mono = httpGraphQlClient.document(graphqlQuery).retrieve("updateBook").toEntity(BookDetails.class);
        return mono.block();
    }

    @DeleteMapping("/deletebook/{bookId}")
    public String deleteBook(@PathVariable String bookId){
        String graphqlQuery = String.format("mutation DeleteBook {\n" +
                "    deleteBook(id: %s)\n" +
                "}", bookId);
        Mono<String> mono = httpGraphQlClient.document(graphqlQuery).retrieve("deleteBook").toEntity(String.class);
        return mono.block();
    }
}
