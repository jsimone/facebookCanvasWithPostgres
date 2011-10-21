package com.force.demo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.force.demo.model.Note;

@Repository
public class NoteDaoImpl implements NoteDao {

    //Spring will provide the entity manager based on the 
    //database configuration in the application context
    @PersistenceContext
    private EntityManager entityManager;
    
	@Override
	@Transactional
	public void saveNote(Note note) {
		entityManager.persist(note);
	}

	@Override
	public Note getNote(String profileId, String placeId) {
        Query query =  entityManager.createQuery("from Note note where note.profileId = ?1 and note.placeId = ?2", Note.class);
        query.setParameter(1, profileId);
        query.setParameter(2, placeId);
        return (Note) query.getSingleResult();
	}
	
	@Override
	@SuppressWarnings(value="unchecked")
	public List<Note> getNotesForUser(String profileId) {
		Query query =  entityManager.createQuery("from Note note where note.profileId = ?1", Note.class);
		query.setParameter(1, profileId);
		return query.getResultList();
	}

}
