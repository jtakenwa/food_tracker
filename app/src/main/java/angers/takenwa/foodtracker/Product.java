package angers.takenwa.foodtracker;

public class Product {
    private String codeBare;
    private String productName;
    private String grade;
    private String expirationDate;
    private long daysUntilExpiry;
    private double energy;
    private double energyKcal;
    private String energyUnit;
    private double fat100g;
    private double fat;
    private String fatUnit;
    private double proteins;
    private String proteinsUnit;
    private double salt;
    private String saltUnit;
    private double sugars;
    private String sugarsUnit;
    private String allergensTags;
    private String status;
    private String statusVerbose;
    private String imageUri;

    // Getters et setters pour chaque attribut

    public String getCodeBare() {
        return codeBare;
    }

    public void setCodeBare(String codeBare) {
        this.codeBare = codeBare;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public long getDaysUntilExpiry() {
        return daysUntilExpiry;
    }

    public void setDaysUntilExpiry(long daysUntilExpiry) {
        this.daysUntilExpiry = daysUntilExpiry;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getEnergyKcal() {
        return energyKcal;
    }

    public void setEnergyKcal(double energyKcal) {
        this.energyKcal = energyKcal;
    }

    public String getEnergyUnit() {
        return energyUnit;
    }

    public void setEnergyUnit(String energyUnit) {
        this.energyUnit = energyUnit;
    }

    public double getFat100g() {
        return fat100g;
    }

    public void setFat100g(double fat100g) {
        this.fat100g = fat100g;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public String getFatUnit() {
        return fatUnit;
    }

    public void setFatUnit(String fatUnit) {
        this.fatUnit = fatUnit;
    }

    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public String getProteinsUnit() {
        return proteinsUnit;
    }

    public void setProteinsUnit(String proteinsUnit) {
        this.proteinsUnit = proteinsUnit;
    }

    public double getSalt() {
        return salt;
    }

    public void setSalt(double salt) {
        this.salt = salt;
    }

    public String getSaltUnit() {
        return saltUnit;
    }

    public void setSaltUnit(String saltUnit) {
        this.saltUnit = saltUnit;
    }

    public double getSugars() {
        return sugars;
    }

    public void setSugars(double sugars) {
        this.sugars = sugars;
    }

    public String getSugarsUnit() {
        return sugarsUnit;
    }

    public void setSugarsUnit(String sugarsUnit) {
        this.sugarsUnit = sugarsUnit;
    }

    public String getAllergensTags() {
        return allergensTags;
    }

    public void setAllergensTags(String allergensTags) {
        this.allergensTags = allergensTags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusVerbose() {
        return statusVerbose;
    }

    public void setStatusVerbose(String statusVerbose) {
        this.statusVerbose = statusVerbose;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}

