package com.eLibrary.data.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String bookId;
    private String title;
    @ElementCollection
    @Fetch(FetchMode.JOIN)
    private List<String> author = new ArrayList<>();
    @ElementCollection
    @Fetch(FetchMode.JOIN)
    private List<String> formats = new ArrayList<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_subjects",joinColumns = @JoinColumn(name = "book_id"))
    private List<String> subjects = new ArrayList<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_bookshelves",joinColumns = @JoinColumn(name = "book_id"))
    private List<String> bookshelves = new ArrayList<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_languages",joinColumns = @JoinColumn(name = "book_id"))
    private List<String> languages = new ArrayList<>();
//    @ManyToMany(mappedBy = "readingList", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//    private List<Liberian> liberian = new ArrayList<>();


    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookId='" + bookId + '\'' +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", formats=" + formats +
                ", subjects=" + subjects +
                ", bookshelves=" + bookshelves +
                ", languages=" + languages +
                '}';
    }

}
