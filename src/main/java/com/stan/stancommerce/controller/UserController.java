package com.stan.stancommerce.controller;

import com.stan.stancommerce.dto.UserDto;
import com.stan.stancommerce.entities.User;
import com.stan.stancommerce.mapper.UserMapper;
import com.stan.stancommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
   private final UserService userService;
   private final UserMapper userMapper;

   @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
       @RequestParam(required = false, defaultValue = "name") String name
   ){
       if(!Set.of("name","password","favoriteProducts","email").contains(name)){
           name = "name";
       }
        List<UserDto> users = userService.findAll(name);
        if(!users.isEmpty()){

            return ResponseEntity.status(HttpStatus.OK).body(users);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") long id
    ){
        UserDto user = userService.findById(id);
        if(user != null){
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) ;
    }
}
