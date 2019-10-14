package com.infodart.apiconsumer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jayway.restassured.path.xml.XmlPath;

public class SoapApi {
public static void main(String[] args) throws Exception {
	SoapApi.getMethod();
	
}
	public static void getMethod() throws Exception{
		
		File soapRequestFile=new File("src/request1.xml");
		CloseableHttpClient client=HttpClients.createDefault();
		HttpPost request=new HttpPost("https://121.244.54.173:9006/MobileAppWebServices/AllStoresLocationInfoWS");
		request.addHeader("Content-Type", "text/xml");
		request.setEntity(new InputStreamEntity(new FileInputStream(soapRequestFile)));
		
		CloseableHttpResponse response=client.execute(request);
		int statusCode=response.getStatusLine().getStatusCode();
		System.out.println(statusCode);
		String responseString=EntityUtils.toString(response.getEntity());
		System.out.println(responseString);
		XmlPath path=new XmlPath(responseString);
		FileWriter fw=new FileWriter("src/test.xml");    
        fw.write(responseString);    
        fw.close();    
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new File("src/test.xml"));
		 
		//Normalize the XML Structure; It's just too important !!
		document.getDocumentElement().normalize();
		 
		//Here comes the root node
		Element root = document.getDocumentElement();
		System.out.println(root.getNodeName());
		 
		//Get all employees
		NodeList nList = document.getElementsByTagName("districtList");
		System.out.println(nList+"============================");
		for (int temp = 0; temp < nList.getLength(); temp++)
		{
		 Node node = nList.item(temp);
		 System.out.println("");    //Just a separator
		 if (node.getNodeType() == Node.ELEMENT_NODE)
		 {
		    //Print each employee's detail
		    Element eElement = (Element) node;
		    System.out.println("Employee id : "    + eElement.getElementsByTagName("district").item(0).getTextContent());
		    System.out.println("First Name : "  + eElement.getElementsByTagName("districtName").item(0).getTextContent());
		    System.out.println("Last Name : "   + eElement.getElementsByTagName("storeName").item(0).getTextContent());
		    //System.out.println("Location : "    + eElement.getElementsByTagName("location").item(0).getTextContent());
		 }
		}
	}

}
