package com.eLibrary.dtos.response;

import com.eLibrary.data.model.Book;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SearchBookResponse {
    private List<Book> books;
}
