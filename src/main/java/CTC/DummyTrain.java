package CTC;

import TrackModel.*;
import java.util.*;

public class DummyTrain {

	private Integer id;
	private Block position;
	private Block authority;
	private double suggSpeed;
	// path is a list of blocks that have the speed and authority that we want to set on train
	private ArrayList<Block> path;
	private Block lastStationVisited;

	public DummyTrain(){
		this.setID(id);
		this.setPosition(position);
		this.setPath(path);
		this.setLastStation(lastStationVisited);
	}

	public DummyTrain(int id){
		this.id = id;
	}

	public DummyTrain(Block pos, ArrayList<Block> p){
		id = -1;
		position = pos;
		path = p;
		// sets last station to yard which is the block it is originally set on at creation
		lastStationVisited = pos;
	}

	public DummyTrain(Block pos){
		id = -1;
		position = pos;
		// sets last station to yard which is the block it is originally set on at creation
		lastStationVisited = pos;
	}

	public Block getLastStation(){
		return lastStationVisited;
	}

	public void setLastStation(Block lastStationVisited){
		this.lastStationVisited = lastStationVisited;
	}

	public Integer getID(){
		return id;
	}

	public void setID(Integer id){
		this.id = id;
	}

	public Block getPosition(){
    	return position;
	}


	public void setPosition(Block position){
		this.position = position;
	}

	public Block getAuthority() {
		return authority;
	}

	public void setAuthority(Block authority) {
		this.authority = authority;
	}

	public double getSuggSpeed() {
		return suggSpeed;
	}

	public void setSuggSpeed(double suggSpeed) {
		this.suggSpeed = suggSpeed;
	}

	public ArrayList<Block> getPath(){
		return path;
	}

	public void setPath(ArrayList<Block> path){
		this.path = path;
		// sendToWS(path);
	}
}
