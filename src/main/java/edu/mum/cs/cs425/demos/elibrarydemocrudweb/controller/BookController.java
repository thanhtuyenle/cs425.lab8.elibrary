package edu.mum.cs.cs425.demos.elibrarydemocrudweb.controller;

import edu.mum.cs.cs425.demos.elibrarydemocrudweb.model.Book;
import edu.mum.cs.cs425.demos.elibrarydemocrudweb.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class BookController {

    //    @Autowired
    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = {"/elibrary/book/list"})
    public ModelAndView listBooks(@RequestParam(defaultValue = "0") int pageno) {
        ModelAndView modelAndView = new ModelAndView();
        Page<Book> books = bookService.getAllBooksPaged(pageno);
        modelAndView.addObject("books", books);
        modelAndView.addObject("booksCount", books.getSize());
        modelAndView.addObject("currentPageNo", pageno);
        modelAndView.setViewName("book/list");
        return modelAndView;
    }

    @GetMapping(value = {"/elibrary/book/new"})
    public String displayNewBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "book/new";
    }

    @PostMapping(value = {"/elibrary/book/new"})
    public String addNewBook(@Valid @ModelAttribute("book") Book book,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "book/new";
        }
        book = bookService.saveBook(book);
        return "redirect:/elibrary/book/list";
    }

    @GetMapping(value = {"/elibrary/book/edit/{bookId}"})
    public String editBook(@PathVariable Integer bookId, Model model) {
        Book book = bookService.getBookById(bookId);
        if (book != null) {
            model.addAttribute("book", book);
            return "book/edit";
        }
        return "book/list";
    }

    @PostMapping(value = {"/elibrary/book/edit"})
    public String updateBook(@Valid @ModelAttribute("book") Book book,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "book/edit";
        }
        book = bookService.saveBook(book);
        return "redirect:/elibrary/book/list";
    }

    @GetMapping(value = {"/elibrary/book/delete/{bookId}"})
    public String deleteBook(@PathVariable Integer bookId, Model model) {
        bookService.deleteBookById(bookId);
        return "redirect:/elibrary/book/list";
    }

    @RequestMapping(value = "/elibrary/book/search", method = RequestMethod.GET)
    public ModelAndView showBooksByTitle(@RequestParam String searchString, @RequestParam(defaultValue = "0") int pageno, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        Page<Book> books = bookService.searchBooks(searchString, pageno);
        modelAndView.addObject("books", books);
        modelAndView.addObject("searchString", searchString);
        modelAndView.addObject("booksCount", books.getContent().size());
        modelAndView.addObject("currentPageNo", pageno);
        modelAndView.setViewName("book/list");
        return modelAndView;
    }

}
