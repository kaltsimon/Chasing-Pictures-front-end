package de.fu_berlin.cdv.chasingpictures;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import de.fu_berlin.cdv.chasingpictures.api.Picture;
import de.fu_berlin.cdv.chasingpictures.api.PictureApiResult;
import de.fu_berlin.cdv.chasingpictures.api.Place;

/**
 * Provides utilities for debugging/testing.
 */
public class DebugUtilities {
    public static List<Picture> getDemoPictures() {
        try {
            return new ObjectMapper().readValue(
                    // region JSON
                    "{ \"pictures\": [\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/b\\/be\\/Panorama_von_Berlin-GDR-64-11-108.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10000,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/b\\/be\\/Panorama_von_Berlin-GDR-64-11-108.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/b\\/b7\\/Der_Ochsen-Platz-GDR-64-11-171.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10001,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/b\\/b7\\/Der_Ochsen-Platz-GDR-64-11-171.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/e\\/ea\\/Einzug_des_K\\u00f6nigs-GDR-64-11-216.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10002,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/e\\/ea\\/Einzug_des_K%C3%B6nigs-GDR-64-11-216.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/9\\/9c\\/Das_Brandenburger_Tor-GDR-65-7-1.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10003,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/9\\/9c\\/Das_Brandenburger_Tor-GDR-65-7-1.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/7\\/7b\\/Berlin_vom_Kreuzberg_aus-GDR-69-56.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10004,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/7\\/7b\\/Berlin_vom_Kreuzberg_aus-GDR-69-56.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/8\\/89\\/Mauerstrasse-Dreifaltigkeitskirche-GDR-71-67.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10005,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/8\\/89\\/Mauerstrasse-Dreifaltigkeitskirche-GDR-71-67.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/4\\/42\\/Das_Leipziger_Tor-GDR-74-55.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10006,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/4\\/42\\/Das_Leipziger_Tor-GDR-74-55.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/b\\/b5\\/Leipziger_Platz-GDR-76-57-24.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10007,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/b\\/b5\\/Leipziger_Platz-GDR-76-57-24.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/f\\/f2\\/Luiseninsel_Tiergarten-GDR-76-57-45.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10008,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/f\\/f2\\/Luiseninsel_Tiergarten-GDR-76-57-45.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/0\\/08\\/M\\u00e4rkisches_Museum-GE-2007-638-VF.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10009,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/0\\/08\\/M%C3%A4rkisches_Museum-GE-2007-638-VF.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/1\\/1f\\/C\\u00f6lln_Jungfernbr\\u00fccke-GHZ-64-3-13.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10010,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/1\\/1f\\/C%C3%B6lln_Jungfernbr%C3%BCcke-GHZ-64-3-13.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/c\\/c1\\/Rondell-GHZ-74-12.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10011,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/c\\/c1\\/Rondell-GHZ-74-12.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/7\\/76\\/Jerusalemkirche-GHZ-74-14.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10012,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/7\\/76\\/Jerusalemkirche-GHZ-74-14.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/1\\/10\\/Nauener_Platz-GHZ-77-13.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10013,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/1\\/10\\/Nauener_Platz-GHZ-77-13.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/6\\/62\\/Ufer_Stralauer_Strasse-GHZ-78-26.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10014,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/6\\/62\\/Ufer_Stralauer_Strasse-GHZ-78-26.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/f\\/f4\\/Landwehrkanal-GHZ-90-61.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10015,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/f\\/f4\\/Landwehrkanal-GHZ-90-61.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/4\\/4d\\/B\\u00f6rse-IV-61-1531-S.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10016,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/4\\/4d\\/B%C3%B6rse-IV-61-1531-S.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/9\\/99\\/Rathaus-IV-61-1537-S.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10017,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/9\\/99\\/Rathaus-IV-61-1537-S.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/9\\/9b\\/Landsbergerplatz-IV-61-3475-V.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10018,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/9\\/9b\\/Landsbergerplatz-IV-61-3475-V.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/0\\/04\\/Zoo_Eingang-IV-64-1294-V.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10019,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/0\\/04\\/Zoo_Eingang-IV-64-1294-V.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/0\\/04\\/Zoo_Eingang-IV-64-1294-V.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10020,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/0\\/04\\/Zoo_Eingang-IV-64-1294-V.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/9\\/9a\\/Avus_Funkturm-SM-2013-0958.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10021,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/9\\/9a\\/Avus_Funkturm-SM-2013-0958.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/1\\/15\\/Grunewaldturm-SM-2013-1297.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10022,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/1\\/15\\/Grunewaldturm-SM-2013-1297.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/d\\/dc\\/Filmfestspiele_Kudamm-SM-2013-1519.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10023,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/d\\/dc\\/Filmfestspiele_Kudamm-SM-2013-1519.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/4\\/4e\\/K\\u00f6llnischer_Fischmarkt-VII-59-29-w.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10024,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/4\\/4e\\/K%C3%B6llnischer_Fischmarkt-VII-59-29-w.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/8\\/83\\/Blick_von_Oberbaumbr\\u00fccke-VII-59-408-W.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10025,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/8\\/83\\/Blick_von_Oberbaumbr%C3%BCcke-VII-59-408-W.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/5\\/5c\\/Platz_am_Zeughaus-VII-59-513-x.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10026,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/5\\/5c\\/Platz_am_Zeughaus-VII-59-513-x.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/1\\/1f\\/K\\u00f6nigsstrasse-VII-60-1489-W.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10027,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/1\\/1f\\/K%C3%B6nigsstrasse-VII-60-1489-W.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/6\\/68\\/Schlossbr\\u00fccke-VII-62-424-a-W.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10028,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/6\\/68\\/Schlossbr%C3%BCcke-VII-62-424-a-W.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/b\\/b9\\/Hackescher_Markt-VII-67-294-W.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10029,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/b\\/b9\\/Hackescher_Markt-VII-67-294-W.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"file_content_type\": null,\n" +
                    "    \"file_file_name\": null,\n" +
                    "    \"file_file_size\": null,\n" +
                    "    \"file_updated_at\": null,\n" +
                    "    \"user_id\": null,\n" +
                    "    \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/2\\/25\\/Hamburger_Bahnhof-VII-97-341-a-W.jpg\",\n" +
                    "    \"time\": null,\n" +
                    "    \"id\": 10030,\n" +
                    "    \"asciiurl\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/2\\/25\\/Hamburger_Bahnhof-VII-97-341-a-W.jpg\",\n" +
                    "    \"created_at\": null,\n" +
                    "    \"updated_at\": 1437590292000,\n" +
                    "    \"place_id\": 0\n" +
                    "  }\n" +
                    "]}",
                    // endregion
                    PictureApiResult.class
                    ).getPictures();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Place getPlaceWithPictures() {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.readValue(
                    // region JSON
                    "{\n" +
                    "  \"id\": 6,\n" +
                    "  \"name\": \"Vue d'une partie de la Rue dite la Mauer=Straße avec l'Eglise de la Trinité. Mit Dedikation an Baron von Werder\",\n" +
                    "  \"description\": \"\",\n" +
                    "  \"created_at\": \"2015-06-09T19:27:29.763Z\",\n" +
                    "  \"updated_at\": \"2015-06-09T19:27:29.763Z\",\n" +
                    "  \"latitude\": 52.511931,\n" +
                    "  \"longitude\": 13.386628,\n" +
                    "  \"pictures\": [\n" +
                    "    [\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/b\\/be\\/Panorama_von_Berlin-GDR-64-11-108.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/b\\/b7\\/Der_Ochsen-Platz-GDR-64-11-171.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10001,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/e\\/ea\\/Einzug_des_K\\u00f6nigs-GDR-64-11-216.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10002,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/9\\/9c\\/Das_Brandenburger_Tor-GDR-65-7-1.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10003,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/7\\/7b\\/Berlin_vom_Kreuzberg_aus-GDR-69-56.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10004,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/8\\/89\\/Mauerstrasse-Dreifaltigkeitskirche-GDR-71-67.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10005,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/4\\/42\\/Das_Leipziger_Tor-GDR-74-55.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10006,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/b\\/b5\\/Leipziger_Platz-GDR-76-57-24.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10007,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/f\\/f2\\/Luiseninsel_Tiergarten-GDR-76-57-45.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10008,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/0\\/08\\/M\\u00e4rkisches_Museum-GE-2007-638-VF.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10009,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/1\\/1f\\/C\\u00f6lln_Jungfernbr\\u00fccke-GHZ-64-3-13.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10010,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/c\\/c1\\/Rondell-GHZ-74-12.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10011,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/7\\/76\\/Jerusalemkirche-GHZ-74-14.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10012,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/1\\/10\\/Nauener_Platz-GHZ-77-13.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10013,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/6\\/62\\/Ufer_Stralauer_Strasse-GHZ-78-26.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10014,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/f\\/f4\\/Landwehrkanal-GHZ-90-61.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10015,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/4\\/4d\\/B\\u00f6rse-IV-61-1531-S.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10016,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/9\\/99\\/Rathaus-IV-61-1537-S.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10017,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/9\\/9b\\/Landsbergerplatz-IV-61-3475-V.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10018,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/0\\/04\\/Zoo_Eingang-IV-64-1294-V.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10019,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 64\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/0\\/04\\/Zoo_Eingang-IV-64-1294-V.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10020,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/9\\/9a\\/Avus_Funkturm-SM-2013-0958.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10021,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/1\\/15\\/Grunewaldturm-SM-2013-1297.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10022,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/d\\/dc\\/Filmfestspiele_Kudamm-SM-2013-1519.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10023,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/4\\/4e\\/K\\u00f6llnischer_Fischmarkt-VII-59-29-w.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10024,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/8\\/83\\/Blick_von_Oberbaumbr\\u00fccke-VII-59-408-W.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10025,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/5\\/5c\\/Platz_am_Zeughaus-VII-59-513-x.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10026,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/1\\/1f\\/K\\u00f6nigsstrasse-VII-60-1489-W.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10027,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/6\\/68\\/Schlossbr\\u00fccke-VII-62-424-a-W.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10028,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/b\\/b9\\/Hackescher_Markt-VII-67-294-W.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10029,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"file_content_type\": null,\n" +
                    "        \"file_file_name\": null,\n" +
                    "        \"file_file_size\": null,\n" +
                    "        \"file_updated_at\": null,\n" +
                    "        \"user_id\": null,\n" +
                    "        \"url\": \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/2\\/25\\/Hamburger_Bahnhof-VII-97-341-a-W.jpg\",\n" +
                    "        \"time\": null,\n" +
                    "        \"id\": 10030,\n" +
                    "        \"created_at\": null,\n" +
                    "        \"updated_at\": 1437590292000,\n" +
                    "        \"place_id\": 6\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  ]\n" +
                    "}",
                    // endregion
                    Place.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
