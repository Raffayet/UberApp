package com.example.uberbackend.service;

import com.example.uberbackend.dto.RegisterDto;
import com.example.uberbackend.exception.CustomValidationException;
import com.example.uberbackend.exception.EmailAlreadyTakenException;
import com.example.uberbackend.model.Role;
import com.example.uberbackend.model.User;
import com.example.uberbackend.model.enums.AccountStatus;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.model.enums.Provider;
import com.example.uberbackend.model.enums.RoleType;
import com.example.uberbackend.repositories.RoleRepository;
import com.example.uberbackend.repositories.UserRepository;
import com.example.uberbackend.security.SecurityConfig;
import com.example.uberbackend.validator.PasswordMatchValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordMatchValidator passwordMatchValidator;
    private final MapErrorService mapErrorService;
    private final RoleRepository roleRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found in the database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        //authorities.add(new SimpleGrantedAuthority(user.get().getRole().getRole()));
        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), authorities);
    }

    public Optional<User> getUser(String username) {
        return userRepository.findByEmail(username);
    }


    public String registerUser(RegisterDto registerDto, BindingResult result){
        passwordMatchValidator.validate(registerDto, result);

        if(result.hasErrors()){
            throw new CustomValidationException(mapErrorService.mapValidationErrors(result));
        }
        if(userRepository.findByEmail(registerDto.getEmail()).isPresent()){
            throw new EmailAlreadyTakenException("User with email "+registerDto.getEmail()+" already exists.");
        }

        Optional<Role> optionalRole = roleRepository.findByName(RoleType.CLIENT.name());
        if(optionalRole.isEmpty()){
            throw new RuntimeException("Failed Registration due to database problem");
        }

        User user = new User();
        user.setName(registerDto.getFirstName());
        user.setSurname(registerDto.getLastName());
        user.setPassword(SecurityConfig.passwordEncoder().encode(user.getPassword()));
        user.setAccountStatus(AccountStatus.INACTIVE);
        user.setBlocked(false);
        user.setDrivingStatus(DrivingStatus.OFFLINE);
        user.setCity(registerDto.getCity());
        user.setPhoneNumber(registerDto.getTelephone());
        user.setProfileImage(null);
        user.setProvider(Provider.valueOf(registerDto.getProvider().toUpperCase()));
        user.setRole(optionalRole.get());
        userRepository.save(user);

        return "Success";
    }
}
