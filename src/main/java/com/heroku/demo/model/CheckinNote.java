package com.heroku.demo.model;

import org.springframework.social.facebook.api.Checkin;

public class CheckinNote {

	private Checkin checkin;
	private String noteText;

	public CheckinNote(Checkin checkin) {
		this.checkin = checkin;
	}
	public Checkin getCheckin() {
		return checkin;
	}
	public void setCheckin(Checkin checkin) {
		this.checkin = checkin;
	}
	public String getNoteText() {
		return noteText;
	}
	public void setNoteText(String noteText) {
		this.noteText = noteText;
	}
}
