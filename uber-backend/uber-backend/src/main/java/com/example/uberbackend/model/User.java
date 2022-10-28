package com.example.uberbackend.model;
import com.example.uberbackend.model.enums.AccountStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.uberbackend.model.enums.DrivingStatus;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="my_user")
@Getter @Setter @NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;
    private String surname;
    private String city;
    private String phoneNumber;
    private Boolean activeAccount;
    private Boolean blocked;
    private String profileImage;
    private DrivingStatus drivingStatus;
    private AccountStatus accountStatus;

    public User(String email, String password, String name, String surname, String city, String phoneNumber, Boolean activeAccount, Boolean blocked, String profileImage, DrivingStatus drivingStatus, AccountStatus accountStatus) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.activeAccount = activeAccount;
        this.blocked = blocked;
        this.profileImage = profileImage;
        this.drivingStatus = drivingStatus;
        this.accountStatus = accountStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(city, user.city) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(activeAccount, user.activeAccount) && Objects.equals(blocked, user.blocked) && Objects.equals(profileImage, user.profileImage) && drivingStatus == user.drivingStatus && accountStatus == user.accountStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, name, surname, city, phoneNumber, activeAccount, blocked, profileImage, drivingStatus, accountStatus);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", city='" + city + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", activeAccount=" + activeAccount +
                ", blocked=" + blocked +
                ", profileImage='" + profileImage + '\'' +
                ", drivingStatus=" + drivingStatus +
                ", accountStatus=" + accountStatus +
                '}';
    }
}
