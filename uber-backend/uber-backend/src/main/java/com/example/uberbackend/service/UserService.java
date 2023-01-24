package com.example.uberbackend.service;

import com.example.uberbackend.dto.*;
import com.example.uberbackend.exception.CustomValidationException;
import com.example.uberbackend.exception.EmailAlreadyTakenException;
import com.example.uberbackend.exceptions.InvalidPasswordException;
import com.example.uberbackend.model.*;
import com.example.uberbackend.model.enums.AccountStatus;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.model.enums.Provider;
import com.example.uberbackend.model.enums.RoleType;
import com.example.uberbackend.repositories.*;
import com.example.uberbackend.security.SecurityConfig;
import com.example.uberbackend.validator.PasswordMatchValidator;
import lombok.AllArgsConstructor;
import org.javatuples.Pair;
import org.springframework.core.io.ResourceLoader;
//import org.springframework.data.util.Pair;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.validation.BindingResult;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

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
    private final DriverRepository driverRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final VehicleRepository vehicleRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;


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
        throw new UsernameNotFoundException("User with the given email does not exist!");
    }

    public void updateProfilePicture(ProfilePictureUpdateDto dto) throws IOException {
        Optional<User> u = userRepository.findByEmail(dto.getEmail());

        if(u.isPresent()){
            Pair<String, String> tuple = generateNewPicturePath();
            try (FileOutputStream fos = new FileOutputStream(tuple.getValue0())) {
                fos.write(DatatypeConverter.parseBase64Binary(dto.getB64Image()));
            }
            User user = u.get();
            user.setProfileImage(tuple.getValue1());
            userRepository.save(user);
        }else{
            throw new UsernameNotFoundException("User with the given email does not exist!");
        }
    }

    private String generateRandomString(){
        String AlphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
        StringBuilder s = new StringBuilder(10);

        for (int i=0; i<10; i++) {
            int ch = (int)(AlphaNumericStr.length() * Math.random());
            s.append(AlphaNumericStr.charAt(ch));
        }
        return s.toString();
    }

    private  Pair<String, String> generateNewPicturePath() throws IOException {
        String uniqueTime = LocalDateTime.now().toString().split("T")[0];
        String photoName = "//" +uniqueTime + generateRandomString() + "profilePicture.jpg";

        File currDir = new File("uber-backend//src//main//resources//data//");
        String path = currDir.getAbsolutePath();

        return new Pair<String, String>(Paths.get(path + photoName).toString(), photoName.substring(1));
    }

    public String getProfilePicture(String email) throws IOException {
        Optional<User> u = userRepository.findByEmail(email);
        if(u.isPresent()){

            File currDir = new File("uber-backend//src//main//resources//data//");
            String path = currDir.getAbsolutePath();
            String photoName = u.get().getProfileImage();

            String py = Paths.get(path + photoName).toString();
            File file = new File(py);
            FileInputStream fl = new FileInputStream(file);
            byte[] arr = new byte[(int)file.length()];
            fl.read(arr);
            fl.close();
            String encoded = Base64Utils.encodeToString(arr);
            return encoded;
        }else{
            throw new UsernameNotFoundException("User with the given email does not exist!");
        }
    }

    public void save(User loggedUser) {
        userRepository.save(loggedUser);
    }

    public User changeUserDrivingStatus(String email, int status) {
        Optional<User> u = userRepository.findByEmail(email);

        if(u.isPresent()){
            User user = u.get();
            user.setDrivingStatus(DrivingStatus.valueOf(status));
            userRepository.save(user);
            return user;
        }else{
            throw new UsernameNotFoundException("User with the given email does not exist!");
        }
    }

    @Transactional
    public String registerDriver(RegisterDriverDto registerDriverDto, BindingResult result) {
        passwordMatchValidator.validate(registerDriverDto, result);

        if(result.hasErrors()){
            throw new CustomValidationException(mapErrorService.mapValidationErrors(result));
        }
        if(driverRepository.findByEmail(registerDriverDto.getEmail()).isPresent()){
            throw new EmailAlreadyTakenException("Driver with email "+registerDriverDto.getEmail()+" already exists.");
        }

        Optional<Role> optionalRole = roleRepository.findByName(RoleType.DRIVER.name());
        if(optionalRole.isEmpty()){
            throw new RuntimeException("Failed Registration due to database problem");
        }

        Vehicle vehicle = new Vehicle();
        Optional<VehicleType> vehicleTypeOpt = vehicleTypeRepository.findByType(registerDriverDto.getVehicle().getVehicleType());
        if(vehicleTypeOpt.isEmpty())
            throw new RuntimeException("Failed Registration due to database problem");
        vehicle.setVehicleType(vehicleTypeOpt.get());
        vehicle.setModel(registerDriverDto.getVehicle().getModel());
        vehicleRepository.save(vehicle);

        Driver driver = new Driver();
        driver.setName(registerDriverDto.getFirstName());
        driver.setSurname(registerDriverDto.getLastName());
        driver.setEmail(registerDriverDto.getEmail());
        driver.setPassword(SecurityConfig.passwordEncoder().encode(registerDriverDto.getPassword()));
        driver.setAccountStatus(AccountStatus.INACTIVE);
        driver.setActiveAccount(true);
        driver.setBlocked(false);
        driver.setDrivingStatus(DrivingStatus.OFFLINE);
        driver.setCity(registerDriverDto.getCity());
        driver.setPhoneNumber(registerDriverDto.getTelephone());
        driver.setProfileImage(null);
        driver.setProvider(Provider.valueOf(registerDriverDto.getProvider().toUpperCase()));
        driver.setRole(optionalRole.get());
        driver.setVehicle(vehicle);
        driverRepository.save(driver);

        return "Success";

    }

    public List<String> getUserEmails() {
        return userRepository.getUserEmails();
    }

    public List<String> getNotBlockedUserEmails() {
        return userRepository.getNotBlockedUserEmails();
    }

    public UserDto blockUser(BlockUserRequestDto blockUserRequestDto) {
        User user = userRepository.findByEmail(blockUserRequestDto.getUserEmail()).orElse(null);
        if(user == null)
            throw new NullPointerException();
        user.setBlocked(true);
        userRepository.save(user);

        simpMessagingTemplate.convertAndSendToUser(blockUserRequestDto.getUserEmail(), "/blocked-user", blockUserRequestDto);

        return new UserDto(user);
    }

    public String getUserTypeByEmail(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        if(user.isPresent())
        {
            return user.get().getRole().getName();
        }
        return "";
    }
}
