package com.example.uberbackend.service;

import com.example.uberbackend.dto.PasswordUpdateDto;
import com.example.uberbackend.dto.PersonalInfoUpdateDto;
import com.example.uberbackend.dto.RegisterDto;
import com.example.uberbackend.dto.SocialLoginDto;
import com.example.uberbackend.exception.CustomValidationException;
import com.example.uberbackend.exception.EmailAlreadyTakenException;
import com.example.uberbackend.exceptions.InvalidPasswordException;
import com.example.uberbackend.model.ActivateAccountToken;
import com.example.uberbackend.model.Role;
import com.example.uberbackend.model.User;
import com.example.uberbackend.model.enums.AccountStatus;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.model.enums.Provider;
import com.example.uberbackend.model.enums.RoleType;
import com.example.uberbackend.repositories.ActivateAccountTokenRepository;
import com.example.uberbackend.repositories.RoleRepository;
import com.example.uberbackend.repositories.UserRepository;
import com.example.uberbackend.security.SecurityConfig;
import com.example.uberbackend.validator.PasswordMatchValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordMatchValidator passwordMatchValidator;
    private final MapErrorService mapErrorService;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final ActivateAccountTokenRepository accountTokenRepository;
    private final PasswordEncoder passwordEncoder;


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
        user.setEmail(registerDto.getEmail());
        user.setPassword(SecurityConfig.passwordEncoder().encode(registerDto.getPassword()));
        user.setAccountStatus(AccountStatus.INACTIVE);
        user.setActiveAccount(false);
        user.setBlocked(false);
        user.setDrivingStatus(DrivingStatus.OFFLINE);
        user.setCity(registerDto.getCity());
        user.setPhoneNumber(registerDto.getTelephone());
        user.setProfileImage(null);
        user.setProvider(Provider.valueOf(registerDto.getProvider().toUpperCase()));
        user.setRole(optionalRole.get());
        userRepository.save(user);

        this.sendAccountActivationEmail(user);

        return "Success";
    }

    private void sendAccountActivationEmail(User user){
        String token = UUID.randomUUID().toString();
        ActivateAccountToken activateAccountToken = new ActivateAccountToken();
        activateAccountToken.setToken(token);
        activateAccountToken.setUser(user);
        accountTokenRepository.save(activateAccountToken);

        emailService.sendConfirmationAsync(user.getEmail(), token);

    }

    public void activateAccount(String token) {
        Optional<ActivateAccountToken> optActivateAccountToken = accountTokenRepository.findByToken(token);
        if(optActivateAccountToken.isEmpty()){
            throw new RuntimeException("No user with that token.");
        }
        long userId = optActivateAccountToken.get().getUser().getId();
        User user = userRepository.findById(userId).orElse(null);
        assert user != null;
        user.setActiveAccount(true);
        userRepository.save(user);
        try {
            emailService.sendConfirmationRegistrationRequest(user.getEmail());
        } catch (InterruptedException e) {
            throw new RuntimeException("Error sending email.");
        }
    }

    public String loginSocialUser(SocialLoginDto socialLoginDto, BindingResult result) {
        if(result.hasErrors()){
            throw new CustomValidationException(mapErrorService.mapValidationErrors(result));
        }
        if(userRepository.findByEmail(socialLoginDto.getEmail()).isPresent()){
            return "User already defined";
        }

        Optional<Role> optionalRole = roleRepository.findByName(RoleType.CLIENT.name());
        if(optionalRole.isEmpty()){
            throw new RuntimeException("Failed social login due to database problem");
        }

        User user = new User();
        user.setName(socialLoginDto.getFirstName());
        user.setSurname(socialLoginDto.getLastName());
        user.setEmail(socialLoginDto.getEmail());
        user.setPassword("social-password");
        user.setAccountStatus(AccountStatus.INACTIVE);
        user.setActiveAccount(false);
        user.setBlocked(false);
        user.setDrivingStatus(DrivingStatus.OFFLINE);
        user.setCity(socialLoginDto.getCity());
        user.setPhoneNumber(socialLoginDto.getTelephone());
        user.setProfileImage(socialLoginDto.getPhotoUrl());
        user.setProvider(Provider.valueOf(socialLoginDto.getProvider().toUpperCase()));
        user.setRole(optionalRole.get());
        userRepository.save(user);

        return "Success";
    }

    public User updatePersonalInfo(PersonalInfoUpdateDto dto) {
        Optional<User> u = userRepository.findByEmail(dto.getEmail());
        if(u.isPresent()) {
            User toUpdate = u.get();
            toUpdate.setName(dto.getName());
            toUpdate.setSurname(dto.getSurname());
            toUpdate.setCity(dto.getCity());
            toUpdate.setPhoneNumber(dto.getPhone());

            userRepository.save(toUpdate);
            return toUpdate;
        }
        throw new UsernameNotFoundException("");
    }

    public void updatePassword(PasswordUpdateDto dto){
        Optional<User> ou = userRepository.findByEmail(dto.getEmail());
        if(ou.isPresent()){
            User u = ou.get();
            String newPasswordHash = passwordEncoder.encode(dto.getNewPassword());
            if (!dto.getNewPassword().equals("") && dto.getNewPassword().equals(dto.getConfirmNewPassword()) && passwordEncoder.matches(dto.getOldPassword(), u.getPassword())) {
                u.setPassword(newPasswordHash);
                userRepository.save(u);
                return;
            }else{
                throw new InvalidPasswordException("Data is invalid!");
            }
        }
        throw new UsernameNotFoundException("User with given email does not exist!");
    }
}
