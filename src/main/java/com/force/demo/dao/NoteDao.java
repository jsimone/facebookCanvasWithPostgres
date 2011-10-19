package com.force.demo.dao;

import com.force.demo.model.Note;

public interface NoteDao {

	public void saveNote(Note note);
	public Note getNote(String profileId, String placeId);
}
