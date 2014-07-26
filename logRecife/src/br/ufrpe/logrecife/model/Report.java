package br.ufrpe.logrecife.model;

import java.util.UUID;

public class Report {
	private Logradouro logradouro;
	private String reportText;
	private String email;
	private boolean sent;
	private String pictureFile;
	private UUID uuid;
	private String time;

	public Report(Logradouro logradouro, boolean sent) {
		this.logradouro = logradouro;
		this.sent = sent;
		this.uuid = UUID.randomUUID();
	}

	public Logradouro getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(Logradouro logradouro) {
		this.logradouro = logradouro;
	}

	public String getReportText() {
		return reportText;
	}

	public void setReportText(String report) {
		this.reportText = report;
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPictureFile() {
		return pictureFile;
	}

	public void setPictureFile(String pictureFilepath) {
		this.pictureFile = pictureFilepath;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Report){
			if(((Report) o).getUuid().equals(this.getUuid())){
				return true;
			}
		}
		return false;
	}

}
