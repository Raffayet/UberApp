package com.example.uberbackend.controller;


import com.example.uberbackend.dto.*;
import com.example.uberbackend.model.User;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.security.JwtTokenGenerator;
import com.example.uberbackend.service.EmailService;
import com.example.uberbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final EmailService emailService;

    private final JwtTokenGenerator tokenGenerator;

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto, BindingResult result){
        try{
            String message = userService.registerUser(registerDto, result);
            if (message.equals("Success"))
                return new ResponseEntity<>(HttpStatus.CREATED);
            return ResponseEntity.badRequest().build();
        }catch (RuntimeException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/socialLogin")
    public ResponseEntity<?> loginSocialUser(@Valid @RequestBody SocialLoginDto socialLoginDto, BindingResult result){
        try{
            String message = userService.loginSocialUser(socialLoginDto, result);
            if (message.equals("Success"))
                return new ResponseEntity<>(HttpStatus.CREATED);
            return ResponseEntity.badRequest().build();
        }catch (RuntimeException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "token") String token, HttpServletResponse httpServletResponse){
        userService.activateAccount(token);
        httpServletResponse.setHeader("Location", "http://localhost:4200/activatedAccount");
        httpServletResponse.setStatus(302);
    }

    @PutMapping(value = "/update-personal-info")
    public ResponseEntity<?> updatePersonalInfo(@RequestBody PersonalInfoUpdateDto dto){
        try{
            User u = userService.updatePersonalInfo(dto);
            String token = tokenGenerator.generateToken(u);
            return ResponseEntity.ok(token);
        }catch (RuntimeException ex){
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordUpdateDto dto){
        try{
            userService.updatePassword(dto);
            return ResponseEntity.ok("You have successfully updated password!");
        } catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/update-profile-picture")
    public ResponseEntity<String> updateProfilePicture(@RequestBody ProfilePictureUpdateDto dto){
        try{
            userService.updateProfilePicture(dto);
            return ResponseEntity.ok(dto.getB64Image());
        } catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/profile-picture")
    public ResponseEntity<String> getProfilePictureBytes(@RequestParam String email){
        try{
            String bytes = userService.getProfilePicture(email);
            return ResponseEntity.ok(bytes);
        } catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/change-user-driving-status")
    public ResponseEntity<?> changeUserDrivingStatus(@RequestBody UserDrivingStatus dto){
        try{
            User u = userService.changeUserDrivingStatus(dto.getEmail(), dto.getStatus());
            String token = tokenGenerator.generateToken(u);
            return ResponseEntity.ok(token);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping
    public ResponseEntity<?> getUserEmails(){
        try{
            List<String> userEmails= userService.getUserEmails();
            return ResponseEntity.ok(userEmails);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/not-blocked")
    public ResponseEntity<?> getNotBlockedUserEmails(){
        try{
            List<String> userEmails= userService.getNotBlockedUserEmails();
            return ResponseEntity.ok(userEmails);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-user-type")
    public ResponseEntity<?> getUserTypeByEmail(@RequestParam String email){
        try{
            String userType = userService.getUserTypeByEmail(email);
            return ResponseEntity.ok(userType);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/block")
    public ResponseEntity<?> blockUser(@RequestBody BlockUserRequestDto blockUserRequestDto){
        try{
            UserDto userDto= userService.blockUser(blockUserRequestDto);
            return ResponseEntity.ok(userDto);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
