package com.example.bookstore.controller;

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

import com.example.bookstore.dto.category.CategoryCreateRequestDto;
import com.example.bookstore.dto.category.CategoryDto;
import com.example.bookstore.model.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
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
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;

    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 200L;
    private static final String INSERT_CATEGORY_SCRIPT =
            "classpath:database/scripts/insert-category.sql";
    private static final String DELETE_CATEGORIES_SCRIPT =
            "classpath:database/scripts/delete-all-from-category.sql";

    private static final Category VALID_CATEGORY = new Category()
            .setId(VALID_ID)
            .setName("Fantasy")
            .setDescription("Very cool category");
    private static final CategoryDto VALID_CATEGORY_DTO = new CategoryDto()
            .setId(VALID_ID)
            .setName("Fantasy")
            .setDescription("Very cool category");
    private static final CategoryCreateRequestDto VALID_CREATE_CATEGORY_DTO =
            new CategoryCreateRequestDto()
                    .setName("Fantasy")
                    .setDescription("Very cool category");

    private static final CategoryCreateRequestDto VALID_UPDATE_CATEGORY_REQUEST_DTO =
            new CategoryCreateRequestDto()
                    .setName("Fantasy Kids")
                    .setDescription("Very cool category");
    private static final CategoryCreateRequestDto INVALID_CREATE_CATEGORY_DTO_EMPTY_TITLE =
            new CategoryCreateRequestDto()
            .setName("-")
            .setDescription("Very cool category");

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext)
            throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = DELETE_CATEGORIES_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Create a new category")
    void createCategory_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(VALID_CREATE_CATEGORY_DTO);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(VALID_CATEGORY_DTO, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = DELETE_CATEGORIES_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Testing creation of invalid book")
    void createCategory_TitleIsEmpty_ThrowsException() throws Exception {
        String jsonRequest = objectMapper
                .writeValueAsString(INVALID_CREATE_CATEGORY_DTO_EMPTY_TITLE);
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )

                .andExpect(status().isBadRequest())
                .andReturn();

        Exception resolvedException = result.getResolvedException();
        assertNotNull(resolvedException);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN, USER"})
    @Sql(
            scripts = INSERT_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORIES_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Get all categories from db")
    void getAll_getALlCategories() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(VALID_CATEGORY.getName())))
                .andExpect(jsonPath("$[0].description", is(VALID_CATEGORY.getDescription())));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN, USER"})
    @Sql(
            scripts = INSERT_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORIES_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Get category from db by valid id")
    void getById_ValidId_Successful() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/" + VALID_ID))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(VALID_CATEGORY_DTO, actual);
        System.out.println(actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN, USER"})
    @Sql(
            scripts = INSERT_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORIES_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Get category from db by invalid id")
    void getCategoryById_InvalidId_StatusNotFound() throws Exception {
        mockMvc.perform(get("/categories/" + INVALID_ID))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = INSERT_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORIES_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Delete category from db by valid id")
    void deleteCategory_Success() throws Exception {
        mockMvc.perform(delete("/categories/" + VALID_ID)
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = INSERT_CATEGORY_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORIES_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Update category from db by valid data and id")
    void updateCategory_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(VALID_UPDATE_CATEGORY_REQUEST_DTO);
        mockMvc.perform(put("/categories/" + VALID_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
    }
}
