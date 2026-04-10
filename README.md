# SysCongé - Système de Gestion des Demandes de Congés du Personnel Universitaire

## Description
Application web Jakarta EE multi-tiers pour la gestion des demandes de congés du personnel universitaire. Développée en respectant strictement l'architecture Java EE classique (Servlet → EJB → JPA) sans framework additionnel.

## Technologies utilisées

| Technologie | Utilisation |
|-------------|-------------|
| **Servlet** | Contrôleurs (doGet, doPost), forwarding, gestion des requêtes/réponses |
| **JSP** | Vues dynamiques (affichage, formulaires) - JSTL + EL exclusivement |
| **JSTL** | Boucles (`<c:forEach>`), conditions (`<c:if>`, `<c:choose>`), formatage (`<fmt:...>`) |
| **EJB** | Logique métier (Session Beans Stateless) - 6 Session Beans |
| **JPA** | Entités, relations (@OneToMany, @ManyToOne...), EntityManager, JPQL - 8 entités |
| **CDI** | Injection dans Servlets et EJBs (@Inject) |
| **Serveur** | GlassFish 7+ (Jakarta EE 10) |

## Architecture du projet

```
src/main/java/com/sysconge/
├── entity/          # 8 entités JPA
│   ├── Utilisateur.java
│   ├── Personnel.java
│   ├── Departement.java
│   ├── TypeConge.java
│   ├── DemandeConge.java
│   ├── Approbation.java
│   ├── SoldeConge.java
│   └── Notification.java
├── ejb/             # 6 EJB Session Beans (Stateless)
│   ├── UtilisateurEJB.java
│   ├── DemandeCongeEJB.java
│   ├── ApprobationEJB.java
│   ├── SoldeCongeEJB.java
│   ├── NotificationEJB.java
│   └── DepartementEJB.java
├── servlet/         # 7 Servlet Contrôleurs
│   ├── AuthServlet.java
│   ├── DashboardServlet.java
│   ├── DemandeCongeServlet.java
│   ├── ApprobationServlet.java
│   ├── NotificationServlet.java
│   ├── AdminServlet.java
│   └── ProfilServlet.java
└── util/            # Utilitaires
    ├── AuthFilter.java
    ├── EncodingFilter.java
    └── DataInitializer.java

src/main/webapp/
├── WEB-INF/
│   ├── web.xml
│   ├── beans.xml
│   └── jsp/         # Vues JSP (JSTL + EL, zéro scriptlet)
│       ├── common/  (header, footer, 404, 500)
│       ├── login.jsp
│       ├── dashboard.jsp
│       ├── profil.jsp
│       ├── demande/  (liste, nouvelle, detail)
│       ├── approbation/ (liste, detail, historique)
│       ├── notification/ (liste)
│       └── admin/   (utilisateurs, départements, types-conge, demandes)
├── css/style.css
└── index.jsp
```

## Fonctionnalités

### Rôles utilisateur
- **PERSONNEL** : Soumettre, consulter et annuler des demandes de congé
- **CHEF_DEPARTEMENT** : Approuver/refuser les demandes de son département
- **DRH** : Validation finale des demandes approuvées par le chef
- **ADMIN** : Gestion des utilisateurs, départements et types de congé

### Workflow d'approbation
1. Le personnel soumet une demande → statut **EN_ATTENTE**
2. Le chef de département approuve → statut **APPROUVEE_CHEF**
3. La DRH valide → statut **APPROUVEE_DRH** (solde décrémenté automatiquement)

À chaque étape, le demandeur est notifié et le refus est possible.

### Entités JPA (8 entités)
1. **Utilisateur** - Authentification et rôles
2. **Personnel** - Informations du personnel (lié à Utilisateur)
3. **Departement** - Départements universitaires
4. **TypeConge** - Types de congé (annuel, maladie, maternité, etc.)
5. **DemandeConge** - Demandes de congé avec workflow
6. **Approbation** - Décisions d'approbation/refus
7. **SoldeConge** - Soldes de congé par personnel et type
8. **Notification** - Notifications système

## Prérequis
- **JDK 17+**
- **Maven 3.8+**
- **GlassFish 7+** (ou WildFly 27+)
- Base de données (H2, MySQL, PostgreSQL)

## Installation et déploiement

### 1. Configuration de la base de données
Créer une datasource JDBC nommée `jdbc/sysconge` dans GlassFish :

```bash
# Exemple avec MySQL
asadmin create-jdbc-connection-pool \
  --datasourceclassname com.mysql.cj.jdbc.MysqlDataSource \
  --restype javax.sql.DataSource \
  --property url=jdbc\\:mysql\\://localhost\\:3306/sysconge:user=root:password=root \
  SysCongePool

asadmin create-jdbc-resource --connectionpoolid SysCongePool jdbc/sysconge
```

### 2. Build du projet
```bash
mvn clean package
```

### 3. Déploiement
```bash
# Copier le WAR dans GlassFish
cp target/sysconge.war $GLASSFISH_HOME/glassfish/domains/domain1/autodeploy/

# Ou via l'admin console de GlassFish
```

### 4. Accès
Naviguer vers : `http://localhost:8080/sysconge/`

## Comptes de démonstration
| Rôle | Email | Mot de passe |
|------|-------|--------------|
| Admin | admin@univ.ma | admin123 |
| DRH | drh@univ.ma | drh123 |
| Chef de département | chef.info@univ.ma | chef123 |
| Personnel | ahmed.idrissi@univ.ma | pers123 |
| Personnel | khadija.tazi@univ.ma | pers123 |
| Personnel | omar.fassi@univ.ma | pers123 |