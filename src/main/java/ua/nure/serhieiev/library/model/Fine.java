package ua.nure.serhieiev.library.model;

public class Fine implements Identified{

    Integer id;
    Order order;
    Double cost;
    Boolean paid;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Fine setId(Integer id) {
        this.id = id;
        return this;
    }

    public Order getOrder() {
        return order;
    }

    public Fine setOrder(Order order) {
        this.order = order;
        return this;
    }

    public Double getCost() {
        return cost;
    }

    public Fine setCost(Double cost) {
        this.cost = cost;
        return this;
    }

    public Boolean getPaid() {
        return paid;
    }

    public Fine setPaid(Boolean paid) {
        this.paid = paid;
        return this;
    }

    @Override
    public String toString() {
        return "Fine{" +
                "id=" + id +
                ", order=" + order +
                ", cost=" + cost +
                ", paid=" + paid +
                '}';
    }

}
