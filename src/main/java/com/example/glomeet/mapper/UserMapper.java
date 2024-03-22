package com.example.glomeet.mapper;

import com.example.glomeet.controller.AuthController.AdditionalInfoDTO;
import com.example.glomeet.controller.AuthController.ResetPasswordDTO;
import com.example.glomeet.controller.AuthController.SignUpDTO;
import com.example.glomeet.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    public int emailCheck(String email);

    public int nickNameCheck(String nickName);

    public int insertUser(SignUpDTO signUpDTO);

    public UserDTO findUserByEmail(String email);

    public int updatePassword(ResetPasswordDTO resetPasswordDTO);

    public int additionalInfoCheck(String email);

    public int updateAdditionalInfo(AdditionalInfoDTO additionalInfoDTO);

    String findNickNameByEmail(String email);

}
