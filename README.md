<div align="center">

<img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
<img src="https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white" />
<img src="https://img.shields.io/badge/Apache_Tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=black" />
<img src="https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white" />
<img src="https://img.shields.io/badge/Status-Production_Ready-brightgreen?style=for-the-badge" />

# 💸 Personal Expense Tracker

### _Track smarter. Spend wiser._

A **production-grade** Java web application for personal finance management — built from scratch with zero frameworks, pure Java Servlets, JDBC, JSP, and Bootstrap 5. Clean architecture. Rock-solid security. Beautifully simple UI.

[Features](#-features) · [Tech Stack](#️-tech-stack) · [Architecture](#️-architecture) · [Getting Started](#-getting-started) · [API Endpoints](#-api-endpoints) · [Security](#️-security) · [Roadmap](#-roadmap)

</div>

---

## ✨ Features

| Feature | Description |
|---|---|
| ➕ **Add Expenses** | Capture category, description, amount, and date with full validation |
| 📋 **View All** | Sorted expense list with totals, record count, and category breakdown |
| ✏️ **Edit & Update** | Modify any expense inline — no page reload needed |
| 🗑️ **Delete** | Remove entries with one click |
| 🔍 **Filter by Category** | Drill into Food, Travel, Utilities, and more |
| 📊 **Live Statistics** | Running totals and per-category summaries at a glance |
| 🛡️ **Secure by Design** | SQL injection prevention, XSS protection, HttpOnly cookies |

---

## 🛠️ Tech Stack

```
┌─────────────────────────────────────────────────────────────┐
│                      Browser (Client)                       │
│              HTML5  ·  Bootstrap 5  ·  CSS3                 │
├─────────────────────────────────────────────────────────────┤
│               Apache Tomcat 10.0+ (Server)                  │
├──────────────────────────┬──────────────────────────────────┤
│   Controller             │   View                          │
│   Java Servlet           │   JSP + JSTL + EL               │
├──────────────────────────┴──────────────────────────────────┤
│                  DAO Layer (JDBC)                           │
│          PreparedStatement  ·  Try-with-resources           │
├─────────────────────────────────────────────────────────────┤
│                    MySQL 5.7+                               │
└─────────────────────────────────────────────────────────────┘
```

| Layer | Technology | Why |
|---|---|---|
| **Backend** | Core Java (no Spring) | Full control, no magic |
| **Database** | MySQL 5.7+ | Reliable, widely supported |
| **Data Access** | JDBC + PreparedStatement | SQL injection prevention built-in |
| **Servlet** | Java Servlets (doGet/doPost) | Lightweight, standard |
| **View** | JSP + JSTL + EL | Zero scriptlets, clean separation |
| **Frontend** | Bootstrap 5, HTML5, CSS3 | Responsive out of the box |
| **Server** | Apache Tomcat 10.0+ | Industry-standard servlet container |

---

## 🏗️ Architecture

This project strictly follows **MVC (Model-View-Controller)** with an additional DAO layer for clean database separation.

```
src/main/java/com/expense/
│
├── model/
│   └── Expense.java          ← POJO · data only · no business logic
│
├── dao/
│   └── ExpenseDAO.java       ← ALL database operations · CRUD methods
│
├── servlet/
│   └── ExpenseServlet.java   ← Front controller · routing · validation
│
└── util/
    └── DBConnection.java     ← JDBC setup · single responsibility

src/main/webapp/
│
├── WEB-INF/
│   └── web.xml               ← Deployment descriptor
│
├── index.jsp                 ← Expense list + statistics
├── add-expense.jsp           ← Add new expense form
├── edit-expense.jsp          ← Edit existing expense form
│
└── css/                      ← Bootstrap 5 via CDN

database/
└── expense_tracker_schema.sql  ← Schema + sample data
```

### Layer Responsibilities

**🟦 Model** — `Expense.java`
> Pure POJO. Private fields, getters, setters. No business logic. Serializable for clean data transfer.

**🟩 View** — JSP Pages
> Zero Java code — no scriptlets, ever. JSTL handles loops and conditionals. EL binds data. Bootstrap handles layout.

**🟨 Controller** — `ExpenseServlet.java`
> Routes every request via `action` parameter. `doGet()` for reads, `doPost()` for writes. Validates input, handles errors, forwards to JSP.

**🟥 DAO** — `ExpenseDAO.java`
> The only layer that talks to MySQL. PreparedStatements everywhere. Try-with-resources for safe connection handling. No business logic leaks in.

**⬜ Utility** — `DBConnection.java`
> One job: provide a JDBC connection. Single responsibility, no bloat.

---

## 🔄 Request Flow

```
Browser
  │
  │  HTTP Request (?action=...)
  ▼
ExpenseServlet
  ├── doGet()  → list · edit · filterByCategory
  └── doPost() → add  · update · delete
  │
  │  switch(action)
  ▼
Business Operation
  │
  ▼
ExpenseDAO ──────→ MySQL
  │                  │
  │◄─── ResultSet ───┘
  │
  │  Expense Objects → Request Attributes
  ▼
JSP Forward
  │
  ▼
HTML Response → Browser
```

---

## 🚀 Getting Started

### Prerequisites

- Java JDK 11+
- Apache Tomcat 10.0+
- MySQL 5.7+
- Maven (optional, for builds)

### 1. Clone the Repository

```bash
git clone https://github.com/red-coder-27/personal-expense-tracker.git
cd personal-expense-tracker
```

### 2. Set Up the Database

```bash
mysql -u root -p < database/expense_tracker_schema.sql
```

### 3. Configure the DB Connection

Edit `src/main/java/com/expense/util/DBConnection.java`:

```java
private static final String URL      = "jdbc:mysql://localhost:3306/expense_tracker";
private static final String USERNAME = "your_username";
private static final String PASSWORD = "your_password";
```

### 4. Build & Deploy

```bash
# Build WAR
mvn clean package

# Deploy to Tomcat
cp target/expense-tracker.war $CATALINA_HOME/webapps/
```

### 5. Open in Browser

```
http://localhost:8080/expense-tracker/expense?action=list
```

> 📖 Need more detail? See [`DEPLOYMENT_GUIDE.md`](./DEPLOYMENT_GUIDE.md) for full setup, troubleshooting, and Tomcat configuration.

---

## 📡 API Endpoints

| Method | URL | Action | Description |
|---|---|---|---|
| `GET` | `/expense?action=list` | List | View all expenses, sorted newest first |
| `GET` | `/expense?action=edit&id={id}` | Edit | Load expense into edit form |
| `GET` | `/expense?action=filterByCategory&category={cat}` | Filter | Filter by selected category |
| `POST` | `/expense?action=add` | Add | Submit new expense |
| `POST` | `/expense?action=update` | Update | Save edited expense |
| `POST` | `/expense?action=delete&id={id}` | Delete | Remove expense by ID |

---

## 🛡️ Security

### Already Implemented

- ✅ **SQL Injection** — `PreparedStatement` used throughout, no raw string queries
- ✅ **XSS Prevention** — JSTL/EL auto-escapes all output before rendering
- ✅ **Input Validation** — Server-side validation on all fields in the servlet layer
- ✅ **Secure Cookies** — `HttpOnly` flag configured in `web.xml`

### Recommended for Production

- 🔐 Add user **authentication** (session-based login)
- 🔑 Implement **role-based authorization**
- 🔒 Enable **HTTPS/SSL** via Tomcat connector or reverse proxy
- 🧱 Deploy behind a **Web Application Firewall (WAF)**

---

## 📊 Database Schema

```sql
CREATE TABLE expenses (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    category    VARCHAR(50)    NOT NULL,
    description VARCHAR(255)   NOT NULL,
    amount      DECIMAL(10,2)  NOT NULL,
    expense_date DATE          NOT NULL,
    created_at  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);
```

---

## 🗺️ Roadmap

### Phase 2 — Performance
- [ ] **Connection Pooling** with HikariCP
- [ ] **Pagination** for large datasets
- [ ] **Category caching** to reduce DB round-trips

### Phase 3 — Features
- [ ] **User Authentication** — session-based login system
- [ ] **Multi-user Support** — per-user expense isolation
- [ ] **Recurring Expenses** — automatic scheduled entries
- [ ] **Export to CSV/PDF** — download your expense history
- [ ] **Charts & Analytics** — spending trends over time

---

## 📚 References

- [Oracle JDBC Documentation](https://docs.oracle.com/javase/tutorial/jdbc/)
- [Java Servlet Specification](https://www.oracle.com/java/technologies/servlet-tech.html)
- [Apache Tomcat 10 Docs](https://tomcat.apache.org/tomcat-10.0-doc/)
- [Bootstrap 5 Documentation](https://getbootstrap.com/docs/5.0/)
- [JSTL Reference](https://javaee.github.io/jstl-api/)

---

## 🐞 Troubleshooting

| Problem | Fix |
|---|---|
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | Add MySQL connector JAR to `WEB-INF/lib/` |
| `Connection refused` on DB | Check MySQL is running and credentials in `DBConnection.java` |
| `404` on servlet URL | Confirm `web.xml` mapping and WAR deployed correctly |
| Blank JSP page | Check Tomcat logs: `$CATALINA_HOME/logs/catalina.out` |

---

<div align="center">

**Version 1.0** · Academic Release · Last Updated May 2026

Made with ☕ Java and a lot of `System.out.println` debugging

⭐ Star this repo if it helped you!

</div>