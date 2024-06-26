package com.eLibrary.services;

import com.eLibrary.data.model.Book;
import com.eLibrary.data.model.Liberian;
import com.eLibrary.data.repository.BookRepository;
import com.eLibrary.data.repository.LiberianRepository;
import com.eLibrary.dtos.request.*;
import com.eLibrary.dtos.response.*;
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
    public LiberianRegisterResponse register(LiberianRegisterRequest request) throws ElibraryException {
        validateUserNameFieldIsNotEmpty(request);
        validateUserName(request);
        validatePassword(request);
      checkIfUsernameExist(request);
        Liberian liberian = new Liberian();

        liberian.setUsername(request.getUsername().trim());
        liberian.setPassword(request.getPassword().trim());
        liberianRepository.save(liberian);

        LiberianRegisterResponse liberianRegisterResponse = new LiberianRegisterResponse();
        liberianRegisterResponse.setId(liberian.getId());

        return liberianRegisterResponse;
    }

    @Override
    public LoginResponse login(LoginRequest request) throws ElibraryException {
        Liberian liberian = liberianRepository.findLiberianByUsername(request.getUsername().trim());

        if (request.getUsername().isEmpty()){
            throw new ElibraryException("{\"error\": \"username field cannot be empty\"}");
        }

        if (request.getPassword().isEmpty()){
            throw new ElibraryException("{\"perror\": \"password field cannot be empty\"}");
        }


        if (!userExist(request.getUsername().trim())){throw  new ElibraryException("{\"error\"  : \"login credentials is not valid!!!\"}");}
        if (!(liberian.getPassword().equals(request.getPassword().trim()))){throw new ElibraryException("{\"perror\" : \"login credentials is not valid\"}");}

        liberianRepository.save(liberian);

        LoginResponse response = new LoginResponse();
        response.setId(liberian.getId());

        return response;
    }

    @Override
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) throws ElibraryException {
        Liberian liberian = liberianRepository.findLiberianByUsername(request.getUsername());

        if (request.getUsername().isEmpty()){
            throw new ElibraryException("{\"error\": \"username field cannot be empty\"}");
        }

       validateForgotPassword(request);

        if (liberian == null){
            throw new ElibraryException("{\"error\": \"username does not exist\"}");
        }


        liberian.setPassword(request.getPassword());
        liberianRepository.save(liberian);

        ForgotPasswordResponse response = new ForgotPasswordResponse();
        response.setPassword(liberian.getPassword());

        return response;
    }

    @Override
    public SearchBookResponse searchBook(long id, SearchBookRequest request) throws ElibraryException, IOException {
        checkTitleFieldIsNotEmpty(request);
        String title = validateBookTitle(request);
        String apiUrL = "https://gutendex.com/books?search=" + title;
        String jsonResponse = fetchBookInTheApi(apiUrL);


        return getSearchBookResponse(id, jsonResponse);

    }

    @Override
    public ReadingListResponse getReadingList(ReadingListRequest request) throws ElibraryException {
        Liberian liberian = findBy(request.getId());
        Hibernate.initialize(liberian.getReadingList());
        List<Book> books = liberian.getReadingList();

        if (books.isEmpty()){
            throw new ElibraryException("{\"error\" :\"Reading list is empty\"}");
        }
        ReadingListResponse readingListResponse = new ReadingListResponse();
        readingListResponse.setBooks(books);
        return readingListResponse;
    }

    private SearchBookResponse getSearchBookResponse(long id, String jsonResponse) throws JsonProcessingException, ElibraryException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonResponse);

        if (!root.has("results") || root.get("results").isNull()) {
            throw new ElibraryException("{\"error\" :\"Book not found\"}");
        }

        return getSearchBooks(id, root);
    }


    private SearchBookResponse getSearchBooks(long id, JsonNode root) throws ElibraryException {
        JsonNode results = root.get("results");

        SearchBookResponse searchBookResponse = new SearchBookResponse();
        List<Book> books = new ArrayList<>();
        Liberian liberian = findBy(id);
        for (JsonNode bookNode : results){
            if (bookNode == null || bookNode.isNull()) {
                throw new ElibraryException("{\"error\": \"book not found\"}");
            }

           String bookId = bookNode.get("id").asText();
           String bookTitle = bookNode.get("title").asText();
            List<String> authors = getAuthors(bookNode);

            List<String> subjects = getSubjects(bookNode);

            List<String> languages = getLanguages(bookNode);

            List<String> bookshelves = getBookShelves(bookNode);

            List<String> formats = getFormats(bookNode);

            Book book = getBook(bookId, bookTitle, authors, bookshelves, subjects,languages,formats);
            bookRepository.save(book);

            
            books.add(book);
            liberian.getReadingList().add(book);

        }


        liberianRepository.save(liberian);
        searchBookResponse.setBooks(books);

        if (searchBookResponse.getBooks() == null){
            throw new ElibraryException("{\"error\": \"Book not found\"}");
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

    private static Book getBook(String bookId, String bookTitle, List<String> authors, List<String> bookshelves, List<String> subjects, List<String> languages,List<String> formats) {
        Book book = new Book();
        book.setBookId(bookId);
        book.setTitle(bookTitle);
        book.setAuthor(authors);
        book.setBookshelves(bookshelves);
        book.setSubjects(subjects);
        book.setFormats(formats);
        book.setLanguages(languages);
//        book.getLiberian().add(liberian);


        return book;
    }

    private String validateBookTitle(SearchBookRequest request){
        String title = request.getTitle().trim();

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
            throw new ElibraryException("{\"error\": \"Network failure\"}");
        }

        String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
        if (jsonResponse.isEmpty()) {
            throw new ElibraryException("{\"error\":\"Book not found\"}");
        }

        httpClient.close();

        return jsonResponse;
    }



    private Liberian findBy(Long id) throws ElibraryException {

        return  liberianRepository.findById(id).orElseThrow(()->new ElibraryException(String.format("liberian with id %d not found",id)));
    }

    private boolean userExist(String username){
        Liberian liberian = liberianRepository.findLiberianByUsername(username);
        return liberian != null;
    }

    private void checkIfUsernameExist(LiberianRegisterRequest request) throws ElibraryException {
        if (userExist(request.getUsername())){
            throw new ElibraryException("{\"error\": \"username exist\"}");
        }
    }

    private void validateUserName(LiberianRegisterRequest request) throws ElibraryException {
        if (!(request.getUsername().trim().matches("^(?=.*[a-z])(?=.*\\d)[a-z\\d]{5,10}$"))) {
            throw new ElibraryException("{\"error\": \"Username must contain at least one small letter & number,with length of 5 - 10.\"}");
        }
    }

    private void validateUserNameFieldIsNotEmpty(LiberianRegisterRequest request) throws ElibraryException {
        if (request.getUsername().trim().isEmpty()){
            throw new ElibraryException("{\"error\": \"username field cannot be empty\"}");
        }
    }

    private void validatePassword(LiberianRegisterRequest request) throws ElibraryException {
        if (request.getPassword().trim().isEmpty()){
            throw new ElibraryException("{\"pError\": \"password field is empty, kindly provide your password\"}");
        }

        if (!request.getPassword().matches("^(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()+.=-])(?!.*\\s).{6,11}$")) {
            throw new ElibraryException("{\"pError\": \"password must contain small letters,numbers,symbols,with length of 6 - 10.\"}");
        }
    }

    private void validateForgotPassword(ForgotPasswordRequest request) throws ElibraryException {
        if (request.getPassword().trim().isEmpty()){
            throw new ElibraryException("{\"perror\": \"password field is empty, kindly provide your password\"}");
        }

        if (!request.getPassword().matches("^(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()+.=-])(?!.*\\s).{6,11}$")) {
            throw new ElibraryException("{\"perror\": \"password must contain small letters,numbers,symbols,with length of 6 - 10.\"}");
        }
    }

    private void checkTitleFieldIsNotEmpty(SearchBookRequest request) throws ElibraryException {
        if (request.getTitle().trim().isEmpty()){
            throw new ElibraryException("{\"error\": \"title field is empty\"}");
        }
    }

}
