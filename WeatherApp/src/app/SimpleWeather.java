package app;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import org.json.JSONObject;

public class SimpleWeather {

    static String[] countries = {
        "Bangladesh", "Qatar", "UAE", "Kuwait"
    };

    static String[] bdDistricts = {
        "Dhaka","Faridpur","Gazipur","Gopalganj","Kishoreganj","Madaripur","Manikganj","Munshiganj","Narayanganj","Narsingdi","Rajbari","Shariatpur","Tangail",
        "Bogra","Joypurhat","Naogaon","Natore","Nawabganj","Pabna","Rajshahi","Sirajganj",
        "Dinajpur","Gaibandha","Kurigram","Lalmonirhat","Nilphamari","Panchagarh","Rangpur","Thakurgaon",
        "Barguna","Barisal","Bhola","Jhalokathi","Patuakhali","Pirojpur",
        "Bandarban","Brahmanbaria","Chandpur","Chattogram","Cumilla","Cox's Bazar","Feni","Khagrachhari","Lakshmipur","Noakhali","Rangamati",
        "Habiganj","Maulvibazar","Sunamganj","Sylhet",
        "Bagerhat","Chuadanga","Jashore","Jhenaidah","Khulna","Kushtia","Magura","Meherpur","Narail","Satkhira"
    };

    static String[] qatarRegions = { "Doha","Al Rayyan","Al Wakrah","Umm Salal","Al Khor","Al Shamal","Al Daayen","Al Shahaniya" };
    static String[] uaeRegions = { "Abu Dhabi","Dubai","Sharjah","Ajman","Umm Al Quwain","Ras Al Khaimah","Fujairah" };
    static String[] kwRegions = { "Al Asimah","Hawalli","Al Farwaniyah","Mubarak Al-Kabeer","Al Jahra","Al Ahmadi" };

    static String API_KEY = "4a583ee6fd5dee00a94454a75845f26f";

    public static void main(String[] args) {

        JFrame frame = new JFrame("Weather App");
        frame.setSize(450, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        Color pastelGreen = new Color(204, 255, 204);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(pastelGreen);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.anchor = GridBagConstraints.WEST;

        // LABEL + BOX side by side
        JLabel countryLabel = new JLabel("Select Country:");
        JComboBox<String> countryBox = new JComboBox<>(countries);
        countryBox.setPreferredSize(new Dimension(150, 25));

        JLabel regionLabel = new JLabel("Select Region:");
        JComboBox<String> regionBox = new JComboBox<>();
        regionBox.setPreferredSize(new Dimension(150, 25));

        JButton btn = new JButton("Show Weather");
        btn.setPreferredSize(new Dimension(130, 30));

        JLabel resultLabel = new JLabel("Weather will show here.");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Row 1: country label
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(countryLabel, gbc);

        // Row 1: country box beside label
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(countryBox, gbc);

        // Row 2: region label
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(regionLabel, gbc);

        // Row 2: region box
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(regionBox, gbc);

        // Row 3: button centered
        gbc.gridx = 0; gbc.gridy = 2; 
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btn, gbc);

        // Row 4: result label with spacing
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20,10,10,10); // more space above
        panel.add(resultLabel, gbc);

        frame.add(panel);

        // LOAD REGIONS
        countryBox.addActionListener(e -> {
            loadRegions(regionBox, (String) countryBox.getSelectedItem());
        });

        loadRegions(regionBox, countries[0]);

        btn.addActionListener(e -> {
            String country = (String) countryBox.getSelectedItem();
            String region = (String) regionBox.getSelectedItem();

            if (region == null) {
                resultLabel.setText("Please select a region!");
                return;
            }

            try {
                String query = region + "," + country;
                String apiUrl =
                    "https://api.openweathermap.org/data/2.5/weather?q=" +
                    URLEncoder.encode(query, "UTF-8") +
                    "&appid=" + API_KEY + "&units=metric";

                String response = readURL(apiUrl);
                JSONObject obj = new JSONObject(response);

                String weather = obj.getJSONArray("weather").getJSONObject(0).getString("description");
                double temp = obj.getJSONObject("main").getDouble("temp");

                resultLabel.setText("Weather in " + region + ": " + temp + "°C, " + weather);

            } catch (Exception ex) {
                resultLabel.setText("Could not fetch weather.");
            }
        });

        frame.setVisible(true);
    }

    public static void loadRegions(JComboBox<String> box, String country) {
        box.removeAllItems();

        if (country.equals("Bangladesh"))
            for (String d : bdDistricts) box.addItem(d);
        else if (country.equals("Qatar"))
            for (String r : qatarRegions) box.addItem(r);
        else if (country.equals("UAE"))
            for (String r : uaeRegions) box.addItem(r);
        else if (country.equals("Kuwait"))
            for (String r : kwRegions) box.addItem(r);
    }

    public static String readURL(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();
        return sb.toString();
    }
}
