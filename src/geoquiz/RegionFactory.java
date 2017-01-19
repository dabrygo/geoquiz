package geoquiz;

import static eu.hansolo.fx.world.Country.*;

import java.util.ArrayList;

import eu.hansolo.fx.world.Country;

public class RegionFactory {
	static Region regionFrom(Continent code) {
		switch (code) {
		case ASIA:
			return new Asia(false);
		case AFRICA:
			return new Africa(false);
		case NORTH_AMERICA:
			return new NorthAmerica(false);
		case SOUTH_AMERICA:
			return new SouthAmerica(false);
		case EUROPE:
			return new Europe(false);
		case AUSTRALIA:
			return new Australia(false);
		case WORLD:
			return new World(false);
		default:
			return null;
		}
	}
}

enum Continent {
	ASIA, AFRICA, NORTH_AMERICA, SOUTH_AMERICA, EUROPE, AUSTRALIA, WORLD
};

class Region extends ArrayList<IQuizCountry> {
	private static final long serialVersionUID = 1L;
	boolean useLightWeight;

	public Region(boolean useLightWeight, Country... countries) {
		this.useLightWeight = useLightWeight;
		for (Country country : countries) {
			add(assignQuizCountry(country));
		}
	}

	private IQuizCountry assignQuizCountry(Country country) {
		return useLightWeight ? new QuizCountry(country) : new PictorialQuizCountry(country);
	}
}

class Asia extends Region {
	private static final long serialVersionUID = 1L;

	public Asia(boolean useLightWeight) {
		super(useLightWeight, RU);
	}
}

class Africa extends Region {
	private static final long serialVersionUID = 1L;

	public Africa(boolean useLightWeight) {
		super(useLightWeight, MA, DZ, TN, LY, EG, SD, TD, NE, ML, MR, SN, GW, GN, SL, LR, CI, BF, GH, TG, BJ, NG, CM,
				CF, SS, ET, SO, KE, UG, CD, CG, GA, GQ, AO, ZM, TZ, MZ, MW, MG, MZ, ZW, ZM, AO, NA, BW, ZA, LS, SZ, KM,
				RE, MU, RW, BI);
	}
}

class NorthAmerica extends Region {
	private static final long serialVersionUID = 1L;

	public NorthAmerica(boolean useLightWeight) {
		super(useLightWeight, US, CA);
	}
}

class SouthAmerica extends Region {
	private static final long serialVersionUID = 1L;

	public SouthAmerica(boolean useLightWeight) {
		super(useLightWeight, BR);
	}

}

class Europe extends Region {
	private static final long serialVersionUID = 1L;

	public Europe(boolean useLightWeight) {
		super(useLightWeight, GB, IE);
	}
}

class Australia extends Region {
	private static final long serialVersionUID = 1L;

	public Australia(boolean useLightWeight) {
		super(useLightWeight, AU, NZ);
	}
}

class World extends Region {
	private static final long serialVersionUID = 1L;

	public World(boolean useLightWeight) {
		super(useLightWeight, Country.values());
	}
}