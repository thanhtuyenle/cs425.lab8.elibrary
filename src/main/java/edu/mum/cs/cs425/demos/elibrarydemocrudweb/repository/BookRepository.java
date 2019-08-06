package edu.mum.cs.cs425.demos.elibrarydemocrudweb.repository;

import edu.mum.cs.cs425.demos.elibrarydemocrudweb.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    // This interface definition relies on the public abstract methods
    // inherited from the super interface, CrudRepository<T, ID>
    // We may override any or add more methods here, if needed.
    Optional<Book> findBookByIsbn(String isbn);

    @Query("Select b from Book b where b.title like %:title%")
    Page<Book> findByTitle(String title, Pageable pageable);
    //List<Book> findBooksByTitleContainsOrIsbnContainsOrOverdueFeeContainsOrPublisherContainsOrDatePublishedContainsOrderByTitle(String searchString);
    Page<Book> findAllByIsbnContainingOrTitleContainingOrPublisherContainingOrderByTitle(String isbn,
                                                                                         String title,
                                                                                         String pub, Pageable pageable);
    Page<Book> findAllByOverdueFeeEquals(Double overdueFee, Pageable pageable);
    Page<Book> findAllByDatePublishedEquals(LocalDate datePublished, Pageable pageable);
}
