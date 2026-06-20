package com.abdelaziz26.metriplate.controllers;

import com.abdelaziz26.metriplate.dtos.user.UserProfileDto;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.services.user.UserService;
import com.abdelaziz26.metriplate.utils._Abdel3zizController;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController extends _Abdel3zizController {

    UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<@NotNull Result<UserProfileDto, Error>> getUserProfile(@PathVariable Long id) {
        Result<UserProfileDto, Error> res = userService.getUserById(id);
        return ResponseEntity.status(resolveStatus(res)).body(res);
    }
}
