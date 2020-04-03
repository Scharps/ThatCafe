package models;


//Potential issue with having a 'Special' type. If a food item is moved to a special,
//information is lost on what type it is. Perhaps SpecialFood/SpecialDrink?
public enum MenuItemType {
    Food,
    Drink,
    Special
}
