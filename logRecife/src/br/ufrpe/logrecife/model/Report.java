package br.ufrpe.logrecife.model;

import java.util.UUID;

public class Report {
	private Logradouro logradouro;
	private String reportText;
	private String cpf;
	private String email;
	private boolean sent;
	private String pictureFile;
	private UUID uuid;

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

	public String getReport() {
		return reportText;
	}

	public void setReport(String report) {
		this.reportText = report;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
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

}
