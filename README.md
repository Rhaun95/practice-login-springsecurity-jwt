# Mini-Projekt (Authentifizierung)

####
☀️ Projektbeschreibung 
---
Dies ist ein Mini-Projekt, das darauf abzielt, einn sichereren Authentifizierungsprozess zu erlernen, 
indem ich nicht nur einfache Sessions verwende, sondern Spring Security und JWT integriere. 
</br>
Meine Motivation liegt in der Erweiterung meines Wissens durch das Erlernen und Anwenden neuer Technologien.
</br>

Zu den Funktionen gehören grundlegende CRUD-Operationen wie _**Registrierung, Kontolöschung, Login, Logout, Informationsabfrage und Informationsaktualisierung**_.


#### 
🍁 Stacks 
---


| **Languages & Frameworks** | Java, Spring Boot, Spring Security, JavaScript, React |
|---------------------------|------------------------------------------------------|
| **Libraries**             | Lombok, JJWT                                         |
| **Database**              | MySQL                                                |
| **Environment**           | IntelliJ, Github, Git                                |


</br>


#### 
🔃 Authentifizierungsprozess in Spring Security
---

</br>

<div align="center" >
  <img  src="https://github.com/user-attachments/assets/59ada5b2-5a0c-4e9e-a971-b7735b00fe13"  width="60%" height="60%">
</div>
</br>

1. Der Benutzer sendet eine Authentifizierungsanfrage mit seinen Anmeldeinformationen (HTTP-Anfrage).
2. Der `AuthenticationFilter` fängt die Anfrage ab und erstellt ein Authentifizierungsobjekt vom Typ `UsernamePasswordAuthenticationToken` mit den abgefangenen Informationen.
3. Dieses Objekt wird an den `ProviderManager`, der eine Implementierung des `AuthenticationManager` ist, übergeben.
4. Der `AuthenticationManager` ruft die registrierten `AuthenticationProvider` ab und fordert die Authentifizierung an.
5. Er übergibt die Benutzerinformationen an den `UserDetailsService`, der die Benutzerauthentifizierungsdaten aus der Datenbank abruft.
6. Basierend auf den empfangenen Benutzerinformationen wird ein `UserDetails`-Objekt erstellt, das die in der Datenbank gefundenen Benutzerinformationen enthält.
7. Die `AuthenticationProvider` empfangen das `UserDetails`-Objekt und vergleichen die Benutzerinformationen.
8. Sobald die Authentifizierung abgeschlossen ist, gibt der `AuthenticationProvider` ein `Authentication`-Objekt zurück, das Informationen über die Berechtigungen und den Benutzer enthält.
9. Das `Authentication`-Objekt wird dann an den ursprünglichen `AuthenticationFilter` zurückgegeben.
10. Schließlich wird das `Authentication`-Objekt im `SecurityContext` gespeichert.

</br>

### 
📁 Folder Struktur
---
<div align="center" >
  <img src="https://github.com/user-attachments/assets/cc96782a-db47-4244-9c91-f63a734455b2" width="40%" height="50%">
  <img src="https://github.com/user-attachments/assets/7b9b7793-7f6a-4d48-989b-debd18e82481" width="30%" height="50%">
</div>



