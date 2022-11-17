package com.example.uberbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import net.bytebuddy.build.Plugin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

    public Collection<GrantedAuthority> getAuthorities(){
        Collection<GrantedAuthority> retList = new ArrayList<>();
        retList.add(new SimpleGrantedAuthority(name));
        return retList;
    }
}
