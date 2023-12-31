package hyun.portfolio9.repositories;

import hyun.portfolio9.entities.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
    List<Posts> findByPostTitleContaining(String keyword);
}
