package MyPackage;
import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect("index.html");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String inputData = request.getParameter("userInput");
	    String apiKey = "bc25ea1b9cffc429704746ab68c5a9d5";
	    String city = request.getParameter("city");
	    String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

	    try {
	        // API INTEGRATION
	        URL url = new URL(apiUrl);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");
	        // READING DATA FROM THE NETWORK
	        InputStream is = con.getInputStream();
	        InputStreamReader read = new InputStreamReader(is);
	        Scanner sc = new Scanner(read);
	        // would store string
	        StringBuilder responseContent = new StringBuilder();

	        while (sc.hasNext()) {
	            responseContent.append(sc.nextLine());
	        }
	        sc.close();
	        Gson g = new Gson();
	        JsonObject jsonObject = g.fromJson(responseContent.toString(), JsonObject.class);

	        // Extracting data from JSON
	        long dateTimeStamp = jsonObject.get("dt").getAsLong() * 1000;
	        String date = new Date(dateTimeStamp).toString();

	        double tempKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
	        int tempCelsius = (int) (tempKelvin - 273.15);

	        int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();

	        double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();

	        String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();

	        // You can do something with the extracted data here
	        // For example, you can set attributes in the request and forward it to a JSP for rendering.
	        request.setAttribute("date", date);
	        request.setAttribute("city", city);
	        request.setAttribute("tempCelsius", tempCelsius);
	        request.setAttribute("humidity", humidity);
	        request.setAttribute("windSpeed", windSpeed);
	        request.setAttribute("weatherCondition", weatherCondition);
	        request.setAttribute("weatherData", responseContent.toString());
	        // Forward the request to a JSP for rendering
	        request.getRequestDispatcher("index.jsp").forward(request, response);

	    } catch (Exception e) {
	        // Proper error handling
	        e.printStackTrace();
	        // You may want to handle this error more gracefully, like returning an error response.
	    }
	}


}
