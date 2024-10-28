# Login Service via Spring Security + JWT
</br>

### Was ist JWT?
---
JWT (JSON Web Token) ist ein offener Standard zur sicheren Übertragung von Informationen zwischen Parteien als JSON-Objekt.</br>
JWT besteht aus drei Teilen:

>**Header**: Enthält Informationen über den Typ des Tokens und das verwendete Verschlüsselungsverfahren.<br/>
>**Payload**: Enthält die verschlüsselten Informationen.<br/>
>**Signature**: Enthält die Signatur, die die Integrität des Tokens gewährleistet.
</br>
</br>

### Authentifizierungsprozess in Spring Security
---

</br>

<center>
  <img src="https://github.com/user-attachments/assets/59ada5b2-5a0c-4e9e-a971-b7735b00fe13" width="70%" height="70%">
</center>
<br/>
<br/>
<ol>
  <li>Der Benutzer sendet eine Authentifizierungsanfrage mit seinen Anmeldeinformationen (HTTP-Anfrage).</li>
  <li>Der `AuthenticationFilter` fängt die Anfrage ab und erstellt ein Authentifizierungsobjekt vom Typ `UsernamePasswordAuthenticationToken` mit den abgefangenen Informationen.</li>
  <li>Dieses Objekt wird an den `ProviderManager`, der eine Implementierung des `AuthenticationManager` ist, übergeben.</li>
  <li>Der `AuthenticationManager` ruft die registrierten `AuthenticationProvider` ab und fordert die Authentifizierung an.</li>
  <li> Er übergibt die Benutzerinformationen an den `UserDetailsService`, der die Benutzerauthentifizierungsdaten aus der Datenbank abruft.</li>
  <li>Basierend auf den empfangenen Benutzerinformationen wird ein `UserDetails`-Objekt erstellt, das die in der Datenbank gefundenen Benutzerinformationen enthält.</li>
  <li> Die `AuthenticationProvider` empfangen das `UserDetails`-Objekt und vergleichen die Benutzerinformationen.</li>
  <li>Sobald die Authentifizierung abgeschlossen ist, gibt der `AuthenticationProvider` ein `Authentication`-Objekt zurück, das Informationen über die Berechtigungen und den Benutzer enthält.</li>
  <li>Das `Authentication`-Objekt wird dann an den ursprünglichen `AuthenticationFilter` zurückgegeben.</li>
  <li>Schließlich wird das `Authentication`-Objekt im `SecurityContext` gespeichert.</li>
    
</ol>






