//    jDownloader - Downloadmanager
//    Copyright (C) 2008  JD-Team jdownloader@freenet.de
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jd.plugins.host;

//requests RMEIUM:
//#1  (Linkinfos holen, serverID etc
//POST /files/36717968/Cash_Flow_Bir_Anlik_Hata__Bandrollu__AraFura.zip HTTP/1.1
//Host: rs102.rapidshare.com
//User-Agent: Mozilla/5.0 (Windows; U; Windows NT 6.0; de; rv:1.8.1.6) Gecko/20070725 Firefox/2.0.0.6
//Accept: application/x-shockwave-flash,text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5
//Accept-Language: de-de,de;q=0.8,en-us;q=0.5,en;q=0.3
//Accept-Encoding: gzip,deflate
//Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7
//Keep-Alive: 300
//Connection: keep-alive
//Referer: http://rapidshare.com/files/36717968/Cash_Flow_Bir_Anlik_Hata__Bandrollu__AraFura.zip
//Content-Type: application/x-www-form-urlencoded
//Content-Length: 16
//#2  ((Kann man umgehen)  Hier kann man sich den cookie holen, denn amn dirch eine rawURlEncode Funktion aber nachstellen kann
//POST /cgi-bin/premium.cgi HTTP/1.1
//Host: rs102.rapidshare.com
//User-Agent: Mozilla/5.0 (Windows; U; Windows NT 6.0; de; rv:1.8.1.6) Gecko/20070725 Firefox/2.0.0.6
//Accept: application/x-shockwave-flash,text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5
//Accept-Language: de-de,de;q=0.8,en-us;q=0.5,en;q=0.3
//Accept-Encoding: gzip,deflate
//Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7
//Keep-Alive: 300
//Connection: keep-alive
//Referer: http://rs102.rapidshare.com/files/36717968/Cash_Flow_Bir_Anlik_Hata__Bandrollu__AraFura.zip
//Content-Type: application/x-www-form-urlencoded
//Content-Length: 137
//#3 $p ist das passwort 2 mal rawurlencodet. dieser schritt führt zur serverliste
//POST /files/36717968/Cash_Flow_Bir_Anlik_Hata__Bandrollu__AraFura.zip HTTP/1.1
//Host: rs102.rapidshare.com
//User-Agent: Mozilla/5.0 (Windows; U; Windows NT 6.0; de; rv:1.8.1.6) Gecko/20070725 Firefox/2.0.0.6
//Accept: application/x-shockwave-flash,text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5
//Accept-Language: de-de,de;q=0.8,en-us;q=0.5,en;q=0.3
//Accept-Encoding: gzip,deflate
//Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7
//Keep-Alive: 300
//Connection: keep-alive
//Referer: http://rs102.rapidshare.com/cgi-bin/premium.cgi
//Cookie: user=******-**********
//Content-Type: application/x-www-form-urlencoded
//Content-Length: 119
//
//l=********&p=***********&dl.start=Download+Cash_Flow_Bir_Anlik_Hata__Bandrollu__AraFura.zip
//#4  entgültoger download
//GET /files/36717968/dl/Cash_Flow_Bir_Anlik_Hata__Bandrollu__AraFura.zip HTTP/1.1
//Host: rs102tl2.rapidshare.com
//User-Agent: Mozilla/5.0 (Windows; U; Windows NT 6.0; de; rv:1.8.1.6) Gecko/20070725 Firefox/2.0.0.6
//Accept: application/x-shockwave-flash,text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5
//Accept-Language: de-de,de;q=0.8,en-us;q=0.5,en;q=0.3
//Accept-Encoding: gzip,deflate
//Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7
//Keep-Alive: 300
//Connection: keep-alive
//Referer: http://rs102.rapidshare.com/files/36717968/Cash_Flow_Bir_Anlik_Hata__Bandrollu__AraFura.zip
//Cookie: user=****-*******
//premiumlogin=1&fileid=36717968&filename=Cash_Flow_Bir_Anlik_Hata__Bandrollu__AraFura.zip&serverid=102&accountid=******&password=******
//dl.start=PREMIUM
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import jd.captcha.CES;
import jd.config.ConfigContainer;
import jd.config.ConfigEntry;
import jd.config.Configuration;
import jd.config.MenuItem;
import jd.controlling.ProgressController;
import jd.event.ControlEvent;
import jd.plugins.DownloadLink;
import jd.plugins.HTTPConnection;
import jd.plugins.Plugin;
import jd.plugins.PluginForHost;
import jd.plugins.PluginStep;
import jd.plugins.Regexp;
import jd.plugins.RequestInfo;
import jd.plugins.download.RAFDownload;
import jd.utils.CESClient;
import jd.utils.JDLocale;
import jd.utils.JDUtilities;

public class Rapidshare extends PluginForHost {
    static private final String host = "rapidshare.com";

    private String version = "1.3.0.1";

    // http://(?:[^.]*\.)*rapidshare\.com/files/[0-9]*/[^\s"]+
    // private String botHash = "63d572beae06a841c23b0d824ac1bfe2"; //
    // "dab07d2b7f1299f762454cda4c6143e7";

    /**
     * Vereinfachte Patternerstellung: [*] optionaler Platzhalter [+] musthav
     * platzhalter
     */
    // http://rapidshare.com/files/62495619/toca3.lst
    static private final Pattern patternSupported = Pattern.compile("http://[\\w\\.]*?rapidshare\\.com/files/[\\d]{3,9}/.*", Pattern.CASE_INSENSITIVE);

    /**
     * Das findet die Ziel URL für den Post
     */
    private Pattern patternForNewHost = Pattern.compile("<form *action *= *\"([^\\n\"]*)\"");

    /**
     * Das findet die Captcha URL <form *name *= *"dl" (?s).*<img *src *=
     * *"([^\n"]*)">
     */
    private Pattern patternForCaptcha = Pattern.compile("<form *name *= *\"dlf\" (?s).*<img *src *= *\"([^\\n\"]*\\.jpg)\">");

    /**
     * <form name="dl".* action="([^\n"]*)"(?s).*?<input type="submit"
     * name="actionstring" value="[^\n"]*"
     */
    // private Pattern patternForFormData = Pattern.compile("<form name=\"dl\".*
    // action=\"([^\\n\"]*)\"(?s).*?<input type=\"submit\" name=\"actionstring\"
    // value=\"([^\\n\"]*)\"");
    // private Pattern patternForFormData =
    // Pattern.compile("document.dl.action=\'([^\\n\"]*)\"(?s).*?\';document.dl.actionstring.value=\'([^\\n\"]*)\'");
    // private String dataPattern=
    // "document.dl.action=\'°\';document.dl.actionstring.value=\'°\'\">°<br></td></tr></table><h3>Kein
    // Premium-User. Bitte<br>'°'<img src=°><br>hier eingeben: <input
    // type=\"text\" name=\"accesscode\" °size=\"5\" maxlength=\"4\"> <input
    // type=\"submit\" name=\"actionstring\" value=\"°\"></h3></form>";
    private String dataPatternPost = "<form name=\"dlf\" action=\"°\" method=\"post\""; // "document.dl.action=°document.dl.actionstring.value";

    // private String dataPatternAction = "name=\"actionstring\"
    // value=\"°\"></h3></form>";

    /**
     * Pattern trifft zu wenn die "Ihre Ip läd gerade eine datei " Seite kommt
     */

    private String patternForAlreadyDownloading = "bereits eine Datei runter";

    /**
     * Muss static bleiben!!!. Das Rapidshare Plugin merkt sich so, dass es
     * gerade wartezeit hat. Überflüssige
     */
    private static long END_OF_DOWNLOAD_LIMIT = 0;

    private static final String captchaWrong = "Access code wrong";

    /**
     * Wenn Rapidshare ihre happyhour hat
     */
    private static final String happyhour = "RapidShare Happy Hours";
    private boolean happyhourboolean = false;

    /**
     * s Das DownloadLimit wurde erreicht (?s)Downloadlimit.*Oder warte ([0-9]+)
     */
    private String patternErrorDownloadLimitReached = "Oder warte ° Minute";

    // private Pattern patternErrorCaptchaWrong = Pattern.compile("(zugriffscode
    // falsch|code wrong)", Pattern.CASE_INSENSITIVE);
    private Pattern patternErrorFileAbused = Pattern.compile("(darf nicht verteilt werden|forbidden to be shared)", Pattern.CASE_INSENSITIVE);

    private Pattern patternErrorFileNotFound = Pattern.compile("(datei nicht gefunden|file not found)", Pattern.CASE_INSENSITIVE);

    private String patternForSelectedServer = "<input checked °actionstring.value=°>°<br>";

    private String patternForServer = "<input° type=\"radio\" name=\"°\" onclick=\"document.dl.action=°http://°/files/°;document.dl.actionstring.value=°\"> °<br>";

    private String ticketWaitTimepattern = "var c=°;";

    private String ticketCodePattern = "unescape('°')}";

    // <!-- E#8 --><p>Dieses Konto ist am Mon, 2. Jun 2008 abgelaufen.
    // Verl&auml;ngern Sie jetzt Ihren Account und nutzen profitieren Sie
    // weiterhin von den Vorteilen der Premium-Mitgliedschaft.</p></p>

    private static final String PATTERN_ACCOUNT_EXPIRED = "Dieses Konto ist am Mon";

    private static final String PATTERN_ERROR_BOT = "Too many wrong codes";

    private int waitTime = 500;

    private String captchaAddress;

    private String postTarget;

    private String actionString;

    private HashMap<String, String> postParameter = new HashMap<String, String>();

    private String finalURL;

    private static HashMap<String, String> serverMap = new HashMap<String, String>();

    private static String[] serverList1;

    private String finalCookie;

    private String[] serverList2;

    private boolean hardewareError = false;

    private String ticketCode;

    private String newURL;

    private String captchaCode;

    private File captchaFile;

    private long headLength;

    private Boolean noLimitFreeInsteadPremium = false;

    private CESClient ces;

    private boolean waitingForCES;

    transient private int downloadType = -1;

    private boolean ddl = false;

    private boolean hashFound;

    private static DownloadLink freeInsteadOfPremiumDownloadlink;

    private static long freeInsteadOfPremiumStarttime = 0;

    private static long LAST_FILE_CHECK = 0;

    private static final String PROPERTY_SELECTED_SERVER = "SELECTED_SERVER";

    private static final String PROPERTY_SELECTED_SERVER2 = "SELECTED_SERVER#2";

    private static final String PROPERTY_USE_TELEKOMSERVER = "USE_TELEKOMSERVER";

    private static final String PROPERTY_USE_PRESELECTED = "USE_PRESELECTED";

    private static final String PROPERTY_USE_SSL = "USE_SSL";

