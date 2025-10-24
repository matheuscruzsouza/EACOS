package br.uenf.eacos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.uenf.eacos.model.entity.auth.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdentifier(String sub);

}
