package org.blog.apis.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.blog.apis.payloads.UserRequestDto;
import org.blog.apis.payloads.UserResponseDto;
import org.blog.apis.services.UserServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserServices services;
    @PostMapping()
    public ResponseEntity<UserResponseDto>create(@Valid @RequestBody UserRequestDto requestDto){
       UserResponseDto userCreated= services.createUser(requestDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @GetMapping()
   public ResponseEntity<List<UserResponseDto>>getAll(){
        return ResponseEntity.ok(services.showAllUsers());
   }

   @GetMapping("/{id}")
   public ResponseEntity<UserResponseDto>findUserUsingId(@PathVariable Long id){
       UserResponseDto user= services.getUserUsingId(id);
       return ResponseEntity.ok(user);
   }

   @PutMapping("/{id}")
   public ResponseEntity<UserResponseDto>update(@Valid @RequestBody UserRequestDto request,@PathVariable Long id){
      UserResponseDto updateUser=  services.updateUserUsingId(request,id);
      return ResponseEntity.ok(updateUser);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<String>delete(@PathVariable Long id){
        services.deleteUser(id);
        return ResponseEntity.noContent().build();
   }


}
