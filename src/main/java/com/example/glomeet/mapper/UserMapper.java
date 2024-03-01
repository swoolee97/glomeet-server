package com.example.glomeet.mapper;

import com.example.glomeet.controller.AuthController;
import com.example.glomeet.controller.AuthController.SignUpDTO;
import com.example.glomeet.dto.UserDTO;
import com.example.glomeet.controller.AuthController.ResetPasswordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface UserMapper {
    public int emailCheck(String email);

    public int nickNameCheck(String nickName);

    public int insertUser(SignUpDTO signUpDTO);

    public UserDTO findUserByEmail(String email);

    public int updatePassword(ResetPasswordDTO resetPasswordDTO);


}
