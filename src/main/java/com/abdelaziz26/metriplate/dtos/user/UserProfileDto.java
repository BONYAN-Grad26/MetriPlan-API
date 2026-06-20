package com.abdelaziz26.metriplate.dtos.user;

import com.abdelaziz26.metriplate.entities.user.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class UserProfileDto {
    String firstName;
    String lastName;
    String email;

    public static UserProfileDto of(User user) {
        return new UserProfileDto(user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