    private static final String PROPERTY_WAIT_WHEN_BOT_DETECTED = "WAIT_WHEN_BOT_DETECTED";

    private static final String PROPERTY_INCREASE_TICKET = "INCREASE_TICKET";

    private static final String PROPERTY_PREMIUM_USER_2 = "PREMIUM_USER_2";

    private static final String PROPERTY_PREMIUM_PASS_2 = "PREMIUM_PASS_2";

    private static final String PROPERTY_USE_PREMIUM_2 = "USE_PREMIUM_2";

    private static final String PROPERTY_PREMIUM_USER_3 = "PREMIUM_USER_3";

    private static final String PROPERTY_PREMIUM_PASS_3 = "PREMIUM_PASS_3";

    private static final String PROPERTY_USE_PREMIUM_3 = "USE_PREMIUM_3";

    private static final String PROPERTY_FREE_IF_LIMIT_NOT_REACHED = "FREE_IF_LIMIT_NOT_REACHED";

    private static final String PATTERN_DOWNLOAD_ERRORPAGE = "RapidShare: 1-Click Webhosting";

    private static final String PATTERN_ACCOUNT_OVERLOAD = "runtergeladen und damit das Limit";

    private static final String PATTERN_ERROR_OCCURED = "<center><h2>Ein Fehler ist aufgetreten:</h2><p><font color=\"°\"><b><!-- ° -->°</b></font></p><p";
    private static final String PATTERN_ERROR_2_OCCURED = "<script>alert(\"°\")</script>";

    private static final int ACTION_TOGGLE_PREMIUM_1 = 1;

    private static final int ACTION_TOGGLE_PREMIUM_2 = 2;
    private static final int ACTION_TOGGLE_PREMIUM_3 = 3;
    private static final int ACTION_INFO_PREMIUM_1 = 4;
    private static final int ACTION_INFO_PREMIUM_2 = 5;
    private static final int ACTION_INFO_PREMIUM_3 = 6;

    private static final int ACTION_HAPPY_HOURS = 7;

    private static final String PARAM_WAIT_FOR_HAPPYHOURS = "WAIT_FOR_HAPPYHOURS";

    private static final int ACTION_HAPPY_HOURS_TOGGLE_WAIT = 8;

    private static final int ACTION_HAPPY_HOURS_FORCE_FREE = 9;

    private static final String PARAM_FORRCEFREE_WHILE_HAPPYHOURS = "FORRCEFREE_WHILE_HAPPYHOURS";

    private static final int FREE = 0;
    private static final int PREMIUM = 1;

    private static final String PATTERN_ERROR = "<!-- E#° -->°div";

    private static final int ERROR_ID_ACCOUNTEXPIRED = 8;

    private static final int ERROR_ID_FILENOTFOUND = 4;

    private static boolean HAPPYHOUR_IS_SUPPOSED = true;

