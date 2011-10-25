package com.heroku.demo.dao;

import java.util.List;

import com.heroku.demo.model.Note;

public interface NoteDao {

	public void saveNote(Note note);
	public Note getNote(String profileId, String placeId);
	public List<Note> getNotesForUser(String profileId);
}
