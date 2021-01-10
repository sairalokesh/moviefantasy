package com.lokesh.movies.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.json.JSONArray;

import com.lokesh.movies.domain.Languages;
import com.lokesh.movies.domain.Movie;
import com.lokesh.movies.domain.MovieGenres;
import com.lokesh.movies.domain.Person;
import com.lokesh.movies.dto.MovieTrailers;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface MovieService {

	public List<Movie> getAllMovies() throws UnirestException;
	public MovieTrailers getMovieDetailsByMovieId(String movieId, String type) throws UnirestException;
	public JSONArray getMoviesByIndexOrSearchOrGeneric(String pageIndex, String type, String query, String queryId, String SearchType) throws UnirestException, UnsupportedEncodingException;
	public List<MovieGenres> getMovieGenries() throws UnirestException;
	public Person getPersonByPersonId(String personId) throws UnirestException;
	public List<Movie> getMoviesByPersonId(String queryId) throws UnirestException;
	public List<Languages> getMovieLanguages() throws UnirestException;
	public String getMovieTicketByMovieIdAndTicketId(String imdb_id, String ipAddress) throws UnirestException;

}
