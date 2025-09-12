package com.palci.CoachProgram.Models.Services;

import com.palci.CoachProgram.Data.Entities.UserEntity;
import com.palci.CoachProgram.Data.Repositories.UserRepository;
import com.palci.CoachProgram.Models.DTO.UserDTO;
import com.palci.CoachProgram.Models.Exceptions.DuplicateEmailException;
import com.palci.CoachProgram.Models.Exceptions.PasswordsDoNotEqualException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void create(UserDTO userDTO, boolean isAdmin) {
        // If passwords do not equal - throw exception
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())){
            throw new PasswordsDoNotEqualException();
        }

        UserEntity userEntity = new UserEntity(); // Creating entity to add to database

        userEntity.setEmail(userDTO.getEmail());
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userEntity.setAdmin(isAdmin);

        try {
            userRepository.save(userEntity);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateEmailException();
        }

    }
}
