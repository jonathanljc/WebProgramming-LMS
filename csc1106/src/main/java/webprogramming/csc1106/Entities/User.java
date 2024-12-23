package webprogramming.csc1106.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "joined_date", nullable = false)
    private Date joinedDate;

    @Column(name = "joined_time", nullable = false)
    private Time joinedTime;

    @Column(name = "user_balance", nullable = false, precision = 38, scale = 2)
    private BigDecimal userBalance;

    @Column(name = "last_login", nullable = true)
    private Timestamp lastLogin;

    @Column(name = "login_cookie", nullable = true)
    private String loginCookie;

    @Column(name = "profile_picture", nullable = true)
    private String profilePicture;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transactions> transactions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes;
    
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subscription> subscriptions;

    // Default constructor
    public User() {}

    // Parameterized constructor
    public User(Integer userID, Roles role, String userName, String userPassword, String userEmail, Date joinedDate, Time joinedTime, BigDecimal userBalance, Timestamp lastLogin, String loginCookie) {
        this.userId = userID;
        this.role = role;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.joinedDate = joinedDate;
        this.joinedTime = joinedTime;
        this.userBalance = userBalance;
        this.lastLogin = lastLogin;
        this.loginCookie = loginCookie;
        this.profilePicture = null;
    }

    // Getters and setters
    public Integer getUserID() {
        return userId;
    }

    public void setUserID(Integer userID) {
        this.userId = userID;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }

    public Time getJoinedTime() {
        return joinedTime;
    }

    public void setJoinedTime(Time joinedTime) {
        this.joinedTime = joinedTime;
    }

    public BigDecimal getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(BigDecimal userBalance) {
        this.userBalance = userBalance;
    }

    public List<Transactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transactions> transactions) {
        this.transactions = transactions;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLoginCookie() {
        return loginCookie;
    }

    public void setLoginCookie(String loginCookie) {
        this.loginCookie = loginCookie;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public List<Likes> getLikes() {
        return likes;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    // Generate Random String with Numbers
    private static Random random = new Random();

    public String generateRandomCookie(int length){
        String cookieCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + 
                            "abcdefghijklmnopqrstuvwxyz" + 
                            "0123456789" + 
                            "!@#$%^&*()";

        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            builder.append(cookieCharacters.charAt(random.nextInt(cookieCharacters.length())));
        }

        return builder.toString();
    }
}

