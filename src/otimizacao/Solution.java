package otimizacao;

import java.util.*;
import java.util.stream.Collectors;

public class Solution implements Comparable<Solution>{
    private List<Bin> bins;

    public Solution() {

    }

    public Solution(List<Bin> bins) {
        this.bins = bins;
    }

    public Solution(Solution solution){
        this.bins = new ArrayList<>(solution.getBins().stream().map(bin -> new Bin(bin)).collect(Collectors.toList()));

    }

    public void setBins(List<Bin> bins) {
        this.bins = bins;
    }

    public List<Bin> getBins() {
        return bins;
    }

    public Integer getSize() {
        return this.bins.size();
    }

    @Override
    public int compareTo(Solution o) {
        return this.getSize().compareTo(o.getSize());
    }

    public List<Bin> getRandomBinOfSolution(int numberOfBins){
        List<Bin> randomBins = new ArrayList<>();
        Random random = new Random();
        for(int i=0; i<numberOfBins &&  this.getSize() > 0 ; i++){
            Bin bin =  this.bins.get(random.nextInt(this.getSize()));
            if(randomBins.contains(bin))
                i --;
            else{
                randomBins.add(bin);
                this.bins.remove(bin);
            }
        }

        return randomBins;
    }

    public void removeBinsWithItemsOfCrossingOver(List<Bin> crossingOverBins){
        List<Item> crossingOverItems = this.getAllItemsFromBins(crossingOverBins);
        Set<Bin> deletedBins = new HashSet<>();
        for (Bin bin: this.bins){
            if(binHasItems(bin, crossingOverItems)){
                deletedBins.add(bin);
            }
        }
        this.bins.removeAll(deletedBins);
    }

    public List<Item> getMissingItems(List<Item> items){
        items.removeAll(this.getAllItemsFromBins(this.getBins()));
        return items;
    }

    private List<Item> getAllItemsFromBins(List<Bin> bins){
        List<Item> items = new ArrayList<>();
        for(Bin bin: bins){
            items.addAll(bin.getItems());
        }
        return items;
    }

    private Boolean binHasItems(Bin bin, List<Item> items){
        for(Item item: bin.getItems()){
            if(items.contains(item)){
                return true;
            }
        }
        return false;
    }

    public void add(List<Bin> bins){
        this.bins.addAll(bins);
    }

    public void addItemsOnSolution(List<Item> items){
        items.sort(Comparator.comparing(Item::getWeight));
        for (Item item: items) {

            if(couldAddItemOnExistingBins(bins, item) == false){
                Bin newBin = new Bin(item);
                this.bins.add(newBin);
            }
        }
    }

    private Boolean couldAddItemOnExistingBins(List<Bin> bins, Item item){
        for(Bin bin: bins){
            if(bin.canAddItem(item)){
                bin.addItem(item);
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Size: " + this.getSize().toString() + " Number of item: " + this.numberOfItemsOnSolution() + " Valid Solution: " + this.isValid();
    }
    public String completeToString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.toString());
        for(Bin bin: this.getBins()){
            stringBuilder.append("\n");
            stringBuilder.append(bin);
        }
        return stringBuilder.toString();
    }
    public Integer numberOfItemsOnSolution(){
        Integer numItems = 0;
        for (Bin bin: this.getBins()) {
            numItems += bin.getItems().size();
        }
        return numItems;
    }

    public Boolean isValid(){
        List<Item> items = this.getAllItemsFromBins(this.bins);
        items.sort(Item::compareTo);
        Item lastItem = null;
        for(Item item: items){
            if(lastItem != null){
                if(lastItem.getId() + 1 != item.getId()){
                    return false;
                }
            }
        }
        return true;
    }
}