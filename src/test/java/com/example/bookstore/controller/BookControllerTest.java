package com.example.bookstore.controller;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.dto.book.UpdateBookRequestDto;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Set;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 200L;
    private static final String INSERT_CATEGORY_SCRIPT =
            "classpath:database/scripts/insert-category.sql";
    private static final String INSERT_BOOK_SCRIPT =
            "classpath:database/scripts/insert-book.sql";
    private static final String INSERT_BOOK_CATEGORY_SCRIPT =
            "classpath:database/scripts/insert-book-category-relations-for-harry_potter.sql";
    private static final String DELETE_CATEGORIES_SCRIPT =
            "classpath:database/scripts/delete-all-from-category.sql";
    private static final String DELETE_BOOKS_SCRIPT =
            "classpath:database/scripts/delete-all-from-books.sql";
    private static final String DELETE_BOOK_CATEGORY_SCRIPT =
            "classpath:database/scripts/delete-all-from-book_category.sql";
    private static final Category VALID_CATEGORY = new Category()
            .setId(VALID_ID)
            .setName("Fantasy")
            .setDescription("Very cool category");
    private static final Book VALID_BOOK = new Book()
            .setId(VALID_ID)
            .setTitle("Harry Potter")
            .setAuthor("Rowling Joan")
            .setIsbn("978-3-16-148410-0")
            .setPrice(BigDecimal.valueOf(50.00))
            .setDescription("Very cool book")
            .setCoverImage("cover.jpeg")
            .setCategories(Set.of(VALID_CATEGORY));
    private static final CreateBookRequestDto VALID_CREATE_BOOK_REQUEST_DTO =
            new CreateBookRequestDto()
                    .setTitle("Harry Potter")
                    .setAuthor("Rowling Joan")
                    .setIsbn("978-3-16-148410-0")
                    .setPrice(BigDecimal.valueOf(50.00))
                    .setDescription("Very cool book")
                    .setCoverImage("cover.jpeg")
                    .setCategoryIds(Set.of(VALID_ID));

    private static final CreateBookRequestDto INVALID_CREATE_BOOK_REQUEST_DTO_EMPTY_TITLE =
            new CreateBookRequestDto()
                    .setTitle("-")
                    .setAuthor("Joan")
                    .setIsbn("978-3-16-148410-0")
                    .setPrice(BigDecimal.valueOf(-50))
                    .setDescription("Very cool book")
                    .setCoverImage("cover.jpeg")
                    .setCategoryIds(Set.of(VALID_ID));
    private static final UpdateBookRequestDto VALID_UPDATE_BOOK_REQUEST_DTO =
            new UpdateBookRequestDto()
                    .setTitle("Harry Potter")
                    .setAuthor("Rowling Joan")
                    .setIsbn("978-3-16-148410-0")
                    .setPrice(BigDecimal.valueOf(60.00))
                    .setDescription("Very cool book")
                    .setCoverImage("cover.jpeg")
                    .setCategoryIds(Set.of(VALID_ID));

    private static final BookDto VALID_BOOK_DTO = new BookDto()
            .setId(VALID_ID)
            .setTitle("Harry Potter")
            .setAuthor("Rowling Joan")
            .setIsbn("978-3-16-148410-0")
            .setDescription("Very cool book")
            .setCategoryIds(Set.of(VALID_ID))
            .setCoverImage("cover.jpeg")
            .setPrice(BigDecimal.valueOf(50.00));
    private static final BookDtoWithoutCategoryIds VALID_BOOK_DTO_WITHOUT_CATEGORY_IDS =
            new BookDtoWithoutCategoryIds()
                    .setId(VALID_ID)
                    .setTitle("Harry Potter")
                    .setAuthor("Rowling Joan")
                    .setIsbn("978-3-16-148410-0")
                    .setDescription("Very cool book")
                    .setCoverImage("cover.jpeg")
                    .setPrice(BigDecimal.valueOf(50.00));
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource,
                          @Autowired WebApplicationContext applicationContext)
            throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = DELETE_BOOKS_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Create a new book")
    void createBook_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(VALID_CREATE_BOOK_REQUEST_DTO);

        MvcResult result = mockMvc.perform(post("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(VALID_BOOK_DTO, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = DELETE_BOOKS_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Testing creation of invalid book")
    void createBook_TitleIsNullAndPriceLessThanNull_ThrowsException() throws Exception {
        String jsonRequest = objectMapper
                .writeValueAsString(INVALID_CREATE_BOOK_REQUEST_DTO_EMPTY_TITLE);
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )

                .andExpect(status().isBadRequest())
                .andReturn();

        Exception resolvedException = result.getResolvedException();
        assertNotNull(resolvedException);
        assertTrue(resolvedException instanceof MethodArgumentNotValidException);

    }

    @WithMockUser(username = "admin", roles = {"ADMIN, USER"})
    @Sql(
            scripts = {INSERT_CATEGORY_SCRIPT, INSERT_BOOK_SCRIPT,INSERT_BOOK_CATEGORY_SCRIPT},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {DELETE_BOOKS_SCRIPT, DELETE_CATEGORIES_SCRIPT, DELETE_BOOK_CATEGORY_SCRIPT},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Get all books from db")
    void getAll_getALlBooksFromDb() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].isbn", is(VALID_BOOK.getIsbn())));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = {INSERT_CATEGORY_SCRIPT, INSERT_BOOK_SCRIPT,INSERT_BOOK_CATEGORY_SCRIPT},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {DELETE_BOOKS_SCRIPT, DELETE_CATEGORIES_SCRIPT, DELETE_BOOK_CATEGORY_SCRIPT},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Get book from db by valid id")
    void getById_ValidId_Successful() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/" + VALID_ID))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(VALID_BOOK_DTO, actual, "id");
        System.out.println(actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = {INSERT_CATEGORY_SCRIPT, INSERT_BOOK_SCRIPT,INSERT_BOOK_CATEGORY_SCRIPT},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {DELETE_BOOKS_SCRIPT, DELETE_CATEGORIES_SCRIPT, DELETE_BOOK_CATEGORY_SCRIPT},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Get book from db by invalid id")
    void getById_InValidId_BadRequest() throws Exception {
        mockMvc.perform(get("/books/" + INVALID_ID))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = {INSERT_CATEGORY_SCRIPT, INSERT_BOOK_SCRIPT,INSERT_BOOK_CATEGORY_SCRIPT},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {DELETE_BOOKS_SCRIPT, DELETE_CATEGORIES_SCRIPT, DELETE_BOOK_CATEGORY_SCRIPT},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Update book from db by valid data and id")
    void updateBook_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(VALID_UPDATE_BOOK_REQUEST_DTO);
        mockMvc.perform(put("/books/" + VALID_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = {INSERT_CATEGORY_SCRIPT, INSERT_BOOK_SCRIPT,INSERT_BOOK_CATEGORY_SCRIPT},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {DELETE_BOOKS_SCRIPT, DELETE_CATEGORIES_SCRIPT, DELETE_BOOK_CATEGORY_SCRIPT},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Delete book from db by valid id")
    void deleteBook_Success() throws Exception {
        mockMvc.perform(delete("/books/" + VALID_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
