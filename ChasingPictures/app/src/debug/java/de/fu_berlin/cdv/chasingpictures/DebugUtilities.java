package de.fu_berlin.cdv.chasingpictures;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.fu_berlin.cdv.chasingpictures.api.Picture;

/**
 * Provides utilities for debugging/testing.
 */
public class DebugUtilities {
    public static List<Picture> getDemoPictures() {
        long i = 10000L;
        Picture p;
        List<Picture> pictures = new ArrayList<>();
        Date date = new GregorianCalendar(2015, 6, 22, 14, 38, 12).getTime();

        //region Create pictures
        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/b/be/Panorama_von_Berlin-GDR-64-11-108.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/b/b7/Der_Ochsen-Platz-GDR-64-11-171.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/e/ea/Einzug_des_Königs-GDR-64-11-216.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/9/9c/Das_Brandenburger_Tor-GDR-65-7-1.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/7/7b/Berlin_vom_Kreuzberg_aus-GDR-69-56.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/8/89/Mauerstrasse-Dreifaltigkeitskirche-GDR-71-67.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/4/42/Das_Leipziger_Tor-GDR-74-55.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/b/b5/Leipziger_Platz-GDR-76-57-24.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/f/f2/Luiseninsel_Tiergarten-GDR-76-57-45.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/0/08/Märkisches_Museum-GE-2007-638-VF.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/1/1f/Cölln_Jungfernbrücke-GHZ-64-3-13.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/c/c1/Rondell-GHZ-74-12.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/7/76/Jerusalemkirche-GHZ-74-14.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/1/10/Nauener_Platz-GHZ-77-13.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/6/62/Ufer_Stralauer_Strasse-GHZ-78-26.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/f/f4/Landwehrkanal-GHZ-90-61.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/4/4d/Börse-IV-61-1531-S.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/9/99/Rathaus-IV-61-1537-S.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/9/9b/Landsbergerplatz-IV-61-3475-V.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/0/04/Zoo_Eingang-IV-64-1294-V.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/0/04/Zoo_Eingang-IV-64-1294-V.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/9/9a/Avus_Funkturm-SM-2013-0958.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/1/15/Grunewaldturm-SM-2013-1297.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/d/dc/Filmfestspiele_Kudamm-SM-2013-1519.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/4/4e/Köllnischer_Fischmarkt-VII-59-29-w.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/8/83/Blick_von_Oberbaumbrücke-VII-59-408-W.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/5/5c/Platz_am_Zeughaus-VII-59-513-x.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/1/1f/Königsstrasse-VII-60-1489-W.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/6/68/Schlossbrücke-VII-62-424-a-W.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/b/b9/Hackescher_Markt-VII-67-294-W.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        i++;

        p = new Picture();
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/2/25/Hamburger_Bahnhof-VII-97-341-a-W.jpg");
        p.setId(i);
        p.setUpdatedAt(date);
        pictures.add(p);
        //endregion

        return pictures;
    }
}
