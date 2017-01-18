package geoquiz;

import static eu.hansolo.fx.world.Country.*;

import eu.hansolo.fx.world.Country;

public class RegionFactory {
	static Region regionFrom(String code) {
		if (code.equals("AS")) {
			return new Asia(false);
		}
		if (code.equals("AF")) {
			return new Africa(false);
		}
		if (code.equals("NA")) {
			return new NorthAmerica(false);
		}
		if (code.equals("SA")) {
			return new SouthAmerica(false);
		}
		if (code.equals("EU")) {
			return new Europe(false);
		}
		if (code.equals("AU")) {
			return new Australia(false);
		}
		if (code.equals("WORLD")) {
			return new World(false);
		}
		return null;
	}
}

class Region {
	IQuizCountry[] quizCountries;
	boolean useLightWeight;
	
	public Region(boolean useLightWeight, Country... countries) {
		this.useLightWeight = useLightWeight;
		quizCountries = new IQuizCountry[countries.length];
		int i = 0;
		for (Country country : countries) {
			quizCountries[i++] = assignQuizCountry(country);
		}
	}

	private IQuizCountry assignQuizCountry(Country country) {
		return useLightWeight ? new QuizCountry(country) : new PictorialQuizCountry(country);
	}
}

class Asia extends Region {
	public Asia(boolean useLightWeight) {
		super(useLightWeight, RU);
	}
}

class Africa extends Region {
	public Africa(boolean useLightWeight) {
		super(useLightWeight, MA, DZ, TN, LY, EG, SD, TD, NE, ML, MR, 
							  SN, GW, GN, SL, LR, CI, BF, GH, TG, BJ, 
							  NG, CM, CF, SS, ET, SO, KE, UG, CD, CG, 
							  GA, GQ, AO, ZM, TZ, MZ, MW, MG, MZ, ZW, 
							  ZM, AO, NA, BW, ZA, LS, SZ, KM, RE, MU,
							  RW, BI);
	}
}

class NorthAmerica extends Region {
	public NorthAmerica(boolean useLightWeight) {
		super(useLightWeight, US, CA);
	}
}

class SouthAmerica extends Region {
	public SouthAmerica(boolean useLightWeight) {
		super(useLightWeight, BR);
	}
	
}

class Europe extends Region {
	public Europe(boolean useLightWeight) {
		super(useLightWeight, GB, IE);
	}
}

class Australia extends Region {
	public Australia(boolean useLightWeight) {
		super(useLightWeight, AU, NZ);
	}
}

class World extends Region {
	public World(boolean useLightWeight) {
		super(useLightWeight, Country.values());
	}
}