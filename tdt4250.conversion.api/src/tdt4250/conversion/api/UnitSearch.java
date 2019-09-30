package tdt4250.conversion.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class UnitSearch {

	private static final String DEFAULT_MESSAGE = "Sorry, no conversions";
	private Collection<unit> conversions = new ArrayList<unit>();
	
	public void addConversion(unit unit) {
		conversions.add(unit);
	}

	public void removeConversion(unit unit) {
		conversions.remove(unit);
	}
	
	public UnitSearch(unit... units) {
		conversions.addAll(Arrays.asList(units));
	}
	
	private UnitSearchResult search(String searchKey, Iterable<unit> conversions) {
		StringBuilder messages = new StringBuilder();
		URI link = null;
		boolean success = false;
		for (unit unit : conversions) {
			UnitSearchResult result = unit.convert(searchKey);
			if (result.isSuccess()) {
				messages.append(result.getMessage());
				success = true;
				if (link == null) {
					link = result.getLink();
				}
				break;
			} else {
				messages.append(result.getMessage());
			}
		}
		if (messages.length() == 0) {
			messages.append(DEFAULT_MESSAGE);
		}
		return new UnitSearchResult(success, messages.toString(), link);
	}

	public UnitSearchResult search(String unitKey, String searchKey) {
		return search(searchKey, conversions.stream().filter(unit -> unit.getUnitName().equals(unitKey)).collect(Collectors.toList()));
	}
}