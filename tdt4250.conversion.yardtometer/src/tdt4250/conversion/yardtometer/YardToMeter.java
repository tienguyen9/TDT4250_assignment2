package tdt4250.conversion.yardtometer;


import org.osgi.service.component.annotations.Component;

import tdt4250.conversion.api.unit;
import tdt4250.conversion.util.ConvertUnits;

@Component(
		property = {
				ConvertUnits.UNIT_NAME_PROP + "=yardtometer",
				ConvertUnits.UNIT_CONVERSION_PROP + "=x*0.9144"}
		)
public class YardToMeter extends ConvertUnits implements unit {
}

