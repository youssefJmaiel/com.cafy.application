package com.cafy.application.request;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@Getter
public class UserRequest {


    private String name;

    private String contactNumber;

    private String email;

    private String password;

    private String status;

    private String role;


}
