package geoquiz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import eu.hansolo.fx.world.Country;

enum Continent {
	ASIA, AFRICA, NORTH_AMERICA, SOUTH_AMERICA, EUROPE, AUSTRALIA, WORLD
};

public abstract class AbstractRegionFactory {
	abstract Region regionFrom(Continent code);
	
	protected Region makeCountry(Continent code, boolean lightweight) {
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

class RegionFactory extends AbstractRegionFactory {
	Region regionFrom(Continent code) {
		return makeCountry(code, false);
	}
}

class LightweightRegionFactory extends AbstractRegionFactory {
	Region regionFrom(Continent code) {
		return makeCountry(code, true);
	}
}

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
		handleSpecialCases(codesToNames);
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

	private void handleSpecialCases(HashMap<String, Country> codesToNames) {
		handleAsiaSpecialCases(codesToNames);
		handleAfricaSpecialCases(codesToNames);
		handleNorthAmericaSpecialCases(codesToNames);
		handleSouthAmericaSpecialCases(codesToNames);
		handleEuropeSpecialCases(codesToNames);
		handleAustraliaSpecialCases(codesToNames);
	}

	private void handleAsiaSpecialCases(HashMap<String, Country> codesToNames) {
		codesToNames.put("Abkhazia", Country.GE); // partially recognized state
		codesToNames.put("Akrotiri and Dhekelia", Country.GR); // minoan bronze age settlement 
		codesToNames.put("Cocos (Keeling) Islands", Country.CC); 
		codesToNames.put("East Timor (Timor-Leste)", Country.TL); 
		codesToNames.put("Macau", Country.CA); // autonomous territory of china
		codesToNames.put("Myanmar (Burma)", Country.MM);
		codesToNames.put("Nagorno-Karabakh", Country.AZ); // disputed territory
		codesToNames.put("Northern Cyprus", Country.CY);
		codesToNames.put("South Ossetia", Country.GE);// partially recognized state
	}

	private void handleAfricaSpecialCases(HashMap<String, Country> codesToNames) {
		codesToNames.put("Congo (Congo-Brazzaville)", Country.CG);
		codesToNames.put("Democratic Republic of the Congo (Congo-Kinshasa)", Country.CD);
		codesToNames.put("Ivory Coast (Côte d'Ivoire)", Country.CI);
		codesToNames.put("Réunion", Country.RE);
		codesToNames.put("Sahrawi Arab Democratic Republic", Country.EH);
		codesToNames.put("Saint Helena| Ascension and Tristan da Cunha", Country.SH);
		codesToNames.put("São Tomé and Príncipe", Country.ST);
		codesToNames.put("Somaliland", Country.SO); // Disputed territory
	}

	private void handleNorthAmericaSpecialCases(HashMap<String, Country> codesToNames) {
		codesToNames.put("Bonaire", Country.BQ);
		codesToNames.put("Saba", Country.BQ);
		codesToNames.put("Sint Eustatius", Country.BQ);
		codesToNames.put("Clipperton Island", Country.MX);
		codesToNames.put("Navassa Island", Country.HT);
		codesToNames.put("Saint Kitts and Nevis", Country.KN);
		codesToNames.put("Saint Pierre and Miquelon", Country.PM);
		codesToNames.put("Saint Vincent and the Grenadines", Country.VC);
		codesToNames.put("Sint Maarten", Country.SX);
		codesToNames.put("Turks and Caicos Islands", Country.TC);
	}

	private void handleSouthAmericaSpecialCases(HashMap<String, Country> codesToNames) {
		codesToNames.put("South Georgia and the South Sandwich Islands", Country.GS);
	}

	private void handleEuropeSpecialCases(HashMap<String, Country> codesToNames) {
		codesToNames.put("Isle of Man", Country.IM);
		codesToNames.put("Kosovo", Country.XK);
		codesToNames.put("Svalbard", Country.SJ);
		codesToNames.put("Transnistria", Country.MD); // recognized as part of moldova
		codesToNames.put("Åland Islands", Country.AX);
		codesToNames.put("Ã…land Islands", Country.AX);
	}

	private void handleAustraliaSpecialCases(HashMap<String, Country> codesToNames) {
		codesToNames.put("Ashmore and Cartier Islands", Country.AU);
		codesToNames.put("Coral Sea Islands", Country.AU);
		codesToNames.put("Howland Island", Country.AU);
		codesToNames.put("Jarvis Island", Country.AU);
		codesToNames.put("Johnston Atoll", Country.AU);
		codesToNames.put("Kingman Reef", Country.AU);
		codesToNames.put("Midway Atoll", Country.AU);
		codesToNames.put("Palmyra Atoll", Country.AU);
		codesToNames.put("Baker Island", Country.AU);
		codesToNames.put("Pitcairn Islands", Country.AU);
		codesToNames.put("Wake Island", Country.AU);
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