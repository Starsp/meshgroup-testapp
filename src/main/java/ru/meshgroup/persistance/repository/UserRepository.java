package ru.meshgroup.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.meshgroup.persistance.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

}
