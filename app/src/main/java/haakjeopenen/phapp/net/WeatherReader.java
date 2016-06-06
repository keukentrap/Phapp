package haakjeopenen.phapp.net;

import android.os.NetworkOnMainThreadException;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import haakjeopenen.phapp.fragments.WeatherFragment;

/**
 * Created by U on 6-6-2016.
 */
public class WeatherReader {
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;

	private ArrayList<String> stations;
	NodeList stationnodes;

	private API api;

	private WeatherFragment weatherfragment;

	final int node = 1;

	public WeatherReader(WeatherFragment weatherfragment)
	{
		this.weatherfragment = weatherfragment;
		try
		{
			factory = DocumentBuilderFactory.newInstance();
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
	public void read()
	{
		stations = new ArrayList<String>();

		Document document = getDocument();

		api.loadBuienradar(this, builder);
	}

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
	 * Request the BuienRadar XML
	 * @return Document if success
	 */
	public Document getDocument()
	{
		/*
		try
		{
			return builder.parse("http://xml.buienradar.nl/");

		} catch (SAXException | IOException ex)
		{
			Logger.getLogger(WeatherReader.class.getName()).log(Level.SEVERE, null, ex);
		}
		*/
		return null;

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
