package net.stawrul.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

/**
 * Klasa encyjna reprezentująca towar w sklepie (książkę).
 */
@Entity
@EqualsAndHashCode(of = "id")
@NamedQueries(value = {
        @NamedQuery(name = Book.FIND_ALL, query = "SELECT b FROM Book b")
})
public class Book {
    public static final String FIND_ALL = "Book.FIND_ALL";

    @Getter
    @Id
    UUID id = UUID.randomUUID();

    @Getter
    @Setter
    String author;

    @Getter
    @Setter
    Integer amount;
    
    @Getter
    @Setter
    Double prize;
    
    @Getter
    @Setter
    String wydawca;
    
    @Getter
    @Setter
    Integer iloscStron;
}
