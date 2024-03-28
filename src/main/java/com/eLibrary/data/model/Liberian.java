package com.eLibrary.data.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Setter
@Getter
public class Liberian {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String username;
    private String password;
    private boolean isLogin;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "liberian_reading_list",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "liberian_id")
            }
    )
    private List<Book> readingList = new ArrayList<>();

    @Override
    public String toString() {
        return "Liberian{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", readingList=" + readingList +
                '}';
    }
}
