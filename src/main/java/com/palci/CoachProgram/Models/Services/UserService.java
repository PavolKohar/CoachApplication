package com.palci.CoachProgram.Models.Services;

import com.palci.CoachProgram.Models.DTO.UserDTO;

public interface UserService {

    void create(UserDTO userDTO,boolean isAdmin);
}
