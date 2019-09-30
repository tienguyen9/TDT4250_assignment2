package tdt4250.conversion.util;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;

import javax.script.*;

import tdt4250.conversion.api.unit;
import tdt4250.conversion.api.UnitSearchResult;

@Component(
		configurationPid = ConvertUnits.FACTORY_PID,
		configurationPolicy = ConfigurationPolicy.REQUIRE
		)
public class ConvertUnits implements unit {
	
	public static final String FACTORY_PID = "tdt4250.unit.util.UnitsConvert";
	
	public static final String UNIT_NAME_PROP = "unitName";
	public static final String UNIT_CONVERSION_PROP = "unitConversion";
	

	private String name;
	private String conversion;
	
	@Override
	public String getUnitName() {
		return name;
	}

	protected void setUnitName(String name) {
		this.name = name;
	}
	
	@Override
	public String getUnitConversion() {
		return conversion;
	}
	
	protected void setUnitConversion(String conversion) {
		this.conversion = conversion;
	}
	
	public @interface UnitsConvertConfig {
		String unitName();
		String unitConversion() default "";
	}

	@Activate
	public void activate(BundleContext bc, UnitsConvertConfig config) {
		update(bc, config);
	}

	@Modified
	public void modify(BundleContext bc, UnitsConvertConfig config) {
		update(bc, config);		
	}
	
	protected void update(BundleContext bc, UnitsConvertConfig config) {
		setUnitName(config.unitName());
		setUnitConversion(config.unitConversion());
	}

	@Override
	public UnitSearchResult convert(String convertNumber) {
		Map<String, Object> vars = new HashMap<String, Object>();
		String result = "";
		Double numb = Double.parseDouble(convertNumber);
		try {
			vars.put("x", Double.parseDouble(convertNumber));
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
			result = String.format("%.2f", engine.eval(conversion, new SimpleBindings(vars)));
		} catch (Exception e) {
			if (e instanceof ScriptException) {
				String msg = "Could not evaluate the expression, " + conversion + ".";
				return new UnitSearchResult(false, msg, null);
			} else if (e instanceof NumberFormatException) {
				String msg = "Invalid input, " + convertNumber + ". Try again!";
				return new UnitSearchResult(false, msg, null);
			}	
		} 
		return new UnitSearchResult(true, String.format("%s: %.2f converted to %s", getUnitName(), numb, result), null);
	}

}