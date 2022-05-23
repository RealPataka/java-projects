package scraper;

public class Checklist {
	String achievement;
	String check;
	String condition;
	
	public Checklist(String achievement, String condition) {
		this.achievement = achievement;
		this.check = "";
		this.condition = condition;
	}
	
	public String getAchievement() {
		return this.achievement;
	}
	
	public String getCondition() {
		return this.condition;
	}
	
	public void setAchievement(String achievement) {
		if (achievement != "") {
			this.achievement = achievement;
		}
	}
	
	public void setCondition(String condition) {
		if (condition != "") {
			this.condition = condition;
		}
	}
	
	public String toString() {
		return String.format("%s: %s", achievement, condition);
	}
}
