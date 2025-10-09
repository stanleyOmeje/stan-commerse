package com.stan.stancommerce.controller;

import com.stan.stancommerce.dto.ChangePasswordRequest;
import com.stan.stancommerce.dto.RegisterUserRequest;
import com.stan.stancommerce.dto.UpdateUserRequest;
import com.stan.stancommerce.dto.UserDto;
import com.stan.stancommerce.mapper.UserMapper;
import com.stan.stancommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;

@Slf4j
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

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserById(@RequestBody UpdateUserRequest request, @PathVariable("id") long id
    ){
        UserDto user = userService.updateUser(request, id);
        if(user != null){
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) ;
    }

    @PostMapping("/{id}/chang-password")
    public ResponseEntity<UserDto> changePassword(@RequestBody ChangePasswordRequest request, @PathVariable("id") long id
    ){
        UserDto user = userService.changePassword(request, id);
        if(user != null){
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) ;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid RegisterUserRequest request,
                                                UriComponentsBuilder uriBuilder){
       try{
           UserDto userDto = userService.registerUser(request);
           URI uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
           return ResponseEntity.created(uri).body(userDto);
       }catch (Exception e){
           log.info(e.getMessage());
       }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) ;
    }

}
