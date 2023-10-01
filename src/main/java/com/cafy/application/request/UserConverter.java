package com.cafy.application.request;

import com.cafy.application.entity.User;
import lombok.Data;
import org.springframework.core.convert.converter.Converter;

import java.util.Objects;

@Data(staticConstructor = "newInstance")

public class UserConverter implements Converter<User, UserDto> {
    @Override
    public UserDto convert(User user) {
        if (Objects.isNull(user)) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .contactNumber(user.getContactNumber())
                .name(user.getName())
                .role(user.getRole())
                .password(user.getPassword())
                .status(user.getStatus())
                .build();
    }
}
