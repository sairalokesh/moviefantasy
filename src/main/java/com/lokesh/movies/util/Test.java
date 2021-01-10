package com.lokesh.movies.util;

public class Test {

	public static void main(String[] args) {
		Integer t = 109;
		int hours = t / 60; //since both are ints, you get an int
		int minutes = t % 60;
		System.out.printf("%d:%02d", hours, minutes);

	}

}
