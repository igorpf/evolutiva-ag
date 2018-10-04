/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otimizacao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author igor
 */
public class Bin {
    public static int capacity=100;

    private List<Item> items;

    private int weightUsed;

    public Bin(){
        this.items=new ArrayList<>();
        this.weightUsed=0;
    }

    public Bin(Item item) {
        this();
        this.addItem(item);
    }

    public Bin(Bin bin){
        this.items = new ArrayList<>(bin.getItems());
        this.weightUsed = bin.getWeightUsed();
    }

    public void removeItem(Item i){
        if(!items.remove(i)){
            throw new IllegalArgumentException("Item not found");
        }
        this.weightUsed-=i.getWeight();
    }
    public void removeItem(int index){
        Item removed=items.remove(index) ;
        if(removed==null){
            throw new IllegalArgumentException("Item not found");
        }
        this.weightUsed-=removed.getWeight();
    }

    public void addItem(Item i){
        if((i.getWeight()+this.weightUsed)<= Bin.capacity){
            this.items.add(i);
            weightUsed+=i.getWeight();
        }
        else
            throw new IllegalArgumentException("Bin already full!");
    }

    public void addItems(Collection<Item> items){
        items.forEach(item -> this.addItem(item));
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public boolean contains(Item i){
        return this.items.contains(i);
    }

    public boolean isEmpty(){
        return this.items.isEmpty();
    }

    public Integer getWeightUsed(){
        return this.weightUsed;
    }

    public Boolean canAddItem(Item item){
        return this.getWeightUsed() + item.getWeight() <= Bin.capacity;
    }

    public static void changeItemBin(Item i, Bin destination) throws IllegalArgumentException{
        if(destination.getWeightUsed()+i.getWeight()<=capacity){
            i.getBin().removeItem(i);
            destination.addItem(i);
        }
        else
            throw new IllegalArgumentException("Could not change bin!");

    }

    @Override
    public String toString(){
        StringBuilder b=new StringBuilder();
        b.append("Used weight: " + this.weightUsed);
        items.stream().forEach(i->{
            b.append("\t\t\n ");
            b.append(i);
        });
            b.append("\n ");
        return b.toString();
    }
}
