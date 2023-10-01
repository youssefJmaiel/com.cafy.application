package com.cafy.application.request;

import com.cafy.application.entity.User;
import lombok.Data;
import org.springframework.core.convert.converter.Converter;

import java.util.Objects;
@Data(staticConstructor = "newInstance")
public class UserRequestConverter implements Converter<UserRequest, User>{

    @Override
    public User convert(UserRequest userRequest) {
        if (Objects.isNull(userRequest)) {
            return null;
        }
        return User.builder()
                .email(userRequest.getEmail())
                .contactNumber(userRequest.getContactNumber())
                .name(userRequest.getName())
                .role(userRequest.getRole())
                .password(userRequest.getPassword())
                .status(userRequest.getStatus())
                .build();
    }
}
