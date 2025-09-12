package com.palci.CoachProgram.Models.Services;

import com.palci.CoachProgram.Models.DTO.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void create(UserDTO userDTO,boolean isAdmin);
}
