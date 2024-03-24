package com.eLibrary.dtos.response;

import com.eLibrary.data.model.Book;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class SearchBookResponse {
    private List<Book> books;
}
