package com.nighthawk.spring_portfolio.mvc.checkpoints;
import java.util.ArrayList;
import javax.persistence.Entity;

import lombok.*;

@Data
@Entity

public class Stage extends DataObject implements DataObject.InterfaceToJson {

    // Stage data
	private String name;
	private int stage;
	private ArrayList<String> listLevels; 
    // to string to json

    	/* 'Generics' requires toString override
	 * toString provides data based off of Static Key setting
	 */
	@Override
	public String toString()
	{
		return "Name: " + name + ", Stage: " + stage;
	}

	/* 'Generics' requires toJson override
	 * toJson provides data based off of Static Key setting
	 */
	@Override
	public String toJson()
	{
		// return all data in json format
		return "{ \"Name\": \"" + name + "\", \"Stage\": \"" + stage + "\"}";
	}

    public static void main(String[] args)
	{
	}
}
