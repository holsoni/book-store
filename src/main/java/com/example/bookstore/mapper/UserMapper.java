package com.example.bookstore.mapper;

import com.example.bookstore.config.MapperConfiguration;
import com.example.bookstore.dto.user.UserRegistrationRequestDto;
import com.example.bookstore.dto.user.UserResponseDto;
import com.example.bookstore.model.User;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toModel(UserRegistrationRequestDto registrationRequestDto);

    @Named("userFromId")
    default User userFromId(Long id) {
        return Optional.ofNullable(id)
                .map(User::new)
                .orElse(null);
    }
}
