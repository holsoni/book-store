package com.example.bookstore.service.shoppingcart.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.shoppingcart.AddCartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.CartItemDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.bookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.CartItemMapper;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.Category;
import com.example.bookstore.model.Role;
import com.example.bookstore.model.RoleName;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.cartitem.CartItemRepository;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 200L;
    private static final User VALID_USER = new User()
            .setId(VALID_ID)
            .setEmail("user@gmail.com")
            .setPassword("12345678")
            .setRoles(Set.of(new Role(RoleName.ROLE_USER)))
            .setShippingAddress("Kyiv, NP 3");
    private static final Category VALID_CATEGORY = new Category()
            .setId(VALID_ID)
            .setName("Fantasy");

    private static final Book VALID_BOOK = new Book()
            .setId(VALID_ID)
            .setTitle("Harry Potter")
            .setAuthor("J. K. Rowling")
            .setIsbn("921-3-ewfwefwef-148410-")
            .setPrice(BigDecimal.valueOf(50.00))
            .setCategories(Set.of(VALID_CATEGORY));
    private static final CartItem VALID_CART_ITEM = new CartItem()
            .setId(VALID_ID)
            .setBook(VALID_BOOK)
            .setQuantity(5);
    private static final ShoppingCart VALID_SHOPPING_CART =
            new ShoppingCart()
                    .setId(VALID_ID)
                    .setUser(VALID_USER)
                    .setCartItems(Set.of(VALID_CART_ITEM));

    private static final UpdateCartItemRequestDto VALID_UPDATE_ITEM_REQUEST =
            new UpdateCartItemRequestDto()
                    .setQuantity(5);
    private static final AddCartItemRequestDto VALID_ADD_ITEM_REQUEST =
            new AddCartItemRequestDto()
                    .setBookId(VALID_ID)
                    .setQuantity(5);

    private static final CartItemDto CART_ITEM_DTO =
            new CartItemDto()
                    .setId(VALID_ID)
                    .setTitle("Harry Potter")
                    .setBookId(VALID_ID)
                    .setQuantity(5);
    private static final ShoppingCartDto SHOPPING_CART_DTO =
            new ShoppingCartDto()
                    .setId(VALID_ID)
                    .setCartItems(Set.of(CART_ITEM_DTO))
                    .setUserId(VALID_ID);
    private static final Authentication AUTHENTICATION =
            mock(Authentication.class);

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeAll
    public static void setUp() {
        VALID_CART_ITEM.setShoppingCart(VALID_SHOPPING_CART);
    }

    @Test
    @DisplayName("Test retrieving an existing shopping cart for the user")
    public void getShoppingCart_ExistingUser_ReturnsShoppingCart() {

        when(AUTHENTICATION.getPrincipal()).thenReturn(VALID_USER);

        when(shoppingCartRepository.findByUserId(VALID_USER.getId()))
                .thenReturn(Optional.of(VALID_SHOPPING_CART));
        when(shoppingCartMapper.toDto(VALID_SHOPPING_CART)).thenReturn(SHOPPING_CART_DTO);

        ShoppingCartDto actual = shoppingCartService.getShoppingCart(AUTHENTICATION);

        verify(shoppingCartRepository).findByUserId(VALID_USER.getId());
        verify(shoppingCartMapper).toDto(VALID_SHOPPING_CART);

        assertNotNull(actual);
        assertEquals(SHOPPING_CART_DTO, actual);
    }

    @Test
    @DisplayName("Test adding cart item to db")
    public void addToCart_ValidRequest_AddsItemToShoppingCart() {
        when(AUTHENTICATION.getPrincipal()).thenReturn(VALID_USER);
        when(shoppingCartRepository.findByUserId(VALID_USER.getId()))
                .thenReturn(Optional.of(VALID_SHOPPING_CART));
        when(cartItemMapper.toEntity(VALID_ADD_ITEM_REQUEST)).thenReturn(VALID_CART_ITEM);
        when(cartItemRepository.save(VALID_CART_ITEM)).thenReturn(VALID_CART_ITEM);
        when(cartItemMapper.toDto(VALID_CART_ITEM)).thenReturn(CART_ITEM_DTO);

        CartItemDto actual = shoppingCartService.addToCart(VALID_ADD_ITEM_REQUEST,
                AUTHENTICATION);

        assertNotNull(actual);
        assertEquals(CART_ITEM_DTO, actual);

        verify(AUTHENTICATION).getPrincipal();
        verify(shoppingCartRepository).findByUserId(VALID_USER.getId());
        verify(cartItemRepository).save(VALID_CART_ITEM);
    }

    @Test
    @DisplayName("Test updating cart item by existing id")
    public void updateItem_ValidRequest_UpdatesItemInDb() {
        when(cartItemRepository.findById(VALID_CART_ITEM.getId()))
                .thenReturn(Optional.of(VALID_CART_ITEM));

        shoppingCartService.updateCartItem(VALID_CART_ITEM.getId(),
                VALID_UPDATE_ITEM_REQUEST);

        verify(cartItemRepository).findById(VALID_CART_ITEM.getId());
        verify(cartItemRepository).save(VALID_CART_ITEM);
        verifyNoMoreInteractions(cartItemRepository);

    }

    @Test
    @DisplayName("Test updating cart item with non existing id")
    public void updateItem_InvalidRequest_UpdatesItemInDb() {
        when(cartItemRepository.findById(INVALID_ID))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            shoppingCartService.updateCartItem(INVALID_ID, VALID_UPDATE_ITEM_REQUEST);
        });
        String expectedMessage = "Sorry! There is no item in cart with id 200";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Test deleting a cart item by ID")
    public void deleteById_ShouldCallRepositoryDelete() {
        doNothing().when(cartItemRepository).deleteById(VALID_CART_ITEM.getId());
        shoppingCartService.deleteCartItem(VALID_CART_ITEM.getId());

        verify(cartItemRepository).deleteById(VALID_ID);
        verifyNoMoreInteractions(cartItemRepository);
    }
}