    @Override
    public String getCoder() {
        return "astaldo/JD-Team";
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getPluginName() {
        return host;
    }

    @Override
    public Pattern getSupportedLinks() {
        return patternSupported;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getPluginID() {
        return host + " - " + version;
    }

    @Override
    public void init() {
        currentStep = null;
    }

    public Rapidshare() {
        super();

        // Prüfe auf Wartezeit wg downloadlimit
        steps.add(new PluginStep(PluginStep.STEP_WAIT_TIME, null));
        // Prüfe Ticket
        steps.add(new PluginStep(PluginStep.STEP_PENDING, null));
        // Serverauswahl und captchalden
        steps.add(new PluginStep(PluginStep.STEP_PAGE, null));
        // Downloads
        steps.add(new PluginStep(PluginStep.STEP_DOWNLOAD, null));

        serverMap.put("TeliaSonera #2", "tl2");// <td><input name="mirror"
        // value="tl2"
        // type="radio">TeliaSonera
        // #2</td>
        serverMap.put("TeliaSonera #3", "tl3");// <td><input name="mirror2"
        // value="tl3"
        // type="radio">TeliaSonera
        // #3</td>

        serverMap.put("GlobalCrossing", "gc"); // <td><input name="mirror"
        // value="gc"
        // type="radio">GlobalCrossing
        // #1</td>
        serverMap.put("GlobalCrossing #2", "gc2"); // <td><input name="mirror"
        // value="gc2"
        // type="radio">GlobalCrossing
        // #2</td>
        serverMap.put("Cogent", "cg"); // td><input name="mirror" value="cg"
        // type="radio">Cogent #1</td>
        serverMap.put("Cogent #2", "cg2");// <td><input name="mirror"
        // value="cg2" type="radio">Cogent
        // #2</td>
        serverMap.put("Teleglobe", "tg"); // <td><input name="mirror"
        // value="tg" type="radio">Teleglobe
        // #1</td>
        serverMap.put("Level(3)", "l3");// <td><input name="mirror" value="l3"
        // type="radio">Level(3) #1</td>
        serverMap.put("Level(3) #2", "l32");// <td><input name="mirror"
        // value="l32" type="radio">Level(3)
        // #2</td>
        serverMap.put("Level(3) #3", "l33");// <td><input name="mirror"
        // value="l33" type="radio">Level(3)
        // #3</td>

        serverMap.put("Level(3) #4", "l34");// <td><input name="mirror"
        // value="l34" type="radio">Level(3)
        // #4</td>
        serverMap.put("TeliaSonera", "tl");// <td><input name="mirror"
        // value="tl"
        // type="radio">TeliaSonera #1</td>
        serverMap.put("Deutsche Telekom", "dt");
        serverList1 = new String[] { "tl", "tl2", "gc", "gc2", "cg", "cg2", "tg", "l3", "l32", "l33", "l34", "tl", "dt" };
        serverList2 = new String[] { "tl", "tl2", "tl3", "gc", "gc2", "l32", "tg", "l3", "cg" };
        this.setConfigElements();
    }

    private String getServerFromAbbreviation(String abb) {
        Iterator<String> iter = serverMap.keySet().iterator();
        Object next;
        while (iter.hasNext()) {
            next = iter.next();
            if (serverMap.get((String) next).equals(abb)) return (String) next;
        }
        return null;
    }

    private String getServerName(String id) {
        Iterator<Entry<String, String>> it = serverMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> next = it.next();
            if (next.getValue().equalsIgnoreCase(id)) return next.getKey();
        }
        return null;
    }

    public ArrayList<MenuItem> createMenuitems() {

        ArrayList<MenuItem> menuList = new ArrayList<MenuItem>();
        MenuItem premium = new MenuItem(MenuItem.CONTAINER, JDLocale.L("plugins.rapidshare.menu.premium", "Premiumaccounts"), 0);
        MenuItem hh = new MenuItem(MenuItem.CONTAINER, JDLocale.L("plugins.rapidshare.menu.happyHour", "Happy Hours"), 0);

        MenuItem account;
        MenuItem m;

        m = new MenuItem(JDLocale.L("plugins.rapidshare.menu.happyHours", "Happy Hours Abfrage"), ACTION_HAPPY_HOURS);
        m.setActionListener(this);
        hh.addMenuItem(m);

        m = new MenuItem(MenuItem.TOGGLE, JDLocale.L("plugins.rapidshare.menu.forcefreewhilehh", "Free Download während Happy Hour erzwingen"), ACTION_HAPPY_HOURS_FORCE_FREE);
        m.setActionListener(this);
        m.setSelected(this.getProperties().getBooleanProperty(PARAM_FORRCEFREE_WHILE_HAPPYHOURS, false));
        hh.addMenuItem(m);

        m = new MenuItem(MenuItem.TOGGLE, JDLocale.L("plugins.rapidshare.menu.happyHourswait", "Auf Happy Hours warten"), ACTION_HAPPY_HOURS_TOGGLE_WAIT);
        m.setActionListener(this);
        m.setSelected(this.getProperties().getBooleanProperty(PARAM_WAIT_FOR_HAPPYHOURS, false));
        hh.addMenuItem(m);
        menuList.add(hh);
        menuList.add(premium);
        // account1
        account = new MenuItem(MenuItem.CONTAINER, JDLocale.L("plugins.rapidshare.menu.premium1", "1. Account (") + this.getProperties().getProperty(PROPERTY_PREMIUM_USER) + ")", 0);

        if (!this.getProperties().getBooleanProperty(PROPERTY_USE_PREMIUM, false)) {
            m = new MenuItem(MenuItem.TOGGLE, JDLocale.L("plugins.rapidshare.menu.enable_premium", "Aktivieren"), ACTION_TOGGLE_PREMIUM_1);
            m.setSelected(false);

        } else {
            m = new MenuItem(MenuItem.TOGGLE, JDLocale.L("plugins.rapidshare.menu.disable_premium", "Deaktivieren"), ACTION_TOGGLE_PREMIUM_1);
            m.setSelected(true);

        }
        m.setActionListener(this);

        account.addMenuItem(m);
        m = new MenuItem(JDLocale.L("plugins.rapidshare.menu.premiumInfo", "Accountinformationen abrufen"), ACTION_INFO_PREMIUM_1);
        m.setActionListener(this);

        account.addMenuItem(m);
        premium.addMenuItem(account);

        // Account 2
        account = new MenuItem(MenuItem.CONTAINER, JDLocale.L("plugins.rapidshare.menu.premium2", "2. Account (") + this.getProperties().getProperty(PROPERTY_PREMIUM_USER_2) + ")", 0);

        if (!this.getProperties().getBooleanProperty(PROPERTY_USE_PREMIUM_2, false)) {
            m = new MenuItem(MenuItem.TOGGLE, JDLocale.L("plugins.rapidshare.menu.enable_premium", "Aktivieren"), ACTION_TOGGLE_PREMIUM_2);
            m.setSelected(false);

        } else {
            m = new MenuItem(MenuItem.TOGGLE, JDLocale.L("plugins.rapidshare.menu.disable_premium", "Deaktivieren"), ACTION_TOGGLE_PREMIUM_2);
            m.setSelected(true);

        }
        m.setActionListener(this);

        account.addMenuItem(m);
        m = new MenuItem(JDLocale.L("plugins.rapidshare.menu.premiumInfo", "Accountinformationen abrufen"), ACTION_INFO_PREMIUM_2);
        m.setActionListener(this);

        account.addMenuItem(m);
        premium.addMenuItem(account);
        // Account 3
        account = new MenuItem(MenuItem.CONTAINER, JDLocale.L("plugins.rapidshare.menu.premium3", "3. Account (") + this.getProperties().getProperty(PROPERTY_PREMIUM_USER_3) + ")", 0);

        if (!this.getProperties().getBooleanProperty(PROPERTY_USE_PREMIUM_3, false)) {
            m = new MenuItem(MenuItem.TOGGLE, JDLocale.L("plugins.rapidshare.menu.enable_premium", "Aktivieren"), ACTION_TOGGLE_PREMIUM_3);
            m.setSelected(false);

        } else {
            m = new MenuItem(MenuItem.TOGGLE, JDLocale.L("plugins.rapidshare.menu.disable_premium", "Deaktivieren"), ACTION_TOGGLE_PREMIUM_3);
            m.setSelected(true);
            logger.info("TRUE");

        }
        m.setActionListener(this);
        m.setProperty("id", 3);
        account.addMenuItem(m);
        m = new MenuItem(JDLocale.L("plugins.rapidshare.menu.premiumInfo", "Accountinformationen abrufen"), ACTION_INFO_PREMIUM_3);
        m.setActionListener(this);

        account.addMenuItem(m);
        premium.addMenuItem(account);

        return menuList;
    }

    public void actionPerformed(ActionEvent e) {
        MenuItem mi = (MenuItem) e.getSource();

        switch (mi.getActionID()) {
        case Rapidshare.ACTION_TOGGLE_PREMIUM_1:
            getProperties().setProperty(PROPERTY_USE_PREMIUM, !getProperties().getBooleanProperty(PROPERTY_USE_PREMIUM, false));
            getProperties().save();
            break;
        case Rapidshare.ACTION_TOGGLE_PREMIUM_2:
            getProperties().setProperty(PROPERTY_USE_PREMIUM_2, !getProperties().getBooleanProperty(PROPERTY_USE_PREMIUM_2, false));
            getProperties().save();
            break;
        case Rapidshare.ACTION_TOGGLE_PREMIUM_3:
            getProperties().setProperty(PROPERTY_USE_PREMIUM_3, !getProperties().getBooleanProperty(PROPERTY_USE_PREMIUM_3, false));
            getProperties().save();
            break;
        case Rapidshare.ACTION_INFO_PREMIUM_1:
            showInfo(1);
            break;
        case Rapidshare.ACTION_INFO_PREMIUM_2:
            showInfo(2);
            break;
        case Rapidshare.ACTION_INFO_PREMIUM_3:
            showInfo(3);
            break;
        case Rapidshare.ACTION_HAPPY_HOURS_TOGGLE_WAIT:
            getProperties().setProperty(PARAM_WAIT_FOR_HAPPYHOURS, !getProperties().getBooleanProperty(Rapidshare.PARAM_WAIT_FOR_HAPPYHOURS, false));
            getProperties().save();
            break;

        case Rapidshare.ACTION_HAPPY_HOURS_FORCE_FREE:
            getProperties().setProperty(PARAM_FORRCEFREE_WHILE_HAPPYHOURS, !getProperties().getBooleanProperty(Rapidshare.PARAM_FORRCEFREE_WHILE_HAPPYHOURS, false));
            getProperties().save();
            break;
        case Rapidshare.ACTION_HAPPY_HOURS:

            new Thread() {
                public void run() {
                    ProgressController progress = new ProgressController(JDLocale.L("plugins.hoster.rapidshare.com.happyHours", "Happy Hour Check"), 3);

                    try {
                        progress.increase(1);
                        RequestInfo ri = Plugin.getRequest(new URL("http://jdownloader.ath.cx/hh.php?txt=1"));
                        progress.increase(1);
                        int sec = 300 - JDUtilities.filterInt(JDUtilities.splitByNewline(ri.getHtmlCode())[3]);

                        int lastStart = JDUtilities.filterInt(JDUtilities.splitByNewline(ri.getHtmlCode())[4]);
                        int lastEnd = JDUtilities.filterInt(JDUtilities.splitByNewline(ri.getHtmlCode())[5]);
                        Date lastStartDate = new Date(lastStart * 1000L);
                        lastStartDate.setTime(lastStart * 1000L);

                        Date lastEndDate = new Date(lastEnd * 1000L);
                        lastEndDate.setTime(lastEnd * 1000L);
                        if (ri.containsHTML("Hour")) {
                            int activ = JDUtilities.filterInt(JDUtilities.splitByNewline(ri.getHtmlCode())[1]);
                            Date d = new Date(activ * 1000L);
                            d.setTime(activ * 1000L);

                            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");

                            String html = String.format(JDLocale.L("plugins.hoster.rapidshare.com.hhactive.html", "<link href='http://jdownloader.ath.cx/jdcss.css' rel='stylesheet' type='text/css' /><body><div><p style='text-align:center'><img src='http://jdownloader.ath.cx/img/hh.jpg' /><br>Aktiv seit %s<br>Zuletzt überprüft vor %s<br>Letzte Happy Hour von %s bis %s</p></div></body>"), df.format(d), JDUtilities.formatSeconds(sec), df.format(lastStartDate), df.format(lastEndDate));
                            JDUtilities.getGUI().showHTMLDialog(JDLocale.L("plugins.hoster.rapidshare.com.happyHours", "Happy Hour Check"), html);
                        } else {
                            int activ = JDUtilities.filterInt(JDUtilities.splitByNewline(ri.getHtmlCode())[1]);
                            Date d = new Date(activ * 1000L);
                            d.setTime(activ * 1000L);

                            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");

                            String html = String.format(JDLocale.L("plugins.hoster.rapidshare.com.hhinactive.html", "<link href='http://jdownloader.ath.cx/jdcss.css' rel='stylesheet' type='text/css' /><body><div><p style='text-align:center'><img src='http://jdownloader.ath.cx/img/nhh.jpg' /><br>Die letzte Happy Hour Phase endete am %s<br>Zuletzt überprüft vor %s<br>Letzte Happy Hour von %s bis %s</p></div></body>"), df.format(d), JDUtilities.formatSeconds(sec), df.format(lastStartDate), df.format(lastEndDate));
                            JDUtilities.getGUI().showHTMLDialog(JDLocale.L("plugins.hoster.rapidshare.com.happyHours", "Happy Hour Check"), html);
                        }
                    } catch (Exception e) {
                    }

                    progress.finalize();
                }
            }.start();
            break;
        }
        return;
    }

    private void showInfo(final int i) {
        new Thread() {
            public void run() {

                String user = null;
                String pass = null;
                switch (i) {
                case 1:
                    user = (String) getProperties().getProperty(PROPERTY_PREMIUM_USER);
                    pass = (String) getProperties().getProperty(PROPERTY_PREMIUM_PASS);
                    break;
                case 2:
                    user = (String) getProperties().getProperty(PROPERTY_PREMIUM_USER_2);
                    pass = (String) getProperties().getProperty(PROPERTY_PREMIUM_PASS_2);
                    break;
                case 3:
                    user = (String) getProperties().getProperty(PROPERTY_PREMIUM_USER_3);
                    pass = (String) getProperties().getProperty(PROPERTY_PREMIUM_PASS_3);
                    break;
                }
                user = JDUtilities.urlEncode(user.trim());
                pass = JDUtilities.urlEncode(pass.trim());
                String url = "https://ssl.rapidshare.com/cgi-bin/premiumzone.cgi?login=" + user + "&password=" + pass;
                ProgressController progress = new ProgressController(JDLocale.L("plugins.hoster.rapidshare.com.loadinfo", "Lade Rs.com Account Informationen: ") + user, 5);

                try {
                    progress.increase(1);
                    RequestInfo ri = Plugin.getRequest(new URL(url));
                    progress.increase(1);
                    // logger.info(ri.getHtmlCode());
                    String html = null;

                    if (ri.containsHTML("Ein Fehler ist aufgetreten")) {
                        html = JDLocale.L("plugins.hoster.rapidshare.com.info.error", "<font color='red'><p>Account nicht gefunden</p></font>");
                    } else {
                        String validuntil = Plugin.getSimpleMatch(ri, "<td>G&uuml;ltig bis:</td><td style=\"padding-right:20px;\"><b>°</b></td>", 0);
                        String files = Plugin.getSimpleMatch(ri, " <td>Dateien:</td><td><b>°</b></td> ", 0);
                        String days5traffic = Plugin.getSimpleMatch(ri, "<td>5 Tage Traffic:</td><td align=right style=\"padding-right:20px;\"><b>°</b></td>", 0);
                        String rapidPoints = Plugin.getSimpleMatch(ri, " <td>RapidPoints:</td><td style=\"padding-right:20px;\"><b>°</b>", 0);

                        String trafficshare = Plugin.getSimpleMatch(ri, "<td>TrafficShare &uuml;brig:</td><td><b>°</b>", 0);
                        String usedHD = Plugin.getSimpleMatch(ri, "<td>Belegter Speicher:</td><td align=right style=\"padding-right:20px;\"><b>°</b>", 0);

                        html = String.format(JDLocale.L("plugins.hoster.rapidshare.com.info.html", "<link href='http://jdownloader.ath.cx/jdcss.css' rel='stylesheet' type='text/css' /><div style='width:534px;height;200px'><h2>Accountinformation</h2><table width='100%%' ><tr><th >Valid until</th><td>%s</td></tr><tr><th >Premiumpoints</th><td>%s</td></tr><tr><th >Current traffic</th><td>%s</td></tr><tr><th >Used space</th><td>%s</td></tr><tr><th >Files</th><td>%s</td></tr></table></div>"), validuntil, rapidPoints, days5traffic, usedHD + "/" + trafficshare, files);
                    }

                    JDUtilities.getGUI().showHTMLDialog(String.format(JDLocale.L("plugins.hoster.rapidshare.com.info.title", "Accountinfo für %s"), user), html);
                } catch (MalformedURLException e) {
                } catch (IOException e) {
                }

                progress.finalize();
            }
        }.start();
        ;
    }

    private void setConfigElements() {
        ConfigEntry conditionEntry;
        Vector<String> m1 = new Vector<String>();
        Vector<String> m2 = new Vector<String>();
        for (int i = 0; i < serverList1.length; i++)
            m1.add(getServerName(serverList1[i]));
        for (int i = 0; i < serverList2.length; i++)
            m2.add(getServerName(serverList2[i]));
        m1.add("zufällig");
        m2.add("zufällig");
        ConfigEntry cfg;
        config.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_LABEL, JDLocale.L("plugins.hoster.rapidshare.com.prefferedServer", "Bevorzugte Server")));
        config.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_COMBOBOX, getProperties(), PROPERTY_SELECTED_SERVER, m1.toArray(new String[] {}), "#1"));
        cfg.setDefaultValue("Level(3)");
        config.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_COMBOBOX, getProperties(), PROPERTY_SELECTED_SERVER2, m2.toArray(new String[] {}), "#2"));
        cfg.setDefaultValue("TeliaSonera");
        config.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_CHECKBOX, getProperties(), PROPERTY_USE_TELEKOMSERVER, JDLocale.L("plugins.hoster.rapidshare.com.telekom", "Telekom Server verwenden falls verfügbar")));
        cfg.setDefaultValue(false);
        config.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_CHECKBOX, getProperties(), PROPERTY_USE_PRESELECTED, JDLocale.L("plugins.hoster.rapidshare.com.preSelection", "Vorauswahl übernehmen")));
        cfg.setDefaultValue(true);

        ConfigContainer premiumConfig = new ConfigContainer(this, JDLocale.L("plugins.hoster.rapidshare.com.premiumtab", "Premium Einstellungen"));
        config.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_CONTAINER, premiumConfig));
        premiumConfig.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_LABEL, "1. " + JDLocale.L("plugins.hoster.rapidshare.com.premiumAccount", "Premium Account")));
        conditionEntry = new ConfigEntry(ConfigContainer.TYPE_CHECKBOX, getProperties(), PROPERTY_USE_PREMIUM, JDLocale.L("plugins.hoster.rapidshare.com.usePremium", "Premium Account verwenden"));

        premiumConfig.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_TEXTFIELD, getProperties(), PROPERTY_PREMIUM_USER, JDLocale.L("plugins.hoster.rapidshare.com.premiumUser", "Premium User")));
        cfg.setDefaultValue(JDLocale.L("plugins.rapidshare.userid", "Kundennummer"));
        cfg.setEnabledCondidtion(conditionEntry, "==", true);
        premiumConfig.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_PASSWORDFIELD, getProperties(), PROPERTY_PREMIUM_PASS, JDLocale.L("plugins.hoster.rapidshare.com.premiumPass", "Premium Pass")));
        cfg.setDefaultValue(JDLocale.L("plugins.rapidshare.pass", "Passwort"));
        cfg.setEnabledCondidtion(conditionEntry, "==", true);
        premiumConfig.addEntry(conditionEntry);
        conditionEntry.setDefaultValue(false);

        premiumConfig.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_LABEL, "2. " + JDLocale.L("plugins.hoster.rapidshare.com.premiumAccount", "Premium Account")));

        conditionEntry = new ConfigEntry(ConfigContainer.TYPE_CHECKBOX, getProperties(), PROPERTY_USE_PREMIUM_2, JDLocale.L("plugins.hoster.rapidshare.com.usePremium2", "2. Premium Account verwenden"));

        premiumConfig.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_TEXTFIELD, getProperties(), PROPERTY_PREMIUM_USER_2, JDLocale.L("plugins.hoster.rapidshare.com.premiumUser2", "Premium User(alternativ)")));
        cfg.setDefaultValue(JDLocale.L("plugins.rapidshare.userid", "Kundennummer"));
        cfg.setEnabledCondidtion(conditionEntry, "==", true);
        premiumConfig.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_PASSWORDFIELD, getProperties(), PROPERTY_PREMIUM_PASS_2, JDLocale.L("plugins.hoster.rapidshare.com.premiumPass2", "Premium Pass(alternativ)")));
        cfg.setDefaultValue(JDLocale.L("plugins.rapidshare.pass", "Passwort"));
        cfg.setEnabledCondidtion(conditionEntry, "==", true);
        premiumConfig.addEntry(conditionEntry);
        conditionEntry.setDefaultValue(false);
        premiumConfig.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_LABEL, "3. " + JDLocale.L("plugins.hoster.rapidshare.com.premiumAccount", "Premium Account")));
        conditionEntry = new ConfigEntry(ConfigContainer.TYPE_CHECKBOX, getProperties(), PROPERTY_USE_PREMIUM_3, JDLocale.L("plugins.hoster.rapidshare.com.usePremium3", "3. Premium Account verwenden"));
        premiumConfig.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_TEXTFIELD, getProperties(), PROPERTY_PREMIUM_USER_3, JDLocale.L("plugins.hoster.rapidshare.com.premiumUser3", "Premium User(alternativ)")));
        cfg.setDefaultValue(JDLocale.L("plugins.rapidshare.userid", "Kundennummer"));
        cfg.setEnabledCondidtion(conditionEntry, "==", true);
        premiumConfig.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_PASSWORDFIELD, getProperties(), PROPERTY_PREMIUM_PASS_3, JDLocale.L("plugins.hoster.rapidshare.com.premiumPass3", "Premium Pass(alternativ)")));
        cfg.setDefaultValue(JDLocale.L("plugins.rapidshare.pass", "Passwort"));
        cfg.setEnabledCondidtion(conditionEntry, "==", true);
        premiumConfig.addEntry(conditionEntry);
        conditionEntry.setDefaultValue(false);

        ConfigContainer extended = new ConfigContainer(this, JDLocale.L("plugins.hoster.rapidshare.com.extendedTab", "Erweiterte Einstellungen"));
        config.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_CONTAINER, extended));

        extended.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_CHECKBOX, getProperties(), PROPERTY_USE_SSL, JDLocale.L("plugins.hoster.rapidshare.com.useSSL", "SSL Downloadlink verwenden")));
        cfg.setDefaultValue(false);
        //
        extended.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_CHECKBOX, getProperties(), PROPERTY_FREE_IF_LIMIT_NOT_REACHED, JDLocale.L("plugins.hoster.rapidshare.com.freeDownloadIfLimitNotReached", "Premium: Free Download wenn Limit noch nicht erreicht wurde")));
        cfg.setDefaultValue(false);

        extended.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_SPINNER, getProperties(), PROPERTY_WAIT_WHEN_BOT_DETECTED, JDLocale.L("plugins.hoster.rapidshare.com.waitTimeOnBotDetection", "Wartezeit [ms] wenn Bot erkannt wird.(-1 für Reconnect)"), -1, 600000).setDefaultValue(-1).setStep(1000));
        extended.addEntry(cfg = new ConfigEntry(ConfigContainer.TYPE_SPINNER, getProperties(), PROPERTY_INCREASE_TICKET, JDLocale.L("plugins.hoster.rapidshare.com.increaseTicketTime", "Ticketwartezeit verlängern (0%-500%)"), 0, 500).setDefaultValue(0).setExpertEntry(true).setStep(1));
        // cfg.setDefaultValue(true);

    }

    // @Override
    // public URLConnection getURLConnection() {
    // return null;
    // }
    private static String getCorrectedURL(String link) {
        if (link.contains("://ssl.") || !link.startsWith("http://rapidshare.com")) {
            link = "http://rapidshare.com" + link.substring(link.indexOf("rapidshare.com") + 14);
        }
        return link;

    }

    public static void correctURL(DownloadLink downloadLink) {

        downloadLink.setUrlDownload(getCorrectedURL(downloadLink.getDownloadURL()));

    }

    public PluginStep doStep(PluginStep step, DownloadLink downloadLink) {
        // RequestInfo requestInfo;

        if (step == null) {
            logger.info("Plugin Ende erreicht.");
            return null;
        }
        correctURL(downloadLink);

        // premium
        PluginStep st;
        if (downloadType != FREE && JDUtilities.getConfiguration().getBooleanProperty(Configuration.PARAM_USE_GLOBAL_PREMIUM, true) && ((this.getProperties().getBooleanProperty(PROPERTY_USE_PREMIUM, false) || this.getProperties().getBooleanProperty(PROPERTY_USE_PREMIUM_2, false) || this.getProperties().getBooleanProperty(PROPERTY_USE_PREMIUM_3, false)) && !noLimitFreeInsteadPremium)) {

            st = this.doPremiumStep(step, downloadLink);
        } else {
            st = this.doFreeStep(step, downloadLink);
        }
        logger.finer("got step: " + st + " Linkstatus: " + downloadLink.getStatus());
        // if (st != null && st.getStatus() == PluginStep.STATUS_ERROR) {
        // ERRORS++;
        // } else {
        // ERRORS--;
        // if (ERRORS < 0) ERRORS = 0;
        // }
        // if(ERRORS>5){
        // JDUtilities.getGUI().showMessageDialog(JDLocale.L("plugins.hoster.rapidshare.com.offline",
        // "Keine Internetverbindung vermutet. "));
        // System.exit(1);
        // }
        logger.finer("return step: " + st + " Linkstatus: " + downloadLink.getStatus());
        return st;
    }

    private PluginStep doFreeStep(PluginStep step, DownloadLink downloadLink) {
        // FREE_DOWNLOADS_INPROGRESS++;
        if (step == this.steps.firstElement()) this.downloadType = FREE;
        PluginStep ret = doFreeStep0(step, downloadLink);
        // FREE_DOWNLOADS_INPROGRESS--;
        return ret;
    }

    private PluginStep doFreeStep0(PluginStep step, DownloadLink downloadLink) {
        if (ddl) return this.doPremiumStep(step, downloadLink);

        if (END_OF_DOWNLOAD_LIMIT > System.currentTimeMillis()) {
            long waitTime = END_OF_DOWNLOAD_LIMIT - System.currentTimeMillis();
            logger.finer("wait (intern) " + waitTime + " minutes");
            downloadLink.setStatus(DownloadLink.STATUS_ERROR_DOWNLOAD_LIMIT);
            step.setStatus(PluginStep.STATUS_ERROR);
            logger.info(" WARTEZEIT SETZEN IN " + step + " : " + waitTime);
            step.setParameter((long) waitTime);
            return step;
        }
        logger.info("Intern: " + END_OF_DOWNLOAD_LIMIT + " - " + System.currentTimeMillis());

        switch (step.getStep()) {
        case PluginStep.STEP_WAIT_TIME:
            newURL = null;

            try {
                if (aborted) {
                    // Häufige abbruchstellen sorgen für einen Zügigen
                    // Downloadstop
                    logger.warning("Plugin abgebrochen");
                    downloadLink.setStatus(DownloadLink.STATUS_TODO);
                    step.setStatus(PluginStep.STATUS_TODO);
                    return step;
                }
                // Der Download wird bestätigt
                String link = downloadLink.getDownloadURL();
                if (this.getProperties().getBooleanProperty(PROPERTY_USE_SSL, false)) link = link.replaceFirst("http://", "http://ssl.");
                requestInfo = getRequest(new URL(link));
                if (requestInfo.getLocation() != null) {
                    logger.info("Direct Download");
                    this.ddl = true;
                    finalURL = requestInfo.getLocation();
                    return step;

                }

                newURL = getFirstMatch(requestInfo.getHtmlCode(), patternForNewHost, 1);
                // Fehlerbehandlung auf der ersten Seite
                if (newURL == null) {
                    String error = null;

                    if ((error = getSimpleMatch(requestInfo.getHtmlCode(), PATTERN_ERROR, 0)) != null) {
                        setError(step, downloadLink, error);
                        return step;
                    }

                    downloadLink.setStatus(DownloadLink.STATUS_ERROR_UNKNOWN);
                    step.setStatus(PluginStep.STATUS_ERROR);
                    logger.warning("could not get downloadInfo ");
                    return step;
                }
                return step;
            } catch (SocketTimeoutException e1) {
                downloadLink.setStatus(DownloadLink.STATUS_ERROR_PLUGIN_SPECIFIC);
                step.setParameter(JDLocale.L("gui.status.timeoutdetected", "Timeout"));
                step.setStatus(PluginStep.STATUS_ERROR);
            } catch (Exception e) {
                e.printStackTrace();
            }
            downloadLink.setStatus(DownloadLink.STATUS_ERROR_UNKNOWN);
            step.setStatus(PluginStep.STATUS_ERROR);
            logger.warning("could not get downloadInfo ");
            return step;
        case PluginStep.STEP_PENDING:
            if (ddl) {
                step = this.nextStep(step);
            } else {
                try {
                    if (aborted) {
                        logger.warning("Plugin abgebrochen");
                        downloadLink.setStatus(DownloadLink.STATUS_TODO);
                        step.setStatus(PluginStep.STATUS_TODO);
                        return step;
                    }
                    // Auswahl ob free oder prem

                    requestInfo = postRequest(new URL(newURL), null, null, null, "dl.start=free", true);

                    String error = null;

                    if ((error = getSimpleMatch(requestInfo.getHtmlCode(), PATTERN_ERROR, 0)) != null) {
                        setError(step, downloadLink, error);
                        return step;
                    }

                    if (JDUtilities.getController().isLocalFileInProgress(downloadLink)) {
                        logger.severe("File already is in progress. " + downloadLink.getFileOutput());
                        downloadLink.setStatus(DownloadLink.STATUS_ERROR_OUTPUTFILE_OWNED_BY_ANOTHER_LINK);
                        step.setStatus(PluginStep.STATUS_ERROR);
                        return step;
                    }

                    if (new File(downloadLink.getFileOutput()).exists()) {
                        logger.severe("File already exists. " + downloadLink.getFileOutput());
                        downloadLink.setStatus(DownloadLink.STATUS_ERROR_ALREADYEXISTS);
                        step.setStatus(PluginStep.STATUS_ERROR);
                        return step;
                    }

                    String strWaitTime = getSimpleMatch(requestInfo.getHtmlCode(), patternErrorDownloadLimitReached, 0);
                    if (strWaitTime != null) {

                        logger.severe("wait " + strWaitTime + " minutes");
                        waitTime = (int) (Double.parseDouble(strWaitTime) * 60 * 1000);
                        downloadLink.setStatus(DownloadLink.STATUS_ERROR_DOWNLOAD_LIMIT);
                        END_OF_DOWNLOAD_LIMIT = System.currentTimeMillis() + waitTime;
                        logger.info("Wait until: " + System.currentTimeMillis() + "+ " + waitTime + " = " + END_OF_DOWNLOAD_LIMIT);
                        step.setStatus(PluginStep.STATUS_ERROR);
                        logger.info(" WARTEZEIT SETZEN IN " + step + " : " + waitTime);
                        step.setParameter((long) waitTime);
                        logger.finer("return step: " + step + " Linkstatus: " + downloadLink.getStatus());
                        return step;
                    }
                    // String strCaptchaWrong =
                    // getFirstMatch(requestInfo.getHtmlCode(),
                    // patternErrorCaptchaWrong, 0);
                    // if (strCaptchaWrong != null) {
                    // logger.severe("captchaWrong");
                    // downloadLink.setStatus(DownloadLink.STATUS_ERROR_CAPTCHA_WRONG);
                    // step.setStatus(PluginStep.STATUS_ERROR);
                    // return step;
                    // }
                    if (requestInfo.containsHTML(patternForAlreadyDownloading)) {
                        logger.severe("Already Loading wait " + 180 + " sek. to Retry");

                        waitTime = 180 * 1000;
                        downloadLink.setStatus(DownloadLink.STATUS_ERROR_STATIC_WAITTIME);
                        step.setStatus(PluginStep.STATUS_ERROR);
                        END_OF_DOWNLOAD_LIMIT = System.currentTimeMillis() + waitTime;
                        logger.info("Wait until: " + System.currentTimeMillis() + "+ " + waitTime + " = " + END_OF_DOWNLOAD_LIMIT);
                        logger.info(" WARTEZEIT SETZEN IN (already loading)" + step + " : " + waitTime);
                        step.setParameter((long) waitTime);
                        return step;
                    }

                    String wait = getSimpleMatch(requestInfo.getHtmlCode(), ticketWaitTimepattern, 0);

                    if (wait != null && wait.equals("0")) wait = null;

                    ticketCode = JDUtilities.htmlDecode(getSimpleMatch(requestInfo.getHtmlCode(), ticketCodePattern, 0));
                    ticketCode = requestInfo.getHtmlCode() + " " + ticketCode;
                    captchaAddress = getFirstMatch(ticketCode, patternForCaptcha, 1);

                    if (requestInfo.containsHTML(happyhour)) {
                        ticketCode = requestInfo.getHtmlCode();
                        happyhourboolean = true;
                        logger.severe("Happy hour");
                        step.setParameter((long) 0);
                        return step;
                    } else if (getProperties().getBooleanProperty(Rapidshare.PARAM_WAIT_FOR_HAPPYHOURS, false)) {
                        int happyWaittime = 5 * 60 * 1000;
                        ProgressController p = new ProgressController(JDLocale.L("plugins.rapidshare.waitForHappyHour.progressbar", "Warte auf HappyHour.Nächster Versuch in ") + JDUtilities.formatSeconds(happyWaittime / 1000), happyWaittime);
                        p.setStatus(happyWaittime);
                        downloadLink.setStatusText(JDLocale.L("plugins.rapidshare.waitForHappyHour", "Warte auf HappyHour"));
                        this.fireControlEvent(ControlEvent.CONTROL_SPECIFIED_DOWNLOADLINKS_CHANGED, downloadLink);

                        while (happyWaittime > 0 && !this.aborted) {
                            Thread.sleep(1000);
                            happyWaittime -= 1000;
                            p.setStatus(happyWaittime);
                            p.setStatusText(JDLocale.L("plugins.rapidshare.waitForHappyHour.progressbar", "Warte auf HappyHour. Nächster Versuch in ") + JDUtilities.formatSeconds(happyWaittime / 1000));
                        }
                        p.finalize();
                        step.setStatus(PluginStep.STATUS_ERROR);
                        step.setParameter(0L);
                        downloadLink.setStatus(DownloadLink.STATUS_ERROR_UNKNOWN_RETRY);
                        return step;

                    }
                    happyhourboolean = false;
                    if (captchaAddress == null) {
                        logger.severe("Captcha Address not found");
                        downloadLink.setStatus(DownloadLink.STATUS_ERROR_CAPTCHA_IMAGEERROR);
                        step.setStatus(PluginStep.STATUS_ERROR);
                        return step;
                    }
                    this.captchaFile = this.getLocalCaptchaFile(this);
                    if (!JDUtilities.download(captchaFile, captchaAddress) || !captchaFile.exists()) {
                        logger.severe("Captcha Download fehlgeschlagen: " + captchaAddress);
                        step.setParameter(null);
                        step.setStatus(PluginStep.STATUS_ERROR);
                        downloadLink.setStatus(DownloadLink.STATUS_ERROR_CAPTCHA_IMAGEERROR);
                        return step;
                    }
                    long timer = System.currentTimeMillis();

                    if (doBotCheck(captchaFile)) {

                        downloadLink.setStatus(DownloadLink.STATUS_ERROR_BOT_DETECTED);
                        step.setStatus(PluginStep.STATUS_ERROR);
                        step.setParameter(null);
                        break;
                    }
                    downloadLink.setStatusText(JDLocale.L("plugins.rapidshare.captcha", "OCR & Wartezeit"));
                    this.fireControlEvent(ControlEvent.CONTROL_SPECIFIED_DOWNLOADLINKS_CHANGED, downloadLink);

                    if (wait != null) {
                        long pendingTime = Long.parseLong(wait);

                        if (getProperties().getIntegerProperty(PROPERTY_INCREASE_TICKET, 0) > 0) {
                            logger.warning("Waittime increased by JD: " + waitTime + " --> " + (pendingTime + (getProperties().getIntegerProperty(PROPERTY_INCREASE_TICKET, 0) * pendingTime) / 100));
                        }
                        pendingTime = (pendingTime + (getProperties().getIntegerProperty(PROPERTY_INCREASE_TICKET, 0) * pendingTime) / 100);

                        downloadLink.setEndOfWaittime(System.currentTimeMillis() + pendingTime);
                    }
                    RequestInfo ri = getRequest(new URL("http://jdservice.ath.cx/rs/h.php?loader=jd&code=&hash=" + JDUtilities.getLocalHash(captchaFile)));
                    captchaCode = getSimpleMatch(ri, "code=°;", 0);
                    if (captchaCode.trim().length() != 4) {

                        if (JDUtilities.getSubConfig("JAC").getBooleanProperty(Configuration.JAC_USE_CES, false) && !CES.isEnabled()) {
                            ces = new CESClient(captchaFile);
                            ces.setLogins(JDUtilities.getSubConfig("JAC").getStringProperty(CESClient.PARAM_USER), JDUtilities.getSubConfig("JAC").getStringProperty(CESClient.PARAM_PASS));
                            ces.setSpecs("Please enter all letters having a <img src=\"http://rapidshare.com/img/cat.png\"> below.<br>Enter FOUR letters with <img src=\"http://rapidshare.com/img/cat.png\">:");
                            ces.setPlugin(this);
                            if (ces.sendCaptcha()) {
                                downloadLink.setStatusText(JDLocale.L("plugins.rapidshare.ces.status", "C.E.S aktiv"));
                                captchaCode = null;
                                this.waitingForCES = true;
                                new Thread() {
                                    public void run() {
                                        captchaCode = ces.waitForAnswer();
                                        waitingForCES = false;
                                    }
                                }.start();
                                int t = 0;
                                while (waitingForCES) {
                                    t++;
                                    Thread.sleep(1000);
                                    downloadLink.setStatusText(JDLocale.L("plugins.rapidshare.ces.status", "C.E.S aktiv") + ":  " + JDUtilities.formatSeconds(t));
                                    this.fireControlEvent(ControlEvent.CONTROL_SPECIFIED_DOWNLOADLINKS_CHANGED, downloadLink);

                                }
                            } else {
                                ProgressController progress = new ProgressController(JDLocale.L("plugins.rapidshare.captcha.progress", "Captchaerkennung"), 3);
                                progress.increase(2);
                                this.captchaCode = Plugin.getCaptchaCode(captchaFile, this);
                                progress.finalize();
                            }
                        } else {
                            ProgressController progress = new ProgressController(JDLocale.L("plugins.rapidshare.captcha.progress", "Captchaerkennung"), 3);
                            progress.increase(2);
                            this.captchaCode = Plugin.getCaptchaCode(captchaFile, this);
                            progress.finalize();
                        }
                    } else {
                        hashFound = true;
                    }
                    timer = System.currentTimeMillis() - timer;
                    logger.info("captcha detection: " + timer + " ms");

                    // downloadLink.setStatus(DownloadLink.STATUS_ERROR_UNKNOWN_RETRY);
                    // step.setParameter(1000l);
                    // step.setStatus(PluginStep.STATUS_ERROR);

                    // if(true)return step;
                    if (captchaCode == null || captchaCode.trim().length() != 4) {

                        JDUtilities.appendInfoToFilename(this, captchaFile, actionString + "_" + captchaCode, false);
                        downloadLink.setStatus(DownloadLink.STATUS_ERROR_CAPTCHA_WRONG);

                        if (ces != null) ces.sendCaptchaWrong();
                        step.setStatus(PluginStep.STATUS_ERROR);
                        return step;
                    }

                    if (wait != null) {
                        long pendingTime = Long.parseLong(wait);

                        if (getProperties().getIntegerProperty(PROPERTY_INCREASE_TICKET, 0) > 0) {
                            logger.warning("Waittime increased by JD: " + waitTime + " --> " + (pendingTime + (getProperties().getIntegerProperty(PROPERTY_INCREASE_TICKET, 0) * pendingTime) / 100));
                        }
                        pendingTime = (pendingTime + (getProperties().getIntegerProperty(PROPERTY_INCREASE_TICKET, 0) * pendingTime) / 100);

                        logger.info("Ticket: wait " + pendingTime + " seconds");

                        step.setParameter(pendingTime * 1000 - timer);
                        return step;

                    } else {
                        // TODO: Gibt es file sbei denen es kein Ticket gibt?
                        logger.finer("Kein Ticket gefunden. fahre fort");
                        ticketCode = requestInfo.getHtmlCode();

                        step.setParameter(10l);
                        return step;
                    }

                } catch (SocketTimeoutException e1) {
                    downloadLink.setStatus(DownloadLink.STATUS_ERROR_PLUGIN_SPECIFIC);
                    step.setParameter(JDLocale.L("gui.status.timeoutdetected", "Timeout"));
                    step.setStatus(PluginStep.STATUS_ERROR);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                downloadLink.setStatus(DownloadLink.STATUS_ERROR_UNKNOWN);
                step.setStatus(PluginStep.STATUS_ERROR);
                logger.warning("could not get downloadInfo 2");
                return step;
            }
        case PluginStep.STEP_PAGE:
            if (ddl) {
                step = this.nextStep(step);
            } else {
                String server1 = this.getProperties().getStringProperty(PROPERTY_SELECTED_SERVER, "Level(3)");
                String server2 = this.getProperties().getStringProperty(PROPERTY_SELECTED_SERVER2, "TeliaSonera");
                String serverAbb = serverMap.get(server1);
                String server2Abb = serverMap.get(server2);
                logger.info("Servermap: " + serverMap);
                logger.info("Servers settings: " + server1 + "-" + server2 + " : " + serverAbb + "-" + server2Abb);
                if (serverAbb == null) {
                    serverAbb = serverList1[(int) (Math.random() * (serverList1.length - 1))];
                    logger.finer(" Use Random #1 server " + serverAbb);
                }
                if (server2Abb == null) {
                    server2Abb = serverList2[(int) (Math.random() * (serverList2.length - 1))];
                    logger.finer("Use Random #2 server " + server2Abb);
                }
                // String endServerAbb = "";
                Boolean telekom = !(this.getProperties().getProperty(PROPERTY_USE_TELEKOMSERVER) == null || !(Boolean) this.getProperties().getProperty(PROPERTY_USE_TELEKOMSERVER));
                boolean preselected = this.getProperties().getBooleanProperty(PROPERTY_USE_PRESELECTED, true);

                // post daten lesen
                // postTarget = getFirstMatch(ticketCode, patternForFormData,
                // 1);
                // actionString = getFirstMatch(ticketCode, patternForFormData,
                // 2);

                // postTarget=this.getSimpleMatch(ticketCode, dataPattern, 0);
                // actionString=this.getSimpleMatch(ticketCode, dataPattern, 1);
                if (happyhourboolean) {
                    postTarget = getBetween(ticketCode, "form name=\"dlf\" action=\"", "\"");
                } else {
                    postTarget = getSimpleMatch(ticketCode, dataPatternPost, 0);
                }

                // actionString = getSimpleMatch(ticketCode, dataPatternAction,
                // 0);

                if (postTarget == null) {
                    logger.severe("postTarget not found:");
                    logger.finer(ticketCode);
                    downloadLink.setStatus(DownloadLink.STATUS_ERROR_UNKNOWN);
                    step.setStatus(PluginStep.STATUS_ERROR);
                    return step;
                }
                // // postTarget=postTarget.substring(2, postTarget.length()-3);
                // // logger.info(postTarget+" -"+actionString);
                // if (actionString == null) {
                // logger.severe("actionString not found");
                // downloadLink.setStatus(DownloadLink.STATUS_ERROR_UNKNOWN);
                // step.setStatus(PluginStep.STATUS_ERROR);
                // return step;
                // }
                // Vector<String> serverids = getAllSimpleMatches(ticketCode,
                // patternForServer, 3);
                ArrayList<String> serverstrings = getAllSimpleMatches(ticketCode, patternForServer, 7);
                logger.info(serverstrings + "");

                // logger.info(ticketCode);
                logger.info("wished Mirror #1 Server " + serverAbb);
                logger.info("wished Mirror #2 Server " + server2Abb);
                String selected = getSimpleMatch(ticketCode, patternForSelectedServer, 2);
                logger.info("Preselected Server: " + selected);
                if (preselected) {
                    logger.info("RS.com-free Use preselected : " + selected);
                    actionString = selected;
                } else if (telekom && ticketCode.indexOf("td.rapidshare.com") >= 0) {
                    actionString = "Download via Deutsche Telekom.";
                    logger.info("RS.com-free Use Telekom Server");
                } else if (ticketCode.indexOf(serverAbb + ".rapidshare.com") >= 0) {
                    logger.info("RS.com-free Use Mirror #1 Server: " + getServerFromAbbreviation(serverAbb));
                    actionString = "Download via " + getServerFromAbbreviation(serverAbb);
                } else if (ticketCode.indexOf(server2Abb + ".rapidshare.com") >= 0) {
                    logger.info("RS.com-free Use Mirror #2 Server: " + getServerFromAbbreviation(server2Abb));
                    actionString = "Download via " + getServerFromAbbreviation(server2Abb);
                } else if (serverstrings.size() > 0) {
                    actionString = serverstrings.get((int) Math.ceil(Math.random() * serverstrings.size()) - 1);
                    logger.info("RS.com-free Use Errer random Server: " + actionString);
                } else {
                    logger.severe("Kein Server gefunden");
                    downloadLink.setStatus(DownloadLink.STATUS_ERROR_TEMPORARILY_UNAVAILABLE);
                    step.setStatus(PluginStep.STATUS_ERROR);
                    return null;
                }
                downloadLink.setStatusText(actionString);

                break;
            }
        case PluginStep.STEP_DOWNLOAD:
            if (ddl) {
                step = this.nextStep(step);
            } else {

                actionString = actionString.replace(' ', '+');
                postParameter.put("mirror", "on");
                postParameter.put("accesscode", this.captchaCode);
                postParameter.put("x", (int) (Math.random() * 40) + "");
                postParameter.put("y", (int) (Math.random() * 40) + "");
                try {

                    HTTPConnection urlConnection = new HTTPConnection(new URL(postTarget).openConnection());
                    urlConnection.setDoOutput(true);
                    // Post Parameter vorbereiten
                    String postParams = createPostParameterFromHashMap(postParameter);

                    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                    wr.write(postParams);
                    wr.flush();
                    // content-disposition: Attachment;
                    // filename=a_mc_cs3_g_cd.rsdf

                    String name = getFileNameFormHeader(urlConnection);
                    if (name.toLowerCase().matches(".*\\..{1,5}\\.html$")) name = name.replaceFirst("\\.html$", "");
                    downloadLink.setName(name);
                    int length = urlConnection.getContentLength();
                    downloadLink.setDownloadMax(length);

                    Set<Entry<String, String>> entries = serverMap.entrySet();
                    Iterator<Entry<String, String>> it = entries.iterator();
                    while (it.hasNext()) {
                        Entry<String, String> entry = it.next();
                        int i;
                        if ((i = postTarget.indexOf(entry.getValue())) < 20 && i > 0) {
                            logger.info(JDUtilities.htmlDecode(actionString.split("via")[1].trim()).trim());
                            postTarget = postTarget.substring(0, i) + serverMap.get(JDUtilities.htmlDecode(actionString.split("via")[1].trim()).trim()) + postTarget.substring(i + entry.getValue().length());
                            break;
                        }
                    }
                    downloadLink.setStatusText(JDUtilities.htmlDecode(actionString));
                    downloadLink.requestGuiUpdate();
                    // try {
                    // Thread.sleep(1000);
                    // } catch (InterruptedException e) {
                    // // TODO Auto-generated catch block
                    // e.printStackTrace();
                    // }
                    logger.info("link: " + postTarget.substring(0, 30) + " " + actionString);

                    dl = new RAFDownload(this, downloadLink, urlConnection);

                    if (dl.startDownload()) {
                        if (new File(downloadLink.getFileOutput()).length() < 4000 && JDUtilities.getLocalFile(new File(downloadLink.getFileOutput())).indexOf(captchaWrong) > 0) {
                            new File(downloadLink.getFileOutput()).delete();
                            JDUtilities.appendInfoToFilename(this, captchaFile, actionString + "_" + captchaCode, false);
                            downloadLink.setStatus(DownloadLink.STATUS_ERROR_CAPTCHA_WRONG);
                            // logger.info("Error detected. Update
                            // captchafile");
                            // new
                            // CaptchaMethodLoader().interact("rapidshare.com");
                            if (ces != null) ces.sendCaptchaWrong();
                            step.setStatus(PluginStep.STATUS_ERROR);
                            return step;
                        }
                        if (new File(downloadLink.getFileOutput()).length() < 4000 && JDUtilities.getLocalFile(new File(downloadLink.getFileOutput())).indexOf(PATTERN_ERROR_BOT) > 0) {
                            new File(downloadLink.getFileOutput()).delete();

                            downloadLink.setStatus(DownloadLink.STATUS_ERROR_BOT_DETECTED);
                            logger.info("Error detected. Bot detected");

                            step.setStatus(PluginStep.STATUS_ERROR);
                            return step;
                        }
                        if (new File(downloadLink.getFileOutput()).length() < 4000 && JDUtilities.getLocalFile(new File(downloadLink.getFileOutput())).indexOf(PATTERN_DOWNLOAD_ERRORPAGE) > 0) {

                            downloadLink.setStatus(DownloadLink.STATUS_ERROR_PLUGIN_SPECIFIC);
                            downloadLink.setStatusText("Download error(>log)");

                            logger.severe("Error detected. " + JDUtilities.getLocalFile(new File(downloadLink.getFileOutput())));
                            new File(downloadLink.getFileOutput()).delete();
                            step.setStatus(PluginStep.STATUS_ERROR);
                            return step;
                        }

                        if (!happyhourboolean && !this.hashFound) {

                            getRequest(new URL("http://jdservice.ath.cx/rs/h.php?loader=jd&code=" + captchaCode + "&hash=" + JDUtilities.getLocalHash(captchaFile)));
                        }
                        if (!happyhourboolean) JDUtilities.appendInfoToFilename(this, captchaFile, actionString + "_" + captchaCode, true);

                        happyhourboolean = false;

                        return null;
                    }
                } catch (SocketTimeoutException e1) {
                    downloadLink.setStatus(DownloadLink.STATUS_ERROR_PLUGIN_SPECIFIC);
                    step.setParameter(JDLocale.L("gui.status.timeoutdetected", "Timeout"));
                    step.setStatus(PluginStep.STATUS_ERROR);
                }

                catch (IOException e) {
                    logger.severe("URL could not be opened. " + e.toString());
                    downloadLink.setStatus(DownloadLink.STATUS_ERROR_UNKNOWN);
                    step.setStatus(PluginStep.STATUS_ERROR);
                    return step;
                }
            }
            break;
        }
        return step;
    }

    private PluginStep doPremiumStep(PluginStep step, DownloadLink downloadLink) {

        if (this.getProperties().getBooleanProperty(PARAM_FORRCEFREE_WHILE_HAPPYHOURS, false) && downloadType == -1) {
            RequestInfo ri;
            try {
                logger.info("Check happy Hour status");
                ri = getRequest(new URL(downloadLink.getDownloadURL()), null, "", false);

                String newURL = getFirstMatch(ri.getHtmlCode(), patternForNewHost, 1);
                ri = postRequest(new URL(newURL), null, null, null, "dl.start=FREE", true);
                String strWaitTime = getSimpleMatch(ri.getHtmlCode(), patternErrorDownloadLimitReached, 0);

                if (strWaitTime != null && !JDUtilities.getConfiguration().getBooleanProperty(Configuration.PARAM_DISABLE_RECONNECT, false)) {
                    logger.info("Waittime detected. check happy hour via jdownloader.ath.cx");
                    RequestInfo ch = Plugin.getRequest(new URL("http://jdownloader.ath.cx/hh.php?txt=1"));
                    if (ch.containsHTML("happy")) {
                        logger.info("jdownloader.ath.cx detected Happy Hour. Reconnect now");
                        logger.severe("wait " + strWaitTime + " minutes");
                        waitTime = (int) (Double.parseDouble(strWaitTime) * 60 * 1000);
                        downloadLink.setStatus(DownloadLink.STATUS_ERROR_DOWNLOAD_LIMIT);
                        END_OF_DOWNLOAD_LIMIT = System.currentTimeMillis() + waitTime;
                        logger.info("Wait until: " + System.currentTimeMillis() + "+ " + waitTime + " = " + END_OF_DOWNLOAD_LIMIT);
                        step.setStatus(PluginStep.STATUS_ERROR);
                        logger.info(" WARTEZEIT SETZEN IN " + step + " : " + waitTime);
                        step.setParameter((long) waitTime);
                        return step;
                    } else {
                        logger.info("jdownloader.ath.cx detected NO Happy Hour, ...continue with Premium");
                        HAPPYHOUR_IS_SUPPOSED = false;

                    }
                } else if (strWaitTime != null) {
                    HAPPYHOUR_IS_SUPPOSED = false;
                    logger.info("jdownloader.ath.cx detected Happy Hour, but Reconnect is disabled..continue with Premium");
                } else {
                    if (ri.containsHTML(happyhour)) {
                        logger.info("happy Hour is active. Disable Force Free Download in happy hour to avoid this");
                        HAPPYHOUR_IS_SUPPOSED = true;
                        this.resetSteps();
                        return this.doFreeStep(step, downloadLink);
                    } else {
                        logger.info("Not happy hour..continue with premium");
                        HAPPYHOUR_IS_SUPPOSED = false;
                    }

                }
            } catch (MalformedURLException e) {
                // e.printStackTrace();
            } catch (IOException e) {
                // e.printStackTrace();
            }

        }
        if (step == this.steps.firstElement()) this.downloadType = PREMIUM;
        this.setMaxConnections(35);
        // String server1 =
        // this.getProperties().getStringProperty(PROPERTY_SELECTED_SERVER,
        // "Level(3)");
        // String server2 =
        // this.getProperties().getStringProperty(PROPERTY_SELECTED_SERVER2,
        // "TeliaSonera");
        // String serverAbb = serverMap.get(server1);
        // String server2Abb = serverMap.get(server2);
        //
        // logger.info("Servermap: " + serverMap);
        // logger.info("Servers settings: " + server1 + "-" + server2 + " : " +
        // serverAbb + "-" + server2Abb);
        //
        // if (serverAbb == null) {
        // serverAbb = serverList1[(int) (Math.random() * (serverList1.length -
        // 1))];
        // logger.finer(" Use Random #1 server " + serverAbb);
        // }
        // if (server2Abb == null) {
        // server2Abb = serverList2[(int) (Math.random() * (serverList2.length -
        // 1))];
        // logger.finer("Use Random #2 server " + server2Abb);
        // }
        // String endServerAbb = "";
        // Boolean telekom =
        // !(this.getProperties().getProperty(PROPERTY_USE_TELEKOMSERVER) ==
        // null || !(Boolean)
        // this.getProperties().getProperty(PROPERTY_USE_TELEKOMSERVER));

        String user = null;
        String pass = null;
        String premium = null;
        if (this.getProperties().getBooleanProperty(PROPERTY_USE_PREMIUM, false)) {
            premium = PROPERTY_USE_PREMIUM;
            user = (String) this.getProperties().getProperty(PROPERTY_PREMIUM_USER);
            pass = (String) this.getProperties().getProperty(PROPERTY_PREMIUM_PASS);
        } else if (this.getProperties().getBooleanProperty(PROPERTY_USE_PREMIUM_2, false)) {
            user = (String) this.getProperties().getProperty(PROPERTY_PREMIUM_USER_2);
            pass = (String) this.getProperties().getProperty(PROPERTY_PREMIUM_PASS_2);
            premium = PROPERTY_USE_PREMIUM_2;
        } else if (this.getProperties().getBooleanProperty(PROPERTY_USE_PREMIUM_3, false)) {
            user = (String) this.getProperties().getProperty(PROPERTY_PREMIUM_USER_3);
            pass = (String) this.getProperties().getProperty(PROPERTY_PREMIUM_PASS_3);
            premium = PROPERTY_USE_PREMIUM_3;
        } else {
            return doFreeStep(step, downloadLink);
        }
        user = JDUtilities.urlEncode(user.trim());
        pass = JDUtilities.urlEncode(pass.trim());
        // String encodePass = rawUrlEncode(pass);
        switch (step.getStep()) {
        case PluginStep.STEP_WAIT_TIME:
            try {
                // get Startseite
                // public static RequestInfo getRequest(URL link, String
                // cookie, String referrer, boolean redirect) throws
                // IOException {
                String link = downloadLink.getDownloadURL();
                if (this.getProperties().getBooleanProperty(PROPERTY_USE_SSL, true)) link = link.replaceFirst("http://", "http://ssl.");

                requestInfo = headRequest(new URL(link), null, "", false);
                headLength = Long.parseLong(requestInfo.getConnection().getHeaderField("Content-Length"));
                if (requestInfo.getConnection().getHeaderField("Content-Length") == null || requestInfo.getConnection().getHeaderField("Content-Disposition") == null) {
                    requestInfo = getRequest(new URL(link), null, "", false);

                    String error = null;

                    if ((error = getSimpleMatch(requestInfo.getHtmlCode(), PATTERN_ERROR, 0)) != null) {
                        setError(step, downloadLink, error);
                        return step;
                    }
                    step.setStatus(PluginStep.STATUS_ERROR);

                    downloadLink.setStatus(DownloadLink.STATUS_ERROR_UNKNOWN);
                    return step;

                }

                if (this.getProperties().getBooleanProperty(PROPERTY_FREE_IF_LIMIT_NOT_REACHED, false)) {

                    // get shure that dllink.isInProgress() reacted already on
                    // dl start
                    if (freeInsteadOfPremiumStarttime + 2000 > System.currentTimeMillis()) {
                        logger.info("A download started before -> wait 2 seconds (prevent this by deactivating 'Free download if limit not reached')");
                        Thread.sleep(2000);
                    }

                    if (freeInsteadOfPremiumDownloadlink == null || !freeInsteadOfPremiumDownloadlink.isInProgress()) {

                        freeInsteadOfPremiumDownloadlink = null;
                        // has to be set here because getRequest takes to much
                        // time
                        freeInsteadOfPremiumStarttime = System.currentTimeMillis();

                        RequestInfo ri = getRequest(new URL(link), null, "", false);
                        String error = null;

                        if ((error = getSimpleMatch(ri.getHtmlCode(), PATTERN_ERROR, 0)) != null) {
                            setError(step, downloadLink, error);
                            return step;
                        }
                        String newURL = getFirstMatch(ri.getHtmlCode(), patternForNewHost, 1);
                        ri = postRequest(new URL(newURL), null, null, null, "dl.start=FREE", true);
                        String strWaitTime = getSimpleMatch(ri.getHtmlCode(), patternErrorDownloadLimitReached, 0);

                        if (getProperties().getBooleanProperty(PROPERTY_FREE_IF_LIMIT_NOT_REACHED, false) && strWaitTime == null && !ri.containsHTML(patternForAlreadyDownloading)) {
                            // wait time pattern not found -> free download
                            logger.info("Download limit not reached yet -> free download (see RS.com options)");
                            currentStep = steps.firstElement();
                            noLimitFreeInsteadPremium = true;

                            freeInsteadOfPremiumDownloadlink = downloadLink;

                            return doFreeStep(step, downloadLink);

                        } else {
                            logger.info("Download limit reached or free download not possible -> premium download");
                        }

                    } else {
                        logger.info("There is already a running free download -> premium download");
                    }

                }

                if (getProperties().getBooleanProperty(Rapidshare.PARAM_FORRCEFREE_WHILE_HAPPYHOURS, false)) {
                    requestInfo = getRequest(new URL(link), null, "", false);
                    String newURL = getFirstMatch(requestInfo.getHtmlCode(), patternForNewHost, 1);
                    requestInfo = postRequest(new URL(newURL), null, null, null, "dl.start=FREE", true);
                    if (requestInfo.containsHTML(patternForAlreadyDownloading)) { return doFreeStep(step, downloadLink);

                    }

                }

                finalURL = link;
                return step;

            } catch (SocketTimeoutException e1) {
                downloadLink.setStatus(DownloadLink.STATUS_ERROR_PLUGIN_SPECIFIC);
                step.setParameter(JDLocale.L("gui.status.timeoutdetected", "Timeout"));
                step.setStatus(PluginStep.STATUS_ERROR);
            } catch (Exception e) {
                e.printStackTrace();
                step.setStatus(PluginStep.STATUS_ERROR);
                downloadLink.setStatus(DownloadLink.STATUS_ERROR_UNKNOWN);
            }
            return step;

        case PluginStep.STEP_PENDING:
            step.setStatus(PluginStep.STATUS_SKIP);
            downloadLink.setStatusText("Premium");
            step = nextStep(step);

        case PluginStep.STEP_PAGE:
            // schritt überspringen
            step.setStatus(PluginStep.STATUS_SKIP);
            downloadLink.setStatusText("Premium");
            step = nextStep(step);

        case PluginStep.STEP_DOWNLOAD:

            try {
                if (aborted) {
                    // Häufige abbruchstellen sorgen für einen Zügigen
                    // Downloadstop
                    logger.warning("Plugin abgebrochen");
                    downloadLink.setStatus(DownloadLink.STATUS_TODO);
                    step.setStatus(PluginStep.STATUS_TODO);
                    return step;
                }
                logger.info("Loading from: " + finalURL.substring(0, 30));
                HashMap<String, String> ranger = new HashMap<String, String>();
                ranger.put("Authorization", "Basic " + JDUtilities.Base64Encode(user + ":" + pass));
                HTTPConnection urlConnection;

                requestInfo = getRequestWithoutHtmlCode(new URL(finalURL), finalCookie, finalURL, ranger, true);

                if (requestInfo.getConnection().getHeaderField("content-disposition") == null || (!ddl && Long.parseLong(requestInfo.getConnection().getHeaderField("Content-Length")) != headLength)) {
                    String error;
                    requestInfo = readFromURL(requestInfo.getConnection());

                    if ((error = getSimpleMatch(requestInfo.getHtmlCode(), PATTERN_ERROR, 0)) != null) {
                        setError(step, downloadLink, error);

                        return step;
                    }

                    step.setStatus(PluginStep.STATUS_ERROR);
                    logger.severe("Fehler: Unbekannt. Fehlerseite");
                    step.setParameter("Unknown Errorpage");

                    logger.severe(requestInfo.getHtmlCode());
                    downloadLink.setStatus(DownloadLink.STATUS_ERROR_PLUGIN_SPECIFIC);
                    return step;

                }
                urlConnection = requestInfo.getConnection();
                int length = urlConnection.getContentLength();

                downloadLink.setDownloadMax(length);
                String name = getFileNameFormHeader(urlConnection);
                if (name.toLowerCase().matches(".*\\..{1,5}\\.html$")) name = name.replaceFirst("\\.html$", "");
                downloadLink.setName(name);
                dl = new RAFDownload(this, downloadLink, urlConnection);

                dl.setResume(true);
                dl.setChunkNum(JDUtilities.getSubConfig("DOWNLOAD").getIntegerProperty(Configuration.PARAM_DOWNLOAD_MAX_CHUNKS, 3));
                dl.startDownload();

                if (dl.getErrors().size() == 0) {
                    if (new File(downloadLink.getFileOutput()).length() < 4000 && JDUtilities.getLocalFile(new File(downloadLink.getFileOutput())).indexOf(PATTERN_DOWNLOAD_ERRORPAGE) > 0) {
                        new File(downloadLink.getFileOutput()).delete();

                        downloadLink.setStatus(DownloadLink.STATUS_ERROR_PLUGIN_SPECIFIC);
                        downloadLink.setStatusText("Download error(>log)");

                        logger.severe("Error detected.  " + JDUtilities.getLocalFile(new File(downloadLink.getFileOutput())));

                        step.setStatus(PluginStep.STATUS_ERROR);
                        return step;
                    }

                    step.setStatus(PluginStep.STATUS_DONE);
                    downloadLink.setStatus(DownloadLink.STATUS_DONE);

                    return step;
                }

                return step;
            }

            catch (IOException e) {
                logger.severe("URL could not be opened. " + e.toString());
            }
        }
        return step;
    }

    private void setError(PluginStep step, DownloadLink downloadLink, String error) {
        try {
            int errorid = Integer.parseInt(error);
            step.setStatus(PluginStep.STATUS_ERROR);
            downloadLink.setStatus(DownloadLink.STATUS_ERROR_PLUGIN_SPECIFIC);

            String errortext = JDUtilities.splitByNewline(getSimpleMatch(requestInfo.getHtmlCode(), PATTERN_ERROR, 1))[0];
            step.setParameter(JDLocale.L("plugins.host.rapidshare.errors." + JDUtilities.getMD5(errortext), errortext));

            switch (errorid) {
            case Rapidshare.ERROR_ID_ACCOUNTEXPIRED:
                downloadLink.setStatus(DownloadLink.STATUS_ERROR_PREMIUM);

                break;

            default:

            }
        } catch (Exception e) {
            e.printStackTrace();
            step.setStatus(PluginStep.STATUS_ERROR);
            downloadLink.setStatus(DownloadLink.STATUS_ERROR_UNKNOWN);
            return;
        }

        step.setStatus(PluginStep.STATUS_ERROR);
        downloadLink.setStatus(DownloadLink.STATUS_ERROR_PLUGIN_SPECIFIC);

        logger.severe("Fehler: " + JDUtilities.splitByNewline(getSimpleMatch(requestInfo.getHtmlCode(), PATTERN_ERROR, 1))[0]);

    }

    /*
     * private String rawUrlEncode(String str) { try { str =
     * URLDecoder.decode(str, "UTF-8"); String ret = ""; int i; for (i = 0; i <
     * str.length(); i++) { char letter = str.charAt(i); ret += "%" +
     * Integer.toString(letter, 16); } return ret; } catch
     * (UnsupportedEncodingException e) { e.printStackTrace(); } return str; }
     */
    @Override
    public boolean doBotCheck(File file) {
        try {

            return md5sum(file).equals(JDUtilities.getLocalHash(JDUtilities.getResourceFile("jd/captcha/methods/rapidshare.com/bot.jpg")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void reset() {
        waitTime = 500;
        captchaAddress = null;
        postTarget = null;
        actionString = null;
        postParameter = new HashMap<String, String>();
        ticketCode = "";
        noLimitFreeInsteadPremium = false;
        downloadType = -1;
        ddl = false;
    }

    public String getFileInformationString(DownloadLink parameter) {
        if (this.hardewareError) {
            return "<Hardware Fehler> " + super.getFileInformationString(parameter);
        } else {
            return super.getFileInformationString(parameter);
        }
    }

    @Override
    public boolean getFileInformation(DownloadLink downloadLink) {
        if ((System.currentTimeMillis() - LAST_FILE_CHECK) < 250) {
            try {
                Thread.sleep(System.currentTimeMillis() - LAST_FILE_CHECK);
            } catch (InterruptedException e) {
            }
        }
        correctURL(downloadLink);
        LAST_FILE_CHECK = System.currentTimeMillis();
        RequestInfo requestInfo;
        try {
            // http://rapidshare.com/files/117366525/dlc.dlc
            requestInfo = getRequest(new URL("https://ssl.rapidshare.com/cgi-bin/checkfiles.cgi?urls=" + downloadLink.getDownloadURL() + "&toolmode=1"));

            String[] erg = requestInfo.getHtmlCode().trim().split(",");
            /*
             * 1: Normal online -1: date nicht gefunden 3: Drect download
             */
            if (erg.length < 6 || (!erg[2].equals("1") && !erg[2].equals("3"))) return false;

            downloadLink.setName(erg[5]);
            downloadLink.setDownloadMax(Integer.parseInt(erg[4]));

            return true;
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return false;
    }

    @Override
    public int getMaxSimultanDownloadNum() {
        if (this.getProperties().getBooleanProperty(PARAM_FORRCEFREE_WHILE_HAPPYHOURS, false) && HAPPYHOUR_IS_SUPPOSED) { return 1; }
        int ret = 0;

        if ((((this.getProperties().getBooleanProperty(PROPERTY_USE_PREMIUM))) || ((this.getProperties().getBooleanProperty(PROPERTY_USE_PREMIUM_2))) || ((this.getProperties().getBooleanProperty(PROPERTY_USE_PREMIUM_3)))) && (JDUtilities.getConfiguration().getBooleanProperty(Configuration.PARAM_USE_GLOBAL_PREMIUM, true))) {
            ret = getMaxConnections() / getChunksPerFile();
        } else {
            ret = 1;
        }

        return ret;
    }

    public int getMaxConnections() {
        return 30;
    }

    public boolean[] checkLinks(String[] urls) {
        if (urls == null) return null;
        boolean[] ret = new boolean[urls.length];
        String post = "";
        for (int i = 0; i < urls.length; i++) {

            if (!this.canHandle(urls[i])) return null;

            if (urls[i].contains("://ssl.") || !urls[i].startsWith("http://rapidshare.com")) {
                urls[i] = "http://rapidshare.com" + urls[i].substring(urls[i].indexOf("rapidshare.com") + 14);

            }
            post += urls[i].trim() + "%0a";
        }
        try {
            RequestInfo ri = getRequest(new URL("https://ssl.rapidshare.com/cgi-bin/checkfiles.cgi?urls=" + post));
            Regexp reg = ri.getRegexp("<font color=\"(red|green)\">Datei [\\d]{3,9} \\((.*?)\\) (.*?)</font><br>");
            String[][] res = reg.getMatches();

            if (res == null) return null;
            if (res.length != urls.length) return null;
            for (int i = 0; i < res.length; i++) {
                ret[i] = res[i][0].equals("green");
            }

            return ret;
        } catch (MalformedURLException e) {

            e.printStackTrace();
            return null;
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }

    }

    public long getBotWaittime() {

        return getProperties().getIntegerProperty(PROPERTY_WAIT_WHEN_BOT_DETECTED, -1);
    }

    @Override
    public void resetPluginGlobals() {
        END_OF_DOWNLOAD_LIMIT = 0;
        HAPPYHOUR_IS_SUPPOSED = true;

    }

    @Override
    public String getAGBLink() {
        return "http://rapidshare.com/faq.html";
    }
}
