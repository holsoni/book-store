package com.example.bookstore.controller;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 200L;
    private static final Pageable VALID_PAGEABLE = PageRequest.of(0, 10);
    private static final Category VALID_CATEGORY = new Category()
            .setId(VALID_ID)
            .setName("Fantasy");
    private static final Book VALID_BOOK = new Book()
            .setId(VALID_ID)
            .setTitle("Harry Potter")
            .setAuthor("J. K. Rowling")
            .setIsbn("921-3-ewfwefwef-148410-")
            .setPrice(BigDecimal.valueOf(50.00))
            .setDescription("This is description")
            .setCoverImage("cover.jpeg")
            .setCategories(Set.of(VALID_CATEGORY));
    private static final CreateBookRequestDto VALID_CREATE_BOOK_REQUEST_DTO =
            new CreateBookRequestDto()
                    .setTitle("Harry Potter")
                    .setAuthor("J. K. Rowling")
                    .setIsbn("921-3-ewfwefwef-148410-")
                    .setPrice(BigDecimal.valueOf(50.00))
                    .setDescription("This is description")
                    .setCoverImage("cover.jpeg")
                    .setCategoryIds(Set.of(VALID_ID));

    private static final CreateBookRequestDto INVALID_CREATE_BOOK_REQUEST_DTO =
            new CreateBookRequestDto()
                    .setTitle("h")
                    .setAuthor("J")
                    .setIsbn("921-3-ewfwefwef-148410-")
                    .setPrice(BigDecimal.valueOf(-50))
                    .setDescription("This is description")
                    .setCoverImage("cover.jpeg")
                    .setCategoryIds(Set.of(VALID_ID));
    private static final BookDto VALID_BOOK_DTO = new BookDto()
            .setId(VALID_ID)
            .setTitle("Harry Potter")
            .setAuthor("J. K. Rowling")
            .setIsbn("921-3-ewfwefwef-148410-")
            .setDescription("This is description")
            .setCoverImage("cover.jpeg")
            .setPrice(BigDecimal.valueOf(50.00));
    private static final BookDtoWithoutCategoryIds VALID_BOOK_DTO_WITHOUT_CATEGORY_IDS =
            new BookDtoWithoutCategoryIds()
                    .setId(VALID_ID)
                    .setTitle("Harry Potter")
                    .setAuthor("J. K. Rowling")
                    .setIsbn("921-3-ewfwefwef-148410-")
                    .setDescription("This is description")
                    .setCoverImage("cover.jpeg")
                    .setPrice(BigDecimal.valueOf(50.00));

    protected static MockMvc mockMvc;
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
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/scripts/delete-all-from-books.sql")
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/scripts/delete-all-from-books.sql",
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

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(VALID_BOOK_DTO, actual);
        EqualsBuilder.reflectionEquals(VALID_BOOK_DTO, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/scripts/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Testing creation of invalid book")
    void createBook_TitleIsNullAndPriceLessThanNull_ThrowsException() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(INVALID_CREATE_BOOK_REQUEST_DTO);
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
            scripts = "classpath:database/scripts/insert-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/scripts/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Get all books from db")
    void getAll_getALlBooksFromDb() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(INVALID_CREATE_BOOK_REQUEST_DTO);
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].isbn", is(VALID_BOOK.getIsbn())));
    }

    @Sql(
            scripts = "classpath:database/scripts/insert-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/scripts/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Get book from db by valid id")
    void getById_ValidId_Successful() throws Exception {
        mockMvc.perform(get("/books/" + VALID_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VALID_BOOK_DTO)));
    }

    @Sql(
            scripts = "classpath:database/scripts/insert-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/scripts/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Get book from db by invalid id")
    void getById_InValidId_BadRequest() throws Exception {
        mockMvc.perform(get("/books/" + INVALID_ID))
                .andExpect(status().isUnauthorized());
    }

}