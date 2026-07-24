package org.blog.apis.services;

import org.blog.apis.payloads.UserRequestDto;
import org.blog.apis.payloads.UserResponseDto;

import java.util.List;

public interface UserServices {
   UserResponseDto createUser(UserRequestDto request);
   List<UserResponseDto> showAllUsers();
   UserResponseDto getUserUsingId(Long id);
   UserResponseDto updateUserUsingId(UserRequestDto request,Long id);
   void deleteUser(Long id);

}
