package com.logstash.json;

public class Stages {
	public Stage input = new Stage();
	public Stage filter = new Stage();
	public Stage output = new Stage();
	
	public Stage getStageFromName(final String stageName) {
		switch (stageName) {
		case "input":
			return input;
		case "filter":
			return filter;
		case "output":
			return output;
		default:
			return null;
		}
	}
}
