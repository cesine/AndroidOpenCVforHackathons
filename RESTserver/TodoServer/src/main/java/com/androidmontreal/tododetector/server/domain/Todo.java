package com.androidmontreal.tododetector.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "TODO")
@XmlRootElement
public class Todo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private boolean checked;

	@Column(columnDefinition = "LONGBLOB")
	private byte[] image;

	public Long getId() {
		return id;
	}

	public byte[] getImage() {
		return image;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

}
