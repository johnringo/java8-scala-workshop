package no.bekk.java.excercises;

import no.bekk.java.model.League;
import no.bekk.java.model.Player;
import no.bekk.java.model.Team;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toList;

public class LambdaWithStreams {

	static List<String> teamNames(List<Team> teams) {
		return teams.stream().map(Team::getName).collect(toList());
	}

	static Integer ageOfOldestPlayer(List<Player> players) {
		return players.stream().mapToInt(Player::getAge).max().getAsInt();
	}

	static Double averageAgeOfPlayers(List<Player> players) {
		return players.stream().mapToInt(Player::getAge).average().getAsDouble();
	}

	static String nameOfPlayerClosestToAverage(List<Player> players) {
		Double average = averageAgeOfPlayers(players);
		return players.stream().min(comparing(player -> Math.abs(player.getAge() - average))).get().getName();
	}


	static List<Player> playersWithNamesStartingWith(String start, List<Player> players) {
		return players.stream().filter(player -> player.name.startsWith(start)).collect(toList());
	}

	static Double sumValueOfAllTeams(List<Team> teams) {
		return teams.stream().collect(summingDouble(Team::getValue));
	}

	static Optional<Player> youngestPlayer(List<Player> players) {
		return players.stream().reduce(Lambda.youngestPlayerLambda);
	}

	static Map<Integer, List<Player>> groupPlayersByBirthYear(List<Player> players) {
		return players.stream().collect(groupingBy(player -> player.getBirthday().getYear(), mapping(p -> p, toList())));
	}

	static Optional<Team> teamWithHighestValue(final List<Team> teams) {
		return teams.stream().reduce(Lambda.highestTeamValueLambda);
	}


	static List<Double> sumValuesOfEachLeague(final List<League> leagues) {
		return leagues.stream()
				.map(league -> league.getTeams())
				.map(teams -> teams.stream()
						.map(Team::getValue)
						.reduce(0.0, (x, y) -> x + y))
				.collect(toList());
	}

	static List<Player> playersOlderThan(final LocalDate minAge, final List<League> leagues) {
		return leagues.stream()
				.map(League::getTeams)
				.reduce(new ArrayList<>(), LambdaWithStreams::combine).stream()
				.map(Team::getPlayers)
				.reduce(new ArrayList<>(), LambdaWithStreams::combine).stream()
				.filter(player -> player.birthday.isBefore(minAge))
				.collect(toList());
	}

	private static <T> List<T> combine(final List<T> a, final List<T> b) {
		List<T> result = a;
		result.addAll(b);
		return result;
	}
}
