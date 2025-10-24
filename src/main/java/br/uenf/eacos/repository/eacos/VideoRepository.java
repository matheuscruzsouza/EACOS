package br.uenf.eacos.repository.eacos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.uenf.eacos.model.entity.eacos.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    Page<Video> findAll(Pageable pageable);

}
