package haakjeopenen.phapp.net;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import haakjeopenen.phapp.fragments.WeatherFragment;

/**
 * Interprets the data from buienradar
 */
public class WeatherReader {
	private DocumentBuilder builder;

	private ArrayList<String> stations;
	private NodeList stationnodes;

	private API api;

	private final WeatherFragment weatherfragment;

	private final int node = 1;

	public WeatherReader(WeatherFragment weatherfragment)
	{
		this.weatherfragment = weatherfragment;
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			api = API.getInstance(null);

			read();
		} catch (ParserConfigurationException ex) {
			Logger.getLogger(WeatherReader.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Load all the info
	 */
	private void read()
	{
		stations = new ArrayList<>();

		api.loadBuienradar(this, builder);
	}

	/**
	 * Called as soon as a response is gotten, so it can be interpreted and loaded further
	 * @param document
	 */
	public void doneLoading(Document document)
	{
		stationnodes = document.getElementsByTagName("weerstation");
		for (int i = 0; i < stationnodes.getLength(); i++)
		{
			stations.add(((Element) stationnodes.item(i).getChildNodes().item(1)).getAttribute("regio"));
		}

		weatherfragment.doneLoading();
	}

	/**
	 * Get the temperature for the selected city
	 *
	 * @return temperature
	 */
	public String getTemp()
	{
		return getInfoNode("temperatuur10cm");
	}

	/**
	 * Get the wind direction for the selected city
	 *
	 * @return wind direction
	 */
	public String getWind()
	{
		return getInfoNode("windrichting");
	}

	/**
	 * Get the humidity for the selected city
	 *
	 * @return humidity in %
	 */
	public String getHumidity()
	{
		return getInfoNode("luchtvochtigheid");
	}

	/**
	 * Get the wind speed for the selected city
	 *
	 * @return wind speed in m/s
	 */
	public String getWindspeedMS()
	{
		return getInfoNode("windsnelheidMS");
	}

	/**
	 * Get the precipitation for the selected city
	 *
	 * @return precipitation in mm/h
	 */
	public String getPrecipitation()
	{
		return getInfoNode("regenMMPU");
	}

	/**
	 * Get the image representing the weather for the selected city
	 *
	 * @return string of image URL
	 */
	public String getImage()
	{
		return getInfoNode("icoonactueel");
	}

	/**
	 * Get the value of the node with name tag for the selected city
	 * @param tag the name of the node
	 * @return value of said node
	 */
	private String getInfoNode(String tag)
	{
		NodeList wa = stationnodes.item(node).getChildNodes();

		for (int n=0;n < wa.getLength();n++){
			if (wa.item(n).getNodeName().equalsIgnoreCase(tag)) {
				return wa.item(n).getFirstChild().getNodeValue();
			}
		}
		return null;
	}
}
