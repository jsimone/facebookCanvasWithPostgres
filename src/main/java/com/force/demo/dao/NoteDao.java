package com.force.demo.dao;

import java.util.List;

import com.force.demo.model.Note;

public interface NoteDao {

	public void saveNote(Note note);
	public Note getNote(String profileId, String placeId);
	public List<Note> getNotesForUser(String profileId);
}
