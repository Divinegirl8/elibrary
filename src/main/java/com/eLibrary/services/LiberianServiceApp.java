package com.eLibrary.services;

import com.eLibrary.data.model.Book;
import com.eLibrary.data.model.Liberian;
import com.eLibrary.data.repository.BookRepository;
import com.eLibrary.data.repository.LiberianRepository;
import com.eLibrary.dtos.request.LiberianRegisterRequest;
import com.eLibrary.dtos.request.SearchBookRequest;
import com.eLibrary.dtos.response.LiberianRegisterResponse;
import com.eLibrary.dtos.response.SearchBookResponse;
import com.eLibrary.exception.ElibraryException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class LiberianServiceApp implements LiberianService {
    private final LiberianRepository liberianRepository;
    private final BookRepository bookRepository;

    @Override
    public LiberianRegisterResponse register(LiberianRegisterRequest request) {
        Liberian liberian = new Liberian();
        liberian.setUsername(request.getUsername());
        liberianRepository.save(liberian);

        LiberianRegisterResponse liberianRegisterResponse = new LiberianRegisterResponse();
        liberianRegisterResponse.setId(liberian.getId());

        return liberianRegisterResponse;
    }

    @Override
    public SearchBookResponse searchBook(long id, SearchBookRequest request) throws ElibraryException, IOException {
        String title = validateBookTitle(request);
        String apiUrL = "https://gutendex.com/books?search=" + title;
        String jsonResponse = fetchBookInTheApi(apiUrL);


        return getSearchBookResponse(id, jsonResponse);

    }

    @Override
    public List<Book> getReadingList(long liberianId) throws ElibraryException {
        Liberian liberian = findBy(liberianId);
        Hibernate.initialize(liberian.getReadingList());
        return liberian.getReadingList();
    }

    private SearchBookResponse getSearchBookResponse(long id, String jsonResponse) throws JsonProcessingException, ElibraryException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonResponse);

        return getSearchBooks(id, root);
    }


    private SearchBookResponse getSearchBooks(long id, JsonNode root) throws ElibraryException {
        JsonNode results = root.get("results");
        SearchBookResponse searchBookResponse = new SearchBookResponse();

        for (JsonNode bookNode : results){
           String bookId = bookNode.get("id").asText();
           String bookTitle = bookNode.get("title").asText();
            List<String> authors = getAuthors(bookNode);

            List<String> subjects = getSubjects(bookNode);

            List<String> languages = getLanguages(bookNode);

            List<String> bookshelves = getBookShelves(bookNode);

            List<String> formats = getFormats(bookNode);

            Liberian liberian = findBy(id);
            Book book = getBook(bookId, bookTitle, authors, bookshelves, subjects,languages,formats, liberian);
            liberian.getReadingList().add(book);
            bookRepository.save(book);
            liberianRepository.save(liberian);


            List<Book> books = new ArrayList<>();
            books.add(book);
            searchBookResponse.setBooks(books);
        }
        return searchBookResponse;
    }

    private static List<String> getBookShelves(JsonNode bookNode) {
        List<String> bookshelves = new ArrayList<>();
        for (JsonNode bookShelvesNode : bookNode.get("bookshelves")){
            bookshelves.add(bookShelvesNode.asText());
        }
        Hibernate.initialize(bookshelves);
        return bookshelves;
    }

    private static List<String> getLanguages(JsonNode bookNode) {
        List<String> languages = new ArrayList<>();
        for (JsonNode languageNode : bookNode.get("languages")){
            languages.add(languageNode.asText());
        }
        Hibernate.initialize(languages);
        return languages;
    }

    private static List<String> getSubjects(JsonNode bookNode) {
        List<String> subjects = new ArrayList<>();
        for (JsonNode subjectNode : bookNode.get("subjects")){
            subjects.add(subjectNode.asText());
        }
        Hibernate.initialize(subjects);
        return subjects;
    }

    private static List<String> getAuthors(JsonNode bookNode) {
        List<String> authors = new ArrayList<>();
        for(JsonNode authorNode : bookNode.get("authors")){
            authors.add("Name: "+ authorNode.get("name").asText());
            authors.add("Birth Year: "+authorNode.get("birth_year").asText());
            authors.add("Death Year: " + authorNode.get("death_year").asText());
        }
        Hibernate.initialize(authors );
        return authors;
    }

    public static List<String> getFormats(JsonNode bookNode) {
        List<String> formats = new ArrayList<>();
        JsonNode formatsNode = bookNode.get("formats");
        if (formatsNode != null) {
            formatsNode.fields().forEachRemaining(entry -> {
                formats.add(entry.getValue().asText());
            });
        }
        return formats;
    }

    private static Book getBook(String bookId, String bookTitle, List<String> authors, List<String> bookshelves, List<String> subjects, List<String> languages,List<String> formats, Liberian liberian) {
        Book book = new Book();
        book.setBookId(bookId);
        book.setTitle(bookTitle);
        book.setAuthor(authors);
        book.setBookshelves(bookshelves);
        book.setSubjects(subjects);
        book.setFormats(formats);
        book.setLanguages(languages);
        book.getLiberian().add(liberian);
        liberian.getReadingList().add(book);
        return book;
    }

    private String validateBookTitle(SearchBookRequest request){
        String title = request.getTitle();

        if (title.contains(" ")){
            title = title.replace(" ","%20");
        }
        return title;
    }

    private String fetchBookInTheApi(String title) throws IOException, ElibraryException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(title);

        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
            throw new ElibraryException("Network failure");
        }

        String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
        httpClient.close();

        return jsonResponse;
    }



    private Liberian findBy(Long id) throws ElibraryException {

        return  liberianRepository.findById(id).orElseThrow(()->new ElibraryException(String.format("liberian with id %d not found",id)));
    }
}
