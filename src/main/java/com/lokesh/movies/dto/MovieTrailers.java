package com.lokesh.movies.dto;

import java.util.List;

import com.lokesh.movies.domain.Movie;
import com.lokesh.movies.domain.MovieCastCrew;
import com.lokesh.movies.domain.Trailer;

public class MovieTrailers {

	private Movie movie;
	private List<Trailer> trailers;
	private List<MovieCastCrew> casts;
	private List<MovieCastCrew> crews;

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public List<Trailer> getTrailers() {
		return trailers;
	}

	public void setTrailers(List<Trailer> trailers) {
		this.trailers = trailers;
	}

	public List<MovieCastCrew> getCasts() {
		return casts;
	}

	public void setCasts(List<MovieCastCrew> casts) {
		this.casts = casts;
	}

	public List<MovieCastCrew> getCrews() {
		return crews;
	}

	public void setCrews(List<MovieCastCrew> crews) {
		this.crews = crews;
	}

}
