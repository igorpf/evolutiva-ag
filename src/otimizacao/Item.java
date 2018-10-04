/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otimizacao;

/**
 *
 * @author igor
 */
public class Item implements Comparable<Item>{

    private static Integer lastIdentifier = 0;

    private Integer id;

    private int weight;

    private Bin bin;


    public Item(int weight) throws IllegalArgumentException{
        this.setWeight(weight);
        this.id = getNewId();
    }

    public Integer getNewId(){
        return ++Item.lastIdentifier;
    }
    public Bin getBin() {
        return bin;
    }

    public void setBin(Bin bin) {
        this.bin = bin;
    }

    public Integer getId() {
        return id;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(final int weight) throws IllegalArgumentException {
        if (weight >= 0) {
            this.weight = weight;
        } else {
            throw new IllegalArgumentException("Weight must be positive!");
        }
    }

    @Override
    public String toString() {
        return "Item: " + this.id.toString() + " weight: " + this.getWeight().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Item)
            return this.id.equals(((Item) obj).getId());
        else
            return super.equals(obj);
    }

    @Override
    public int compareTo(Item o) {
        return this.id.compareTo(o.getId());
    }
}
