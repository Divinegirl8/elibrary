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
@ToString
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
    private List<String> subjects = new ArrayList<>();
    @ElementCollection
    private List<String> bookshelves = new ArrayList<>();
    @ElementCollection
    private List<String> languages = new ArrayList<>();
    @ManyToMany(mappedBy = "readingList")
    private List<Liberian> liberian = new ArrayList<>();


}
