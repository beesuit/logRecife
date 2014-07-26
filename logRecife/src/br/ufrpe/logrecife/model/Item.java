package br.ufrpe.logrecife.model;

public class Item {
	
	private String text;
	private boolean originalValue;
	private boolean userValue;

	public Item(String text, boolean originalValue) {
		this.text = text;
		this.originalValue = originalValue;
		this.userValue = false;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(boolean originalValue) {
		this.originalValue = originalValue;
	}

	public boolean isUserValue() {
		return userValue;
	}

	public void setUserValue(boolean userValue) {
		this.userValue = userValue;
	}

	@Override
	public String toString() {
		if(originalValue){
			return text + " Sim"; 
		}
		return text + " Não";
	}
	
	
	

}
