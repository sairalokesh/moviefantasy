package com.lokesh.movies.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lokesh.movies.domain.Movie;
import com.lokesh.movies.domain.Trailer;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MovieUtil {
	public static Map<Long, String> movieGenrics = new LinkedHashMap<>();
	public static final String userKey = "5HbImlTRhrrI7aEO";
	public static final String secretKey = "s51qflw236t2ddlcbom9d0hfe800tp";
	public static final String apiKey = "663337055530cc77a3aa1d26fec365d5";

	static {
		movieGenrics.put(28L, "Action");
		movieGenrics.put(12L, "Adventure");
		movieGenrics.put(16L, "Animation");
		movieGenrics.put(35L, "Comedy");
		movieGenrics.put(80L, "Crime");
		movieGenrics.put(99L, "Documentary");
		movieGenrics.put(18L, "Drama");
		movieGenrics.put(10751L, "Family");
		movieGenrics.put(14L, "Fantasy");
		movieGenrics.put(36L, "History");
		movieGenrics.put(27L, "Horror");
		movieGenrics.put(10402L, "Music");
		movieGenrics.put(9648L, "Mystery");
		movieGenrics.put(10749L, "Romance");
		movieGenrics.put(878L, "Science Fiction");
		movieGenrics.put(10770L, "TV Movie");
		movieGenrics.put(53L, "Thriller");
		movieGenrics.put(10752L, "War");
		movieGenrics.put(37L, "Western");
	}

	public static String getMovieGenries(List<Long> genericIds) {
		List<String> genrics = new ArrayList<>();
		for(Long genericId : genericIds) {
			genrics.add(movieGenrics.get(genericId));
		}
		return genrics.stream().map(Object::toString).collect(Collectors.joining(", "));
	}

	public static List<Trailer> getMovieTrailers(List<Trailer> movieTrailers, Movie movie) {
		if(movieTrailers != null && movieTrailers.size() > 0) {
			for(int i = 0; i<movieTrailers.size() ; i++) {
				String imagePath = i % 2 == 0 ? movie.getBackdrop_path() : movie.getPoster_path();
				movieTrailers.get(i).setImage_path(imagePath);
			}
		}
		return movieTrailers;
	}
	
	public static String convertMovieTiming(Integer runTime) {
		int hours = runTime / 60; 
		int minutes = runTime % 60;
		return hours+"h : "+minutes+"m";
	}
	
	
	

}
