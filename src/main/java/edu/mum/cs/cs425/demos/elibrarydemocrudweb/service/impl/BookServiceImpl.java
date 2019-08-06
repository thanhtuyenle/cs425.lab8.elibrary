package edu.mum.cs.cs425.demos.elibrarydemocrudweb.service.impl;

import edu.mum.cs.cs425.demos.elibrarydemocrudweb.model.Book;
import edu.mum.cs.cs425.demos.elibrarydemocrudweb.repository.BookRepository;
import edu.mum.cs.cs425.demos.elibrarydemocrudweb.service.BookService;
import org.apache.el.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.logging.Filter;
import java.util.stream.Collectors;

//@Service(value = "MainBookService")
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository repository;

    @Override
    public Iterable<Book> getAllBooks() {
//        return ((List<Book>)repository.findAll())
//                .stream()
//                .sorted(Comparator.comparing(Book::getTitle))
//                .collect(Collectors.toList());
        return repository.findAll(Sort.by("title"));
    }

    @Override
    public Page<Book> getAllBooksPaged(int pageNo) {
        return repository.findAll(PageRequest.of(pageNo, 3, Sort.by("title")));
    }

    @Override
    public Book saveBook(Book book) {
        return repository.save(book);
    }

    @Override
    public Book getBookById(Integer bookId) {
        return repository.findById(bookId).orElse(null);
    }

    @Override
    public void deleteBookById(Integer bookId) {
        repository.deleteById(bookId);
    }

    @Override
    public Optional<Book> findByISBN(String isbn) {
        return repository.findBookByIsbn(isbn);
    }

//    @Override
//    public Page<Book> listBooksByTitle(String title, int pageNo) {
//        if (title != null && title.isEmpty() == false) {
//            return repository.findByTitle(title, PageRequest.of(pageNo, 3, Sort.by("title")));
//        } else {
//            return getAllBooksPaged(pageNo);
//        }
//    }
    @Override
    public Page<Book> searchBooks(String searchString, int pageNo) {
        if(containsDecimalPoint(searchString) && isMoney(searchString)) {
            return repository.findAllByOverdueFeeEquals(Double.parseDouble(searchString), PageRequest.of(pageNo, 3, Sort.by("title")));

        } else if(isDate(searchString)) {
            return repository.findAllByDatePublishedEquals(LocalDate.parse(searchString, DateTimeFormatter.ISO_DATE), PageRequest.of(pageNo, 3, Sort.by("title")));
        } else {
            return repository.findAllByIsbnContainingOrTitleContainingOrPublisherContainingOrderByTitle(searchString, searchString, searchString, PageRequest.of(pageNo, 3, Sort.by("title")));
        }
    }

    private boolean isMoney(String searchString) {
        boolean isParseableAsMoney = false;
        try {
            Double.parseDouble(searchString);
            isParseableAsMoney = true;
        } catch(Exception ex) {
            if(ex instanceof ParseException) {
                isParseableAsMoney = false;


            }
        }
        return isParseableAsMoney;
    }

    private boolean isDate(String searchString) {
        boolean isParseableAsDate = false;
        try {
            LocalDate.parse(searchString, DateTimeFormatter.ISO_DATE);
            isParseableAsDate = true;
        } catch(Exception ex) {
            if(ex instanceof DateTimeParseException) {
                isParseableAsDate = false;
            }
        }
        return isParseableAsDate;
    }

    private boolean containsDecimalPoint(String searchString) {
        return searchString.contains(".");
    }

}
