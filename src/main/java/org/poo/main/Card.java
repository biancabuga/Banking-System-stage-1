package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class Card implements JsonOutput{
    private String cardNUmber;
    private String status;
    private int isBlocked = 0;
    private String typeOfCard;

    public String getTypeOfCard() {
        return typeOfCard;
    }

    public void setTypeOfCard(String typeOfCard) {
        this.typeOfCard = typeOfCard;
    }

    public int getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(int isBlocked) {
        this.isBlocked = isBlocked;
    }

    public Card(String cardNUmber, String status, String typeOfCard) {
        this.cardNUmber = cardNUmber;
        this.status = status;
        this.typeOfCard = typeOfCard;
    }

    public String getCardNUmber() {
        return cardNUmber;
    }

    public void setCardNUmber(String cardNUmber) {
        this.cardNUmber = cardNUmber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public ObjectNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cardNode = mapper.createObjectNode();
        cardNode.put("cardNumber", cardNUmber);
        cardNode.put("status", status);
        return cardNode;
    }
}
