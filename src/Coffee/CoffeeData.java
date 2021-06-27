package Coffee;

import java.util.ArrayList;
import DataManage.DataWriter;
public class CoffeeData {
    private int code;
    private String name;
    private int productionSeason;
    private String variety;
    private String productionArea;
    private String processMethod;
    private String flavor;
    private int price;
    private int inventory;
    private double rate;
    private int ratedPeople; 
    private String region;

    public CoffeeData(){}
    public CoffeeData(int code, String name, int productionSeason, String variety, String productionArea, String region, 
                        String processMethod, String flavor, int price, int inventory, double rate, int ratedPeople){
        // 編號
        this.code = code;
        // 品名
        this.name = new String(name);
        // 產季
        this.productionSeason = productionSeason;
        // 品種
        this.variety = new String(variety);
        // 產地
        this.productionArea = new String(productionArea);
        // 處理法
        this.processMethod = new String(processMethod);
        // 風味敘述
        this.flavor = new String(flavor);
        // 售價
        this.price = price;
        // 剩餘包數
        this.inventory = inventory;
        // 評分
        this.rate = rate;
        // 評分人數
        this.ratedPeople = ratedPeople;
        // 區域
        this.region = new String(region);
    }

    public static ArrayList<CoffeeData> converter(ArrayList<Integer> ids) {
        ArrayList<CoffeeData> container = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            container.add(DataWriter.getCoffeeInfo(ids.get(i)));
        }
        return container;
    }

    public double getRate() {
        return rate;
    }
    public void setRate(double rate) {
        this.rate = rate;
    }
    public int getRatedPeople() {
        return ratedPeople;
    }
    public void setRatedPeople(int ratedPeople) {
        this.ratedPeople = ratedPeople;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return int return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return int return the productionSeason
     */
    public int getProductionSeason() {
        return productionSeason;
    }

    /**
     * @param productionSeason the productionSeason to set
     */
    public void setProductionSeason(int productionSeason) {
        this.productionSeason = productionSeason;
    }

    /**
     * @return String return the variety
     */
    public String getVariety() {
        return variety;
    }

    /**
     * @param variety the variety to set
     */
    public void setVariety(String variety) {
        this.variety = variety;
    }

    /**
     * @return String return the productionArea
     */
    public String getProductionArea() {
        return productionArea;
    }

    /**
     * @param productionArea the productionArea to set
     */
    public void setProductionArea(String productionArea) {
        this.productionArea = productionArea;
    }

    /**
     * @return String return the processMethod
     */
    public String getProcessMethod() {
        return processMethod;
    }

    /**
     * @param processMethod the processMethod to set
     */
    public void setProcessMethod(String processMethod) {
        this.processMethod = processMethod;
    }

    /**
     * @return String return the flavor
     */
    public String getFlavor() {
        return flavor;
    }

    /**
     * @param flavor the flavor to set
     */
    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    /**
     * @return int return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * @return int return the inventory
     */
    public int getInventory() {
        return inventory;
    }

    /**
     * @param inventory the inventory to set
     */
    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

}
