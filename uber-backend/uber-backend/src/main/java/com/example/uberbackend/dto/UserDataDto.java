package com.example.uberbackend.dto;
import com.example.uberbackend.model.User;
import com.example.uberbackend.model.enums.AccountStatus;
import com.example.uberbackend.model.enums.DrivingStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDataDto {
    private long id;
    private String name;
    private String surname;
    private String email;
    private String role;
    private String city;
    private String phoneNumber;
    private Boolean activeAccount;
    private Boolean blocked;
    private String profileImage;
    private DrivingStatus drivingStatus;
    private AccountStatus accountStatus;

    public UserDataDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.email = user.getEmail();
        this.role = user.getRole().getName();
        this.profileImage = user.getProfileImage();
        this.city = user.getCity();
        this.phoneNumber = user.getPhoneNumber();
        this.accountStatus = user.getAccountStatus();
        this.activeAccount = user.getActiveAccount();
        this.blocked = user.getBlocked();
        this.drivingStatus = user.getDrivingStatus();
        this.accountStatus = user.getAccountStatus();

    }
}
