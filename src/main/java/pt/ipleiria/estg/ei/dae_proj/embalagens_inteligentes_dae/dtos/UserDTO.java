package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos;

import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {
    @NotNull
    private String username;
    @NotNull
    private String name;
    @NotNull
    private String address;
    @NotNull
    private int phoneNumber;
    @NotNull
    private String role;


    public UserDTO() {
    }
    public UserDTO(String username, String name, String address, int phoneNumber, String role) {
        this.username = username;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }


    public static UserDTO from(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getName(),
                user.getAddress(),
                user.getPhoneNumber(),
                Hibernate.getClass(user).getSimpleName()
        );
    }

    public static List<UserDTO> from(List<User> users) {
        return users.stream().map(UserDTO::from).collect(Collectors.toList());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}