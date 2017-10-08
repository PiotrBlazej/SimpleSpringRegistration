package example.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import example.model.User;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findUserByEmail(String email);

	User findUserById(int id);
	
	User findUserByToken(String token);

	@Transactional
	@Modifying
	@Query("update User u set u.token=?2, u.active = ?3 where u.email =?1")
	void activeUser(String email, String token, boolean active);

}
