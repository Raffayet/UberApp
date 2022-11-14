package com.example.uberbackend.model;
import com.example.uberbackend.model.enums.Provider;
import com.example.uberbackend.model.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.uberbackend.model.enums.DrivingStatus;
import javax.persistence.*;

@Entity
@Table(name="my_user")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
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
    @ManyToOne
    private Role role;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

}
