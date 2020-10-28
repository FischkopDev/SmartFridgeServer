<h2>Aufbau und Start des Servers</h2>

<h4>Aufbau</h4>
<p>Der Server ist modular aufgebaut und kann mit wenigen Befehlen und per Plugins erweitert werden.
Eine API dafür finden Sie hier.<br>
Der Server reagiert auf Anfragen, die vom Client per Socket-Verbindung kommen. Um Die Kommunikation zuschützen,
kann man ebenfalls ein SSL-Zertifikat hinterlegen. Für die Sicherheit innerhalb der Datenbank sorgt ebenfalls der Server,
was durch das Hashen von empfindlichen Daten geschieht. Von der Benutzerverwaltung bis zur eigentlichen
Funktion, der Bereitstellung von Rezepten kann der Server einige Befehle bearbeiten.</p>

<h4>Einstellungen vor dem Start</h4>
<p>Bevor der Server gestartet wird, muss in dem Ordner "Config" die mysql.json bearbeitet werden, damit auf die richtige
Datenbank zugegriffen wird.</p>

<h4>Server Starten</h4>
<p>Die beigelegte Java-Datei ausführen. Daraufhin erstellt das Programm ein paar Dateien worunter auch
Config-Files fallen, welche für den individuellen Betrieb angepasst werden müssen. Hat man das erledigt, kann
man den Server erneut starten und sollte keine Fehler bekommen.</p>

<h4>Konsole</h4>
<p>Der Server besitzt eine Konsole die Befehle annehmen kann. Mit /help werden diese aufgelistet.</p>

<h4>Autor</h4>

<p>Bei Problemen einfach über folgende Wege melden:<br><br>
Timo Skrobanek<br>
Email: timo@home-skrobanek.de<br>
Discord: Dr.Fischkopf#6752</p>