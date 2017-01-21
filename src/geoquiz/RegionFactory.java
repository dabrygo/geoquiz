package geoquiz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import eu.hansolo.fx.world.Country;

public class RegionFactory {
	static Region regionFrom(Continent code, boolean lightweight) {
		switch (code) {
		case ASIA:
			return new Asia(lightweight);
		case AFRICA:
			return new Africa(lightweight);
		case NORTH_AMERICA:
			return new NorthAmerica(lightweight);
		case SOUTH_AMERICA:
			return new SouthAmerica(lightweight);
		case EUROPE:
			return new Europe(lightweight);
		case AUSTRALIA:
			return new Australia(lightweight);
		case WORLD:
			return new World(lightweight);
		default:
			return null;
		}
	}
}

enum Continent {
	ASIA, AFRICA, NORTH_AMERICA, SOUTH_AMERICA, EUROPE, AUSTRALIA, WORLD
};

abstract class Region extends ArrayList<IQuizCountry> {
	private static final long serialVersionUID = 1L;
	boolean useLightWeight;

	public Region(boolean useLightWeight, Country... countries) {
		this.useLightWeight = useLightWeight;
		for (Country country : countries) {
			add(assignQuizCountry(country));
		}
	}
	
	public Region(boolean useLightWeight) {
		this.useLightWeight = useLightWeight;
		assignCountries();
	}

	private IQuizCountry assignQuizCountry(Country country) {
		return useLightWeight ? new QuizCountry(country) : new PictorialQuizCountry(country);
	}
	
	private void assignCountries() {
		HashMap<String, Country> codesToNames = assignCountriesToNames();
		try {
			String line;
			BufferedReader reader = new BufferedReader(new FileReader(Paths.get("rsc", "geoquiz", getFileName() + ".txt").toFile()));
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",");	
				if (codesToNames.containsKey(data[0])) {
					add(assignQuizCountry(codesToNames.get(data[0])));
				}
				else {
					System.err.println("Couldn't find country for " + data[0]);
				}
			}
			reader.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private HashMap<String, Country> assignCountriesToNames() {
		HashMap<String, Country> codesToNames = new HashMap<>();
		for (Country country : Country.values()) {
			codesToNames.put(new Locale("", country.name()).getDisplayCountry(), country);
		}
		return codesToNames;
	}
	
	private String getFileName() {
		switch(this.getClass().getSimpleName()) {
		case "Asia": return Continent.ASIA.name();
		case "Africa": return Continent.AFRICA.name();
		case "NorthAmerica": return Continent.NORTH_AMERICA.name();
		case "SouthAmerica": return Continent.SOUTH_AMERICA.name();
		case "Europe": return Continent.EUROPE.name();
		case "Australia": return Continent.AUSTRALIA.name();
		case "World": 
		default: 
			throw new UnsupportedOperationException("Not yet implemented");
		}
	}
}

class Asia extends Region {
	private static final long serialVersionUID = 1L;

	public Asia(boolean useLightWeight) {
		super(useLightWeight);
	}
}

class Africa extends Region {
	private static final long serialVersionUID = 1L;

	public Africa(boolean useLightWeight) {
		super(useLightWeight);
	}
}

class NorthAmerica extends Region {
	private static final long serialVersionUID = 1L;

	public NorthAmerica(boolean useLightWeight) {
		super(useLightWeight);
	}
}

class SouthAmerica extends Region {
	private static final long serialVersionUID = 1L;

	public SouthAmerica(boolean useLightWeight) {
		super(useLightWeight);
	}

}

class Europe extends Region {
	private static final long serialVersionUID = 1L;

	public Europe(boolean useLightWeight) {
		super(useLightWeight);
	}
}

class Australia extends Region {
	private static final long serialVersionUID = 1L;

	public Australia(boolean useLightWeight) {
		super(useLightWeight);
	}
}

class World extends Region {
	private static final long serialVersionUID = 1L;

	public World(boolean useLightWeight) {
		super(useLightWeight, Country.values());
	}
}